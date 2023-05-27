package com.ssafy.trip.map.model;

public class StarDto {
	private String userId;
	private int contentTypeId;
	private String title;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getContentTypeId() {
		return contentTypeId;
	}
	public void setContentTypeId(int contentTypeId) {
		this.contentTypeId = contentTypeId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "StarDto [userId=" + userId + ", contentTypeId=" + contentTypeId + ", title=" + title + "]";
	}

}
