package com.frame.core.http;

/**
 * 
 * @ClassName: HttpStatus
 * @Description: TODO(请求状态)
 * @author lidong
 * @date 2018年12月25日
 *
 */
public enum HttpStatus {

	/**
	 * 请求成功
	 */
	OK(200, "OK"),

	/**
	 * 未授权：没有与服务器认证成功就访问服务器资源
	 */
	UNLOGIN(402, "账号、密码错误！"),

	UNAUTHORIZED(401, "会话过期,请重新登录!"),

	/**
	 * 权限不足：认证成功，但访问了没有权限的接口
	 */
	PERMISSION_DENIED(403, "没有相关权限"),

	NOT_FOUND(404, "资源不存在"),

	TOKEN_TIMEOUT(405, "会话超时,请重新登陆"),

	REQUEST_PARAMS_ILLEGAL(406, "参数错误"),

	REFRESH_TOKEN_FAILED(407, "刷新token失败！"),

	INTERNAL_SERVER_ERROR(500, "服务器异常");

	private final int value;

	private final String title;

	HttpStatus(int value, String title) {
		this.value = value;
		this.title = title;
	}

	public int value() {
		return this.value;
	}

	public String getTitle() {
		return this.title;
	}

	public static HttpStatus valueOf(int statusCode) {
		for (HttpStatus status : values()) {
			if (status.value == statusCode) {
				return status;
			}
		}
		throw new IllegalArgumentException("未知状态 [" + statusCode + "]");
	}

}
