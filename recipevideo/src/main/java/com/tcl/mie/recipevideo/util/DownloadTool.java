/**
 * 
 */
package com.tcl.mie.recipevideo.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * @author qiang_wang
 *
 */
public class DownloadTool {
	/**
	 * 
	 * 每个线程下载的字节数
	 */

	private long unitSize = 1024 * 1024;
	int ThreadNum = 5;

	private ThreadPoolExecutor threadPool;

	private CloseableHttpClient httpClient;

	private String url;
	private String localPath;
	private String fileName;

	private List<String> retList = null;

	private DownloadTool() {

	}

	public DownloadTool(String url, String localPath, String fileName) {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
		cm.setMaxTotal(100);
		httpClient = HttpClients.custom().setConnectionManager(cm).build();
		this.localPath = localPath;
		this.url = url;
		this.fileName = fileName;
		threadPool = new ThreadPoolExecutor(ThreadNum, ThreadNum, 30, TimeUnit.SECONDS,
				new ArrayBlockingQueue(1024), new ThreadPoolExecutor.CallerRunsPolicy());// 如果队列满了，重试添加当前任务.
	}

	public FileInfo down(List<String> retList) {
		FileInfo info = null;
		this.retList = retList;
		try {
			info = execute(url);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	private FileInfo execute(String url) throws Exception {
		if (this.fileName == null || "".equals(this.fileName)) {
			System.out.println("*******" + url);
			fileName = new URL(url).getFile();
			log("开始下载远程文件" + fileName);
			fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length())
					.replace("%20", " ");
			if (fileName.indexOf("?") != -1) {
				fileName = fileName.substring(0, fileName.indexOf("?"));
			}
		}

		log("远程文件本地存储名称" + fileName);
		long fileSize = this.getRemoteFileSize(url);

		this.createFile(localPath + fileName, fileSize);
		Long threadCount = (fileSize / unitSize) + (fileSize % unitSize != 0 ? 1 : 0);
		long offset = 0;

		CountDownLatch end = new CountDownLatch(threadCount.intValue());
		if (fileSize <= unitSize) {// 如果远程文件尺寸小于等于unitSize
			DownloadThread downloadThread = new DownloadThread(url, localPath + fileName, offset,
					fileSize, end, httpClient);
			threadPool.execute(downloadThread);

		} else {// 如果远程文件尺寸大于unitSize
			for (int i = 1; i < threadCount; i++) {
				DownloadThread downloadThread = new DownloadThread(url, localPath + fileName,
						offset, unitSize, end, httpClient);
				threadPool.execute(downloadThread);
				offset = offset + unitSize;
			}

			if (fileSize % unitSize != 0) {// 如果不能整除，则需要再创建一个线程下载剩余字节
				DownloadThread downloadThread = new DownloadThread(url, localPath + fileName,
						offset, fileSize - unitSize * (threadCount - 1), end, httpClient);
				threadPool.execute(downloadThread);
			}

		}
		try {
			end.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log("下载完成！localPath" + fileName);
		FileInfo fileInfo = new FileInfo();
		fileInfo.setName(fileName);
		fileInfo.setLength(fileSize);
		return fileInfo;
		// return localPath+fileName;
	}

	/**
	 * 
	 * 获取远程文件尺寸
	 */

	private long getRemoteFileSize(String remoteFileUrl) throws IOException {
		long fileSize = 0;
		HttpURLConnection httpConnection = (HttpURLConnection) new URL(remoteFileUrl)
				.openConnection();
		httpConnection.setRequestMethod("HEAD");
		int responseCode = httpConnection.getResponseCode();
		if (responseCode >= 400) {
			log("Web服务器响应错误!url=" + remoteFileUrl);
			return 0;
		}
		String sHeader;
		for (int i = 1;; i++) {
			sHeader = httpConnection.getHeaderFieldKey(i);
			if (sHeader != null && sHeader.equals("Content-Length")) {
				log("文件大小ContentLength:" + httpConnection.getContentLength());
				fileSize = Long.parseLong(httpConnection.getHeaderField(sHeader));
				break;
			}
		}
		return fileSize;
	}

	/**
	 * 
	 * 创建指定大小的文件
	 */
	private void createFile(String fileName, long fileSize) throws IOException {
		File newFile = new File(fileName);
		RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
		raf.setLength(fileSize);
		raf.close();
	}

	private void log(String logtxt) {
		System.out.println(logtxt);
		retList.add(logtxt);
	}
}
