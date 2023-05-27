package com.ssafy.trip.map.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.trip.map.model.GugunDto;
import com.ssafy.trip.map.model.MarkerDto;
import com.ssafy.trip.map.model.SidoDto;
import com.ssafy.trip.map.model.StarDto;
import com.ssafy.trip.map.model.mapper.MapMapper;

@Service
public class MapServiceImpl implements MapService {
	
	private MapMapper mapMapper;
	
	public MapServiceImpl(MapMapper mapMapper) {
		super();
		this.mapMapper = mapMapper;
	}

	//서울, 인천, 부산, 대구... : 검색의 선택박스에 넣을 데이터 가져오기
	@Override
	public List<SidoDto> getSido() throws Exception {
		return mapMapper.getSido();
	}

	@Override
	public List<GugunDto> getGugun(int sidoCode) throws Exception {
		return mapMapper.getGugun(sidoCode);
	}

	@Override
	public List<MarkerDto> getMarker(int area, int areaarea, int type) throws Exception {
		mapMapper.updateSido(area);
		mapMapper.updateGugun(area, areaarea);
		mapMapper.updateContentType(type);
		return mapMapper.getMarker(area, areaarea, type);
	}

	@Override
	public void addStar(Map<String, String> map) throws Exception {
		mapMapper.addStar(map);
	}

	@Override
	public List<MarkerDto> getStar(Map<String, String> map) throws Exception {
		return mapMapper.getStar(map);
	}

	@Override
	public void deleteStar(String userId, String title, String contentId) throws Exception {
		mapMapper.deleteStar(userId, title, contentId);
		
	}

	@Override
	public List<String> getGroup(String userId) throws Exception {
		return mapMapper.getGroup(userId);
	}

	@Override
	public void deleteGroup(String userId, String title) throws Exception {
		mapMapper.deleteGroup(userId, title);
		
	}

	@Override
	public void createGroup(Map<String, String> map) throws Exception {
		mapMapper.createGroup(map);
	}

	@Override
	public List<MarkerDto> getDetail(String contentId) throws Exception {
		return mapMapper.getDetail(contentId);
	}

}
