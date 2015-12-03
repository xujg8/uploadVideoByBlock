package com.tcl.mie.recipevideo.db;

import com.tcl.mie.recipevideo.model.VideoUrls;

@Mapper
public interface VideoUrlsMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(VideoUrls record);

	int insertSelective(VideoUrls record);

	VideoUrls selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(VideoUrls record);

	int updateByPrimaryKeyWithBLOBs(VideoUrls record);

	int updateByPrimaryKey(VideoUrls record);
}