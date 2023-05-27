package com.ssafy.trip.user.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.AbstractView;

import com.ssafy.trip.board.model.FileInfoDto;
import com.ssafy.trip.jwt.service.JwtServiceImpl;
import com.ssafy.trip.user.model.UserDto;
import com.ssafy.trip.user.model.service.UserService;

import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/vuser")
@CrossOrigin("*")
public class UserVueController extends AbstractView {

	public static final Logger logger = LoggerFactory.getLogger(UserController.class);
	private static final String SUCCESS = "success";
	private static final String FAIL = "fail";

	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.imgPath}")
	private String uploadImgPath;
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private JwtServiceImpl jwtService;

	@Autowired
	private UserService userService;

	public UserVueController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@GetMapping("/{userid}")
	public ResponseEntity<Map<String, Object>> getInfo(
			@PathVariable("userid") @ApiParam(value = "인증할 회원의 아이디.", required = true) String userid,
			HttpServletRequest request) {
//		logger.debug("userid : {} ", userid);
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.UNAUTHORIZED;
//		if (jwtService.checkToken(request.getHeader("access-token"))) {
			logger.info("사용 가능한 토큰!!!");
			try {
//				로그인 사용자 정보.
				UserDto userDto = userService.getUser(userid);
				FileInfoDto fileDto = userService.getImage(userid);
				resultMap.put("userInfo", userDto);
				resultMap.put("fileInfo", fileDto);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} catch (Exception e) {
				logger.error("정보조회 실패 : {}", e);
				resultMap.put("message", e.getMessage());
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
//		} else {
//			logger.error("사용 불가능 토큰!!!");
//			resultMap.put("message", FAIL);
//			status = HttpStatus.UNAUTHORIZED;
//		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@GetMapping("/{userid}/{emailid}/{emaildomain}")
	public ResponseEntity<String> getpwd(@PathVariable("userid") String userid, @PathVariable("emailid") String emailid,
			@PathVariable("emaildomain") String emaildomain) throws Exception {
		String pwd = userService.findPwd(userid, emailid, emaildomain);
		return new ResponseEntity<String>(pwd, HttpStatus.OK);
	}
	
	@PostMapping("/join")
	public ResponseEntity<Map<String, Object>> join(@RequestBody UserDto userDto) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			userService.signUp(userDto);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		}catch(Exception e) {
			logger.error("회원가입 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/idCheck")
	public ResponseEntity<Map<String, Object>> idCheck(@RequestBody String userId) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		try {
			int result = userService.idCheck(userId);
			if(result == 0) {
				resultMap.put("message", SUCCESS);
			} else {
				resultMap.put("message", FAIL);
			}
			status = HttpStatus.ACCEPTED;
		} catch(Exception e) {
			logger.error("아이디 체크 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
//	@PostMapping("/login")
//	public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> map) {
//		Map<String, Object> resultMap = new HashMap<>();
//		HttpStatus status = null;
//		try {
//			UserDto loginUser = userService.signIn(map);
//			if (loginUser != null) {
//				String accessToken = jwtService.createAccessToken("userid", loginUser.getUserId());// key, data
//				String refreshToken = jwtService.createRefreshToken("userid", loginUser.getUserId());// key, data
//				userService.saveRefreshToken(loginUser.getUserId(), refreshToken);
//				logger.debug("로그인 accessToken 정보 : {}", accessToken);
//				logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
//				resultMap.put("access-token", accessToken);
//				resultMap.put("refresh-token", refreshToken);
//				resultMap.put("message", SUCCESS);
//				status = HttpStatus.ACCEPTED;
//			} else {
//				resultMap.put("message", FAIL);
//				status = HttpStatus.ACCEPTED;
//			}
//		} catch (Exception e) {
//			logger.error("로그인 실패 : {}", e);
//			resultMap.put("message", e.getMessage());
//			status = HttpStatus.INTERNAL_SERVER_ERROR;
//		}
//		return new ResponseEntity<Map<String, Object>>(resultMap, status);
//	}
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(
			@RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) UserDto memberDto) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = null;
		System.out.println(memberDto.toString());
		try {
			UserDto loginUser = userService.login(memberDto);
			if (loginUser != null) {
				String accessToken = jwtService.createAccessToken("userid", loginUser.getUserId());// key, data
				String refreshToken = jwtService.createRefreshToken("userid", loginUser.getUserId());// key, data
				userService.saveRefreshToken(memberDto.getUserId(), refreshToken);
				logger.debug("로그인 accessToken 정보 : {}", accessToken);
				logger.debug("로그인 refreshToken 정보 : {}", refreshToken);
				resultMap.put("access-token", accessToken);
				resultMap.put("refresh-token", refreshToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			} else {
				resultMap.put("message", FAIL);
				status = HttpStatus.ACCEPTED;
			}
		} catch (Exception e) {
			logger.error("로그인 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}

	@GetMapping("/logout/{userid}")
	public ResponseEntity<?> removeToken(@PathVariable("userid") String userid) {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		System.out.println(1111);
		try {
			userService.deleRefreshToken(userid);
			resultMap.put("message", SUCCESS);
			status = HttpStatus.ACCEPTED;
		} catch (Exception e) {
			logger.error("로그아웃 실패 : {}", e);
			resultMap.put("message", e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		System.out.println(status);
		return new ResponseEntity<Map<String, Object>>(resultMap, status);

	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(@RequestBody UserDto memberDto, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		HttpStatus status = HttpStatus.ACCEPTED;
		String token = request.getHeader("refresh-token");
		logger.debug("token : {}, memberDto : {}", token, memberDto);
		if (jwtService.checkToken(token)) {
			if (token.equals(userService.getRefreshToken(memberDto.getUserId()))) {
				String accessToken = jwtService.createAccessToken("userid", memberDto.getUserId());
				logger.debug("token : {}", accessToken);
				logger.debug("정상적으로 액세스토큰 재발급!!!");
				resultMap.put("access-token", accessToken);
				resultMap.put("message", SUCCESS);
				status = HttpStatus.ACCEPTED;
			}
		} else {
			logger.debug("리프레쉬토큰도 사용불!!!!!!!");
			status = HttpStatus.UNAUTHORIZED;
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, status);
	}
	
	@PostMapping("/modify")
	public ResponseEntity<?> modify(@RequestBody UserDto userDto) throws Exception {
		userService.modify(userDto);
		return new ResponseEntity<String>(SUCCESS, HttpStatus.OK);
	}
	
	@PostMapping(value="/image", consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> setUserImage(@RequestParam("files") List<MultipartFile> imgs, String userId) throws Exception {
//		FileUpload 관련 설정.
		logger.debug("MultipartFile.isEmpty : {}", imgs.isEmpty());
//		List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
		FileInfoDto fileInfo = new FileInfoDto();
		if (!imgs.isEmpty()) {
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			uploadPath = servletContext.getRealPath("/upload");
			String saveFolder = uploadPath + File.separator + today;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			for (MultipartFile mfile : imgs) {
				FileInfoDto fileInfoDto = new FileInfoDto();
				String originalFileName = mfile.getOriginalFilename();
				if (!originalFileName.isEmpty()) {
					String saveFileName = UUID.randomUUID().toString()
							+ originalFileName.substring(originalFileName.lastIndexOf('.'));
					fileInfoDto.setSaveFolder(today);
					fileInfoDto.setOriginalFile(originalFileName);
					fileInfoDto.setSaveFile(saveFileName);
					logger.debug("원본 파일 이름 : {}, 실제 저장 파일 이름 : {}", mfile.getOriginalFilename(), saveFileName);
					mfile.transferTo(new File(folder, saveFileName));
				}
				fileInfo = fileInfoDto;
			}
		}
		userService.setUserImage(userId, fileInfo);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<String> deleteUserImage(@PathVariable("userId") String userId) throws Exception {
		userService.deleteUserImage(userId);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	
//	@PostMapping("/showImage")
//	public ResponseEntity<FileSystemResource> showImage(@RequestBody Map<String, Object> map) {
//		System.out.println(map);
//		ServletContext ctx = getServletContext();
//		String realPath = ctx.getRealPath("/upload");
//		
////		Map<String, Object> fileInfo = (Map<String, Object>) model.get("downloadFile"); // 전송받은 모델(파일 정보)
//        
//        String saveFolder = (String) map.get("sfolder");	// 파일 경로
//        String originalFile = (String) map.get("ofile");	// 원본 파일명(화면에 표시될 파일 이름)
//        String saveFile = (String) map.get("sfile");    	// 암호화된 파일명(실제 저장된 파일 이름)
//        File file = new File(realPath + File.separator  + saveFolder, saveFile);
//        FileSystemResource resource = new FileSystemResource(file);
//        if(resource.exists() == false) {
//        	return new ResponseEntity<FileSystemResource>(HttpStatus.NOT_FOUND);
//        }
//
//		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
//		try {
//			headers.add("Content-Disposition", "attachment; filename="+originalFile);
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return new ResponseEntity<FileSystemResource>(resource, headers, HttpStatus.OK);
//    }
	@PostMapping("/showImage")
    public ResponseEntity<Resource> showImage(@RequestBody Map<String, String> map) {
        String os = System.getProperty("os.name").toLowerCase();
		ServletContext ctx = getServletContext();
		String realPath = ctx.getRealPath("/upload");
		System.out.println(map.get("sfolder"));

//        imageRoot = imageRoot + String.valueOf(map.get("ofile"));
//        imageRoot = new File("").getAbsolutePath() + "/resources/img/"+saveFolder+"/"+saveFile;
		String saveFolder = realPath + File.separator + map.get("sfolder") + "\\" + map.get("sfile");
		System.out.println(saveFolder);

        // Resorce를 사용해서 로컬 서버에 저장된 이미지 경로 및 파일 명을 지정
        Resource resource = new FileSystemResource(saveFolder);

        // 로컬 서버에 저장된 이미지 파일이 없을 경우
        if(!resource.exists()){
            return new ResponseEntity<Resource>(HttpStatus.NOT_FOUND); // 리턴 결과 반환 404
        }

        // 로컬 서버에 저장된 이미지가 있는 경우 로직 처리
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(saveFolder);
            // 인풋으로 들어온 파일명 .png / .jpg 에 맞게 헤더 타입 설정
            header.add("Content-Type", Files.probeContentType(filePath));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(resource);
        System.out.println(header);
        // 이미지 리턴 실시 [브라우저에서 get 주소 확인 가능]
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

