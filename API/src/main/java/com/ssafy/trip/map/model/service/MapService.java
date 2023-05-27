package com.ssafy.trip.map.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.trip.map.model.GugunDto;
import com.ssafy.trip.map.model.MarkerDto;
import com.ssafy.trip.map.model.SidoDto;
import com.ssafy.trip.map.model.StarDto;

public interface MapService {
	List<SidoDto> getSido() throws Exception;
	List<GugunDto> getGugun(int sidoCode) throws Exception;
	List<MarkerDto> getMarker(int area, int areaarea, int type) throws Exception;
	void addStar(Map<String, String> map) throws Exception;
	List<MarkerDto> getStar(Map<String, String> map) throws Exception;
	void deleteStar(String userId, String title, String contentId) throws Exception;
	List<String> getGroup(String userId) throws Exception;
	void deleteGroup(String userId, String title) throws Exception;
	void createGroup(Map<String, String> map) throws Exception;
	List<MarkerDto> getDetail(String contentId) throws Exception;
}
