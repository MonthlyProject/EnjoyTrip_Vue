package com.ssafy.trip.admin.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ssafy.trip.board.model.BoardDto;
import com.ssafy.trip.user.model.UserDto;
import com.ssafy.trip.user.model.service.UserService;

@Controller
@RequestMapping("/vadmin")
public class AdminVueController {

	private static final Logger logger = LoggerFactory.getLogger(AdminVueController.class);

	private UserService userService;

	public AdminVueController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> list2(Map<String, String> map) throws Exception {
		return new ResponseEntity<List<UserDto>>(userService.listUser(map), HttpStatus.OK);
	}

	@PostMapping("/delete/{id}")
	public ResponseEntity<?> userDelete(@PathVariable("id") String userId) throws Exception {
		System.out.println(userId);
		logger.debug("userDelete userid : {}", userId);
		userService.deleteUser(userId);
		List<UserDto> list = userService.listUser(null);
		return new ResponseEntity<List<UserDto>>(list, HttpStatus.OK);
	}
	
	@GetMapping("/{key}/{word}")
	public ResponseEntity<List<UserDto>> search(@PathVariable("key") String key, @PathVariable("word") String word, Map<String, String> map) throws Exception {
//		logger.info("map 출력", map);
		map.put("key", key);
		map.put("word", word);
		return new ResponseEntity<List<UserDto>>(userService.searchUser(map), HttpStatus.OK);
	}

	private ResponseEntity<String> exceptionHandling(Exception e) {
		e.printStackTrace();
		return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
