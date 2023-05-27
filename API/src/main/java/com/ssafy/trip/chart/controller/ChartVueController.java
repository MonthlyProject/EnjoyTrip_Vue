package com.ssafy.trip.chart.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.trip.chart.model.service.ChartService;

@RestController
@RequestMapping("/chart")
@CrossOrigin("*")
public class ChartVueController {
	private final Logger logger = LoggerFactory.getLogger(ChartVueController.class);

	private ChartService chartService;
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";
	
	@Autowired
	public ChartVueController(ChartService chartService) {
		super();
		this.chartService = chartService;
	}
	
	@GetMapping(value="/sido")
	public ResponseEntity<?> sido(Map<String, Object> map) {
		try {
			List<String> list1 = chartService.getSidoName();
			List<Integer> list2 = chartService.getSidoCnt();
			map.put("labels", list1);
			map.put("data", list2);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping(value="/gugun")
	public ResponseEntity<?> gugun(Map<String, Object> map) {
		try {
			List<String> list1 = chartService.getGugunName();
			List<Integer> list2 = chartService.getGugunCnt();
			map.put("labels", list1);
			map.put("data", list2);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}
	
	@GetMapping(value="/content")
	public ResponseEntity<?> content(Map<String, Object> map) {
		try {
			List<String> list1 = new ArrayList<String>();
			list1.add("관광지");
			list1.add("문화시설");
			list1.add("축제공연행사");
			list1.add("여행코스");
			list1.add("레포츠");
			list1.add("숙박");
			list1.add("쇼핑");
			list1.add("음식점");
			List<Integer> list2 = chartService.getContentCnt();
			map.put("labels", list1);
			map.put("data", list2);
			return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
		}catch(Exception e) {
			return exceptionHandling(e);
		}
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
