package com.ssafy.trip.map.model;

public class MarkerDto {
	int contentId;
	int contentTypeId;
	String title;
	String addr1;
	String addr2;
	String firstImage;
	int readcount;
	double latitude;
	double longitude;
	String overview;
	
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
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
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getFirstImage() {
		return firstImage;
	}
	public void setFirstImage(String firstImage) {
		this.firstImage = firstImage;
	}
	public int getReadcount() {
		return readcount;
	}
	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	@Override
	public String toString() {
		return "MarkerDto [contentId=" + contentId + ", contentTypeId=" + contentTypeId + ", title=" + title
				+ ", addr1=" + addr1 + ", addr2=" + addr2 + ", firstImage=" + firstImage + ", readcount=" + readcount
				+ ", latitude=" + latitude + ", longitude=" + longitude + ", overview=" + overview + "]";
	}
	
}
