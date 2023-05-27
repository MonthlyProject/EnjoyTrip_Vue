package com.ssafy.trip.map.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.board.controller.BoardVueController;
import com.ssafy.trip.board.model.BoardDto;
import com.ssafy.trip.map.model.GugunDto;
import com.ssafy.trip.map.model.MarkerDto;
import com.ssafy.trip.map.model.SidoDto;
import com.ssafy.trip.map.model.StarDto;
import com.ssafy.trip.map.model.service.MapService;
import com.ssafy.trip.user.model.UserDto;

@RestController
@RequestMapping("/vmap")
@CrossOrigin("*")
public class MapVueController {
	private final Logger logger = LoggerFactory.getLogger(MapVueController.class);

	private MapService mapService;
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	public MapVueController(MapService mapService) {
		super();
		this.mapService = mapService;
	}
	
	@GetMapping(value="/area")
	public ResponseEntity<?> area() {
		try {
			List<SidoDto> list = mapService.getSido();
			return new ResponseEntity<List<SidoDto>>(list, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping(value="/{areaCode}")
	public ResponseEntity<?> areaarea(@PathVariable("areaCode") String areaCode){
		try {
			List<GugunDto> list = mapService.getGugun(Integer.parseInt(areaCode));
			return new ResponseEntity<List<GugunDto>>(list, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping(value="/search/{areaCode}/{areaareaCode}/{contentTypeId}")
	public ResponseEntity<?> search(@PathVariable("areaCode") String area, @PathVariable("areaareaCode") String areaarea, @PathVariable("contentTypeId") String type){
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		try {
			List<MarkerDto> list = mapService.getMarker(Integer.parseInt(area), Integer.parseInt(areaarea), Integer.parseInt(type));
			resultMap.put("mapList", list);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		}catch(Exception e) {
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
//	@PostMapping(value="/addstar/{markerId}/{userId}/{title}")
	@PostMapping
	public ResponseEntity<?> addstar(@RequestBody Map<String, String> map) throws Exception{
		mapService.addStar(map);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@PostMapping(value="/getstar")
	public ResponseEntity<?> getstar(@RequestBody Map<String, String> map) throws Exception{
		List<MarkerDto> list = mapService.getStar(map);
		return new ResponseEntity<List<MarkerDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping(value="/star/group/{userId}")
	public ResponseEntity<?> group(@PathVariable("userId") String userId) {
		try {
			List<String> list = mapService.getGroup(userId);
			logger.info("getGroup : ", list);
			return new ResponseEntity<List<String>>(list, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@DeleteMapping("/star/{userId}/{title}/{contentId}")
	public ResponseEntity<String> deleteStar(@PathVariable("userId") String userId, @PathVariable("title") String title, @PathVariable("contentId") String contentId) throws Exception {
		logger.info("deleteStar - 호출");
		mapService.deleteStar(userId, title, contentId);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@PostMapping(value="/star/group")
	public ResponseEntity<String> newgrouptitle(@RequestBody Map<String, String> map) throws Exception {
		logger.info("newgrouptitle - 호출");
		mapService.createGroup(map);
			return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/star/{userId}/{title}")
	public ResponseEntity<String> deleteGroup(@PathVariable("userId") String userId, @PathVariable("title") String title) throws Exception {
		logger.info("deleteGroup - 호출");
		mapService.deleteGroup(userId, title);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@GetMapping(value="/detail/{contentId}")
	public ResponseEntity<?> getDetail(@PathVariable("contentId") String contentId){
		try {
			List<MarkerDto> list = mapService.getDetail(contentId);
			return new ResponseEntity<List<MarkerDto>>(list, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
