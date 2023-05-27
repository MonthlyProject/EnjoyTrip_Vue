package com.ssafy.trip.board.controller;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.trip.board.model.CommentDto;
import com.ssafy.trip.board.model.service.CommentService;

@Controller
@CrossOrigin("*")
@RequestMapping("/comment")
public class CommentController {
	private final Logger logger = LoggerFactory.getLogger(CommentController.class);
	
	private CommentService commentService;
	
	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@PostMapping
	public ResponseEntity<String> write(@RequestBody CommentDto commentDto) throws Exception {
		logger.debug("write commentDto : {}", commentDto);
		System.out.println(commentDto);
		commentService.writeComment(commentDto);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@GetMapping("/list/{articleNo}")
	public ResponseEntity<List<CommentDto>> list(@PathVariable("articleNo") int articleNo) throws Exception {
		return new ResponseEntity<List<CommentDto>>(commentService.listComment(articleNo), HttpStatus.OK);
	}

	
	@PutMapping
	public ResponseEntity<String> modifyComment(@RequestBody CommentDto commentDto) throws Exception {
		logger.info("modifyComment - 호출 {}", commentDto);
		
		commentService.modifyComment(commentDto);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{commentNo}")
	public ResponseEntity<String> deleteComment(@PathVariable("commentNo") int commentNo) throws Exception {
		logger.info("deleteComment - 호출");
		commentService.deleteComment(commentNo);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
}
