package com.ssafy.trip.board.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.trip.board.model.CommentDto;
import com.ssafy.util.PageNavigation;

public interface CommentService {	
	void writeComment(CommentDto commentDto) throws Exception;
	List<CommentDto> listComment(int articleNo) throws Exception;
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	CommentDto getComment(int commentNo) throws Exception;
	
	void modifyComment(CommentDto commentDto) throws Exception;
	void deleteComment(int commentNo) throws Exception;
	
}
