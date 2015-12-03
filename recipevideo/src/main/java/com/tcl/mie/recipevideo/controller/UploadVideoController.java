/**
 * 
 */
package com.tcl.mie.recipevideo.controller;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tcl.mie.recipevideo.model.Video;
import com.tcl.mie.recipevideo.service.VideoService;
import com.tcl.mie.recipevideo.util.FileInfo;
import com.tcl.mie.recipevideo.vo.JsonResult;
import com.tcl.mie.recipevideo.vo.UploadVo;
import com.tcl.storage.bean.bpc.FileBlockInfo;
import com.tcl.storage.bean.bpc.VedioInfoResult;
import com.tcl.storage.bean.bpc.VedioMetaDataInfo;
import com.tcl.storage.client.BreakpointContinuinglyUploadClient;

/**
 * @author qiang_wang
 *
 */
@Controller
@RequestMapping("/")
// @ServerEndpoint("/hello")
public class UploadVideoController extends AbstractController {

	@Value("${file.localpath}")
	private String localPath;
	@Value("${file.token}")
	private String token;
	@Value("${file.blocksize}")
	private int blockSize;

	@Autowired
	private VideoService service;

	private List<String> retList = new ArrayList<String>();

	@ResponseBody
	@RequestMapping(value = "/upload.json", method = RequestMethod.POST)
	public JsonResult upload(UploadVo vo) throws Exception {
		// 先返回成功接收的消息
		String uuid = java.util.UUID.randomUUID().toString();
		retList.add("开始下载远程文件，流水号" + uuid);
		// 开始从url下载文件
		// DownloadTool tool = new DownloadTool(vo.getFileDownUrl(), localPath,
		// vo.getFileName());
		// FileInfo fileInfo = tool.down(retList);
		FileInfo fileInfo = new FileInfo();
		File file = new File(localPath + "1CJksWG8pT");
		fileInfo.setName("1CJksWG8pT");
		fileInfo.setLength(file.length());
		if (fileInfo == null) {// 文件下载失败
			return buildSystemError("Down file from " + vo.getFileDownUrl() + " error.");
		}

		BreakpointContinuinglyUploadClient client = BreakpointContinuinglyUploadClient
				.getInstance();
		retList.add("初始化dubbo连接完毕，流水号" + uuid);
		// String uuid = "1642e23e-eeec-4046-9a48-4b4914c2d3d1";
		// 下载完毕，开始上传到北京服务器
		VedioMetaDataInfo dataInfo = new VedioMetaDataInfo();
		dataInfo.setFileName(fileInfo.getName());
		dataInfo.setContentLenth(fileInfo.getLength());
		dataInfo.setFileUniqueId(uuid);
		dataInfo.setToken(token);
		dataInfo.setType(1);
		VedioInfoResult result = client.uploadVedioMetaDataInfo(dataInfo);
		retList.add("云端已存在的视频信息，流水号" + uuid + "|" + result.toJson());
		if (result.getStatus() == 0) {
			return buildSystemError("上传视频meta接口返回错误|" + result.getMsg());
		}
		while (result.getStatus() == 1 && result.getUploadStatus() == 0) {
			// 从断点坐标开始加载
			long currentProgress = result.getCurrentProgress();
			retList.add("向云端分块上传视频信息，pos=" + currentProgress + "|" + result.toJson());
			byte[] blockBuff = load(new File(localPath + fileInfo.getName()), currentProgress);
			// 继续上传
			FileBlockInfo blockInfo = new FileBlockInfo();
			blockInfo.setToken(token);
			blockInfo.setType(1);
			blockInfo.setFileUniqueId(uuid);
			blockInfo.setCurrentProgress(currentProgress);
			blockInfo.setBlockBuff(blockBuff);
			result = client.uploadVedioByBlock(blockInfo);
			retList.add("向云端分块上传视频信息结束，pos=" + result.getCurrentProgress() + "|" + result.toJson());
			System.out.println(result.toJson());
		}
		if (result.getStatus() == 0) {
			return buildSystemError("上传视频分块时返回错误|" + result.getMsg());
		} else {
			// 上传成功，入库
			Video video = new Video();
			video.setAccount("wangwei_fj@163.com");
			video.setCreatetime(new Date());
			video.setName(vo.getFileName());
			video.setSourceurl(result.getDomain() + result.getKey());
			video.setStatus((byte) 0);
			video.setUpdatetime(video.getCreatetime());
			video.setDuration(getFormatDuration(result.getVideoTime()));
			service.insert(video);
		}
		return buildSuccess("上传视频成功！");
	}

	private String getFormatDuration(long ms) {
		long hour = ms / (60 * 60 * 1000);
		long minute = (ms - hour * 60 * 60 * 1000) / (60 * 1000);
		long second = (ms - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
		return hour + ":" + minute + ":" + second;
	}

	public byte[] load(File file, long offset) {
		byte[] result = new byte[blockSize];
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(file, "r");
			accessFile.seek(offset);
			int readSize = accessFile.read(result);
			if (readSize == -1) {
				return null;
			} else if (readSize == blockSize) {
				return result;
			} else {
				byte[] tmpByte = new byte[readSize];
				System.arraycopy(result, 0, tmpByte, 0, readSize);
				return tmpByte;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e1) {
				}
			}
		}
		return null;
	}

	@RequestMapping(value = "/upload.do")
	public String goupload() {
		return "upload";
	}

	@ResponseBody
	@RequestMapping(value = "/query.json", method = RequestMethod.POST)
	public JsonResult test() throws Exception {
		Random random = new Random();
		retList.add("" + random.nextInt(100));
		List<String> retMsgs = new ArrayList<String>();
		synchronized (retList) {
			Iterator<String> itor = retList.iterator();
			while (itor.hasNext()) {
				retMsgs.add(itor.next());
			}
		}
		return buildSuccess(retMsgs);
		// return buildSystemError("sss");
	}

	// @ResponseBody
	// @RequestMapping(value = "/login.json", method = RequestMethod.POST)
	// public JsonResult login(LoginVo vo) throws Exception {
	// if (StringUtils.isEmpty(vo.getAccount()) ||
	// StringUtils.isEmpty(vo.getPasswd())) {
	// return buildSystemError("Account and password cannot be null");
	// }
	// if (account.equals(vo.getAccount()) && passwd.equals(vo.getPasswd())) {
	// return buildSuccess("");
	// } else {
	// return buildSystemError("Account or password is wrong!");
	// }
	// }
}
