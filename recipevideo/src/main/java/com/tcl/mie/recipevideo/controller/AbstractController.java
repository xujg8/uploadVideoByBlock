/**
 * 
 */
package com.tcl.mie.recipevideo.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.tcl.mie.recipevideo.util.Constants;
import com.tcl.mie.recipevideo.vo.JsonResult;

/**
 * @author qiang_wang
 *
 */
public class AbstractController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	protected JsonResult buildSuccess(Object data) throws IOException {
		return new JsonResult(Constants.RETCODE_SUCCESS, "Success", data);
	}

	protected JsonResult buildSystemError(String msg) {
		if (StringUtils.isEmpty(msg)) {
			msg = "System Error";
		}
		return new JsonResult(Constants.RETCODE_SERVER_ERROR, msg, null);
	}

	/**
	 * 统一处理接口调用产生的异常
	 *
	 * @param e
	 *            异常
	 * @return json格式的错误信息
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public JsonResult handleException(Exception e, HttpServletRequest request) {
		Integer code = Constants.RETCODE_SERVER_ERROR;
		String msg = "System Error";

		// 请求参数异常
		if (e instanceof BindException) {
			BindException ex = (BindException) e;
			StringBuilder sb = new StringBuilder();
			List<FieldError> errors = ex.getFieldErrors();

			for (FieldError error : errors) {
				sb.append(error.getField()).append(":").append(error.getDefaultMessage())
						.append(";");
			}
			code = Constants.RETCODE_PARAMS_INVALID;
			msg = sb.toString();
		}

		logger.error(request.getRequestURI() + " " + code + " " + msg, e);

		return new JsonResult(code, msg, null);
	}
}
