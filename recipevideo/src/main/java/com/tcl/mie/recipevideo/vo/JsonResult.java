/**
 * 
 */
package com.tcl.mie.recipevideo.vo;

/**
 * @author qiang_wang
 *
 */
public class JsonResult {
	private Integer code;
	private String msg;
	private Object data;

	public JsonResult(Integer retCode, String retMsg, Object data) {
		this.code = retCode;
		this.msg = retMsg;
		this.data = data;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
