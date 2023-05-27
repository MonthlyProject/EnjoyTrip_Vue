package com.ssafy.trip.chart.model.service;

import java.util.List;

public interface ChartService {
	List<String> getSidoName() throws Exception;
	List<Integer> getSidoCnt() throws Exception;
	List<String> getGugunName() throws Exception;
	List<Integer> getGugunCnt() throws Exception;
	List<String> getContentName() throws Exception;
	List<Integer> getContentCnt() throws Exception;
}
