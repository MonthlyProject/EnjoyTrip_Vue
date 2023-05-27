package com.ssafy.trip.board.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.board.model.CommentDto;

@Mapper
public interface CommentMapper {

	void writeComment(CommentDto commentDto) throws SQLException;
	
	List<CommentDto> listComment(int articleNo) throws SQLException;
	
	int getTotalCommentCount(Map<String, Object> param) throws SQLException;
	
	CommentDto getComment(int commentNo) throws SQLException;
	
	void modifyComment(CommentDto commentDto) throws SQLException;
	
	void deleteComment(int commentNo) throws SQLException;
	
}
