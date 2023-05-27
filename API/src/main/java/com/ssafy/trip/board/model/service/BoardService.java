package com.ssafy.trip.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.trip.board.model.BoardDto;
import com.ssafy.trip.board.model.FileInfoDto;
import com.ssafy.trip.map.model.MarkerDto;
import com.ssafy.util.PageNavigation;

public interface BoardService {

	void writeArticle(BoardDto boardDto) throws Exception;
	List<BoardDto> listArticle(Map<String, String> map) throws Exception;
	List<BoardDto> informlistArticle(Map<String, String> map) throws Exception;
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	BoardDto getArticle(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	FileInfoDto getUserImg(int articleNo) throws Exception;
	
	void modifyArticle(BoardDto boardDto) throws Exception;
	void deleteArticle(int articleNo, String path) throws Exception;
	
}
