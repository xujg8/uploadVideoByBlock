package com.tcl.mie.recipevideo.model;

import java.util.Date;

public class VideoUrls {
	private Integer id;

	private Integer pid;

	private String quality;

	private Byte mp;

	private Byte hd;

	private String ftype;

	private Byte no;

	private String videourl;

	private Date createtime;

	private Date updatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public String getQuality() {
		return quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	public Byte getMp() {
		return mp;
	}

	public void setMp(Byte mp) {
		this.mp = mp;
	}

	public Byte getHd() {
		return hd;
	}

	public void setHd(Byte hd) {
		this.hd = hd;
	}

	public String getFtype() {
		return ftype;
	}

	public void setFtype(String ftype) {
		this.ftype = ftype;
	}

	public Byte getNo() {
		return no;
	}

	public void setNo(Byte no) {
		this.no = no;
	}

	public String getVideourl() {
		return videourl;
	}

	public void setVideourl(String videourl) {
		this.videourl = videourl;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
}