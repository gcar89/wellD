package com.interview.welld.points.controller;

public class RestError{
	private Integer status;
	private String description;
	
	public RestError() {}

	public RestError(Integer status, String description) {
		this.setStatus(status);
		this.setDescription(description);
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}