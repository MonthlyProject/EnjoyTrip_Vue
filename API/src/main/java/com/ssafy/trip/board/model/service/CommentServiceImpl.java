package com.ssafy.trip.board.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.trip.board.model.CommentDto;
import com.ssafy.trip.board.model.mapper.CommentMapper;
import com.ssafy.util.PageNavigation;
import com.ssafy.util.SizeConstant;

@Service
public class CommentServiceImpl implements CommentService {
	
	private CommentMapper commentMapper;
	
	@Autowired
	public CommentServiceImpl(CommentMapper commentMapper) {
		this.commentMapper = commentMapper;
	}
	
	@Override
	@Transactional
	public void writeComment(CommentDto commentDto) throws Exception {
		System.out.println("댓글 입력 전 dto : " + commentDto);
		commentMapper.writeComment(commentDto);
		System.out.println("댓글 입력 후 dto : " + commentDto);
	}

	@Override
	public List<CommentDto> listComment(int articleNo) throws Exception {
//		Map<String, Object> param = new HashMap<String, Object>();
//		int pgNo = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
//		int start = pgNo * SizeConstant.LIST_SIZE - SizeConstant.LIST_SIZE;
//		param.put("start", start);
//		param.put("listsize", SizeConstant.LIST_SIZE);
		
		return commentMapper.listComment(articleNo);
	}
	
	@Override
	public CommentDto getComment(int commentNo) throws Exception {
		return commentMapper.getComment(commentNo);
	}

	@Override
	public PageNavigation makePageNavigation(Map<String, String> map) throws Exception {
		PageNavigation pageNavigation = new PageNavigation();

		int naviSize = SizeConstant.NAVIGATION_SIZE;
		int sizePerPage = SizeConstant.LIST_SIZE;
		int currentPage = Integer.parseInt(map.get("pgno"));

		pageNavigation.setCurrentPage(currentPage);
		pageNavigation.setNaviSize(naviSize);
		Map<String, Object> param = new HashMap<String, Object>();
		String key = map.get("key");
		if ("userid".equals(key))
			key = "user_id";
		param.put("key", key == null ? "" : key);
		param.put("word", map.get("word") == null ? "" : map.get("word"));
		int totalCount = commentMapper.getTotalCommentCount(param);
		pageNavigation.setTotalCount(totalCount);
		int totalPageCount = (totalCount - 1) / sizePerPage + 1;
		pageNavigation.setTotalPageCount(totalPageCount);
		boolean startRange = currentPage <= naviSize;
		pageNavigation.setStartRange(startRange);
		boolean endRange = (totalPageCount - 1) / naviSize * naviSize < currentPage;
		pageNavigation.setEndRange(endRange);
		pageNavigation.makeNavigator();

		return pageNavigation;
	}

	@Override
	public void modifyComment(CommentDto commentDto) throws Exception {
		commentMapper.modifyComment(commentDto);

	}

	@Override
	@Transactional
	public void deleteComment(int commentNo) throws Exception {
		commentMapper.deleteComment(commentNo);

	}

	

}
