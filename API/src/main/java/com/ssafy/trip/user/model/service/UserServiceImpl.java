package com.ssafy.trip.user.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.trip.board.model.FileInfoDto;
import com.ssafy.trip.user.model.UserDto;
import com.ssafy.trip.user.model.mapper.UserMapper;
import com.ssafy.util.PageNavigation;
import com.ssafy.util.SizeConstant;

@Service
public class UserServiceImpl implements UserService {

	private UserMapper userMapper;
	
	@Autowired
	private SqlSession sqlSession;
	
	public UserServiceImpl(UserMapper userMapper) {
		super();
		this.userMapper = userMapper;
	}
	
	@Override
	public int idCheck(String userId) throws SQLException {
		return userMapper.idCheck(userId);
	}
	
	@Override
	public void signUp(UserDto user) throws Exception {
		userMapper.signUp(user);
	}

	@Override
	public UserDto signIn(Map<String, String> map) throws Exception {
		return userMapper.signIn(map);
	}

	@Override
	public List<UserDto> listUser(Map<String, String> map) throws SQLException {
		return userMapper.listUser(map);
	}

	@Override
	public UserDto getUser(String userId) throws SQLException {
		return userMapper.getUser(userId);
	}

	@Override
	public void modify(UserDto memberDto) throws SQLException {
		userMapper.modify(memberDto);
	}

	@Override
	public void deleteUser(String userId) throws SQLException {
		userMapper.deleteUser(userId);
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
		int totalCount = userMapper.getTotalArticleCount(param);
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
	public List<UserDto> searchUser(Map<String, String> map) throws SQLException {
		return userMapper.searchUser(map);
	}


	@Override
	public void saveRefreshToken(String userid, String refreshToken) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", refreshToken);
		sqlSession.getMapper(UserMapper.class).saveRefreshToken(map);
	}

	@Override
	public Object getRefreshToken(String userid) throws Exception {
		return sqlSession.getMapper(UserMapper.class).getRefreshToken(userid);
	}

	@Override
	public void deleRefreshToken(String userid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		map.put("token", null);
		sqlSession.getMapper(UserMapper.class).deleteRefreshToken(map);
	}

	@Override
	public UserDto login(UserDto memberDto) throws Exception {
		if (memberDto.getUserId() == null || memberDto.getUserPwd() == null)
			return null;
		return sqlSession.getMapper(UserMapper.class).login(memberDto);
	}

	@Override
	public void setUserImage(String userId, FileInfoDto fileInfo) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("fileInfo", fileInfo);
		sqlSession.getMapper(UserMapper.class).setUserImage(map);
		
	}

	@Override
	public FileInfoDto getImage(String userId) throws SQLException {
		return userMapper.getImage(userId);
	}

	@Override
	public void deleteUserImage(String userId) throws Exception {
		// TODO Auto-generated method stub
		userMapper.deleteUserImage(userId);
	}

	@Override
	public String findPwd(String userid, String emailid, String emaildomain) throws Exception {
		return userMapper.findPwd(userid, emailid, emaildomain);
	}
}
