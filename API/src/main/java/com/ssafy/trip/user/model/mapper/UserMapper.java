package com.ssafy.trip.user.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.trip.board.model.FileInfoDto;
import com.ssafy.trip.user.model.UserDto;

@Mapper
public interface UserMapper {
	int idCheck(String userId) throws SQLException;
	void signUp(UserDto user) throws SQLException;
	UserDto signIn(Map<String, String> map) throws SQLException;
	void setUserImage(Map<String, Object> map) throws Exception;
	void deleteUserImage(String userId) throws Exception;
	UserDto getUser(String userId) throws SQLException;
	FileInfoDto getImage(String userId) throws SQLException;
	String findPwd(String userid, String emailid, String emaildomain) throws Exception;

	/* Admin */
	List<UserDto> listUser(Map<String, String> map) throws SQLException;
	List<UserDto> searchUser(Map<String, String> map) throws SQLException;
	void modify(UserDto memberDto) throws SQLException;
	void deleteUser(String userId) throws SQLException;

	int getTotalArticleCount(Map<String, Object> param) throws SQLException;
	
	/* jwt */
	public void saveRefreshToken(Map<String, String> map) throws SQLException;
	public Object getRefreshToken(String userid) throws SQLException;
	public void deleteRefreshToken(Map<String, String> map) throws SQLException;
	public UserDto login(UserDto memberDto) throws SQLException;
}
