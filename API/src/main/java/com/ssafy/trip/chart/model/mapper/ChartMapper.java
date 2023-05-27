package com.ssafy.trip.chart.model.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChartMapper {
	List<String> getSidoName() throws Exception;
	List<Integer> getSidoCnt() throws Exception;
	List<String> getGugunName() throws Exception;
	List<Integer> getGugunCnt() throws Exception;
	List<String> getContentName() throws Exception;
	List<Integer> getContentCnt() throws Exception;
}
