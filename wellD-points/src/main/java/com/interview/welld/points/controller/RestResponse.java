package com.interview.welld.points.controller;

public class RestResponse{
	private Integer status;
	private Object content;
	
	public RestResponse() {}

	public RestResponse(Integer status, Object content) {
		this.setStatus(status);
		this.setContent(content);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
}