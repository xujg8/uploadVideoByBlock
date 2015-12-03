/**
 * 
 */
package com.tcl.mie.recipevideo.controller;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.tcl.mie.recipevideo.util.Constants;
import com.tcl.mie.recipevideo.util.Encrypt;
import com.tcl.mie.recipevideo.vo.JsonResult;
import com.tcl.mie.recipevideo.vo.LoginVo;

/**
 * @author qiang_wang
 *
 */
@Controller
@RequestMapping("/")
public class LoginController extends AbstractController {

	@Value("${login.name}")
	private String account;
	@Value("${login.passwd}")
	private String passwd;

	@ResponseBody
	@RequestMapping(value = "/login.json", method = RequestMethod.POST)
	public JsonResult login(LoginVo vo, HttpServletResponse response) throws Exception {
		if (StringUtils.isEmpty(vo.getAccount()) || StringUtils.isEmpty(vo.getPasswd())) {
			return buildSystemError("Account and password cannot be null");
		}
		if (account.equals(vo.getAccount()) && passwd.equals(vo.getPasswd())) {
			// 增加cookie
			String uuid = UUID.randomUUID().toString();
			Cookie loginKey = new Cookie(Constants.COOKIE_LOGIN_KEY, uuid);
			loginKey.setPath("/");
			response.addCookie(loginKey);
			String value = Encrypt.md5(uuid + Constants.COOKIE_LOGIN_SECRET);
			Cookie loginValue = new Cookie(Constants.COOKIE_LOGIN_VALUE, value);
			loginValue.setPath("/");
			response.addCookie(loginValue);
			return buildSuccess("");
		} else {
			return buildSystemError("Account or password is wrong!");
		}
	}
}
