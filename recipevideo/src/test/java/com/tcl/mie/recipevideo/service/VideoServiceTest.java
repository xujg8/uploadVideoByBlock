package com.tcl.mie.recipevideo.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.tcl.mie.recipevideo.db.Mapper;
import com.tcl.mie.recipevideo.model.Video;

@Mapper
public class VideoServiceTest extends BaseTestCase {
	@Autowired
	VideoService service;

	@Test
	public void test() {
		Video video = new Video();
		video.setAccount("wangwei_fj@163.com");
		video.setCreatetime(new Date());
		video.setName("test_filename");
		video.setSource("");
		video.setSourceurl("test_url");
		video.setStatus((byte) 1);
		Long seconds = 5430000L;
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		video.setDuration(getFormatDuration(seconds));
		System.out.println(video.getDuration());
		int num = service.insert(video);
	}

	private String getFormatDuration(long ms) {
		long hour = ms / (60 * 60 * 1000);
		long minute = (ms - hour * 60 * 60 * 1000) / (60 * 1000);
		long second = (ms - hour * 60 * 60 * 1000 - minute * 60 * 1000) / 1000;
		return hour + ":" + minute + ":" + second;
	}

}
