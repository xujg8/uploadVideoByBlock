/**
 * 
 */
package com.tcl.mie.recipevideo.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Strings;
import com.tcl.mie.recipevideo.util.Constants;
import com.tcl.mie.recipevideo.util.Encrypt;

/**
 * @author qiang_wang
 *
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler) throws Exception {
		// TODO Auto-generated method stub
		Cookie[] cookies = request.getCookies();

		String loginKey = "";
		String loginValue = "";
		if (null != cookies) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie cookie = cookies[i];
				if (cookie.getName().equals(Constants.COOKIE_LOGIN_KEY)) {
					loginKey = cookie.getValue();
				} else if (cookie.getName().equals(Constants.COOKIE_LOGIN_VALUE)) {
					loginValue = cookie.getValue();
				}
			}
		}
		String md5str = Encrypt.md5(loginKey + Constants.COOKIE_LOGIN_SECRET);
		if (md5str.equals(loginValue)) {
			return true;
		}
		response.sendRedirect(getReturnUrl(request));
		return false;
	}

	private String getReturnUrl(HttpServletRequest request) {
		String returnUrl = request.getRequestURL().toString();
		if (!Strings.isNullOrEmpty(request.getQueryString())) {
			returnUrl = returnUrl + "?" + request.getQueryString();
		}
		if (returnUrl.indexOf(".do") != -1) {
			returnUrl = returnUrl.substring(0, returnUrl.lastIndexOf("/"));
		}
		return returnUrl;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
			Object handler, ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		// TODO Auto-generated method stub

	}

}
