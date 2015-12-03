package com.tcl.mie.recipevideo.db;

import com.tcl.mie.recipevideo.model.Video;

@Mapper
public interface VideoMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Video record);

	int insertSelective(Video record);

	Video selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Video record);

	int updateByPrimaryKeyWithBLOBs(Video record);

	int updateByPrimaryKey(Video record);
}