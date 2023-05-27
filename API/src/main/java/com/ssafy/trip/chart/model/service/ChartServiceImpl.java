package com.ssafy.trip.chart.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.trip.chart.model.mapper.ChartMapper;

@Service
public class ChartServiceImpl implements ChartService {
	
	private ChartMapper chartMapper;
	
	public ChartServiceImpl(ChartMapper chartMapper) {
		super();
		this.chartMapper = chartMapper;
	}

	@Override
	public List<String> getSidoName() throws Exception {
		return chartMapper.getSidoName();
	}

	@Override
	public List<Integer> getSidoCnt() throws Exception {
		return chartMapper.getSidoCnt();
	}

	@Override
	public List<String> getGugunName() throws Exception {
		return chartMapper.getGugunName();
	}

	@Override
	public List<Integer> getGugunCnt() throws Exception {
		return chartMapper.getGugunCnt();
	}

	@Override
	public List<String> getContentName() throws Exception {
		return chartMapper.getContentName();
	}

	@Override
	public List<Integer> getContentCnt() throws Exception {
		return chartMapper.getContentCnt();
	}

}
