package com.ssafy.trip.user.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.trip.board.model.FileInfoDto;
import com.ssafy.trip.user.model.UserDto;
import com.ssafy.util.PageNavigation;

public interface UserService {
	int idCheck(String userId) throws SQLException;
	void signUp(UserDto user) throws Exception;
	public UserDto signIn(Map<String, String> map) throws Exception;
	void setUserImage(String userId, FileInfoDto fileInfo) throws Exception;
	void deleteUserImage(String userId) throws Exception;
	UserDto getUser(String userId) throws SQLException;
	FileInfoDto getImage(String userId) throws SQLException;
	String findPwd(String userid, String emailid, String emaildomain) throws Exception;

	/* Admin */
	List<UserDto> listUser(Map<String, String> map) throws SQLException;
	List<UserDto> searchUser(Map<String, String> map) throws SQLException;
	void modify(UserDto memberDto) throws SQLException;
	void deleteUser(String userId) throws SQLException;
	
	PageNavigation makePageNavigation(Map<String, String> map) throws Exception;
	
	/* jwt */

	public void saveRefreshToken(String userid, String refreshToken) throws Exception;
	public Object getRefreshToken(String userid) throws Exception;
	public void deleRefreshToken(String userid) throws Exception;
	public UserDto login(UserDto memberDto) throws Exception;
}
