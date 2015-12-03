/**
 * 
 */
package com.tcl.mie.recipevideo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.mie.recipevideo.db.VideoMapper;
import com.tcl.mie.recipevideo.db.VideoUrlsMapper;
import com.tcl.mie.recipevideo.model.Video;
import com.tcl.mie.recipevideo.model.VideoUrls;

/**
 * @author qiang_wang
 *
 */
@Service
public class VideoService {
	@Autowired
	VideoMapper videoMapper;
	@Autowired
	VideoUrlsMapper urlsMapper;

	public int insert(Video video) {
		int num = videoMapper.insert(video);
		if (num > 0) {
			VideoUrls videoUrls = new VideoUrls();
			videoUrls.setCreatetime(video.getCreatetime());
			videoUrls.setPid(video.getId());
			videoUrls.setMp((byte) 0);
			videoUrls.setHd((byte) 1);
			videoUrls.setFtype("MP4");
			videoUrls.setNo((byte) 1);
			videoUrls.setQuality("单段|标清|MP4");
			videoUrls.setVideourl(video.getSourceurl());
			videoUrls.setUpdatetime(video.getCreatetime());
			urlsMapper.insert(videoUrls);
		}
		return num;
	}
}
