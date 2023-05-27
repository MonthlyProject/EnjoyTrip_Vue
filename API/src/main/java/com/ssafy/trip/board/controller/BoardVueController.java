package com.ssafy.trip.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import com.ssafy.trip.board.model.BoardDto;
import com.ssafy.trip.board.model.FileInfoDto;
import com.ssafy.trip.board.model.service.BoardService;

@Controller
@CrossOrigin("*")
@RequestMapping("/board")
public class BoardVueController extends AbstractView {
	private final Logger logger = LoggerFactory.getLogger(BoardVueController.class);

	@Value("${file.path}")
	private String uploadPath;
	
	@Value("${file.imgPath}")
	private String uploadImgPath;
	
	private BoardService boardService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	public BoardVueController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	@PostMapping("/write")
	public ResponseEntity<String> writeArticle(@RequestBody BoardDto boardDto) throws Exception {
		logger.info("writeArticle - 호출");
		boardService.writeArticle(boardDto);
			return new ResponseEntity<String>("success", HttpStatus.OK);
	}


	@PostMapping(consumes = {org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> write(@RequestParam("files") List<MultipartFile> imgs, BoardDto boardDto) throws Exception {
		logger.debug("write boardDto : {}", boardDto);
//		FileUpload 관련 설정.
		logger.debug("MultipartFile.isEmpty : {}", imgs.isEmpty());
		if (!imgs.isEmpty()) {
			String today = new SimpleDateFormat("yyMMdd").format(new Date());
			uploadPath = servletContext.getRealPath("/upload");
			String saveFolder = uploadPath + File.separator + today;
			logger.debug("저장 폴더 : {}", saveFolder);
			File folder = new File(saveFolder);
			if (!folder.exists())
				folder.mkdirs();
			List<FileInfoDto> fileInfos = new ArrayList<FileInfoDto>();
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
				fileInfos.add(fileInfoDto);
			}
			boardDto.setFileInfos(fileInfos);
		}
		
		System.out.println(boardDto);
		boardService.writeArticle(boardDto);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<BoardDto>> list(Map<String, String> map) throws Exception {
//		logger.info("map 출력", map);
		return new ResponseEntity<List<BoardDto>>(boardService.listArticle(map), HttpStatus.OK);
	}
	
	@GetMapping("/inform/list")
	public ResponseEntity<List<BoardDto>> informlist(Map<String, String> map) throws Exception {
//		logger.info("map 출력", map);
		return new ResponseEntity<List<BoardDto>>(boardService.informlistArticle(map), HttpStatus.OK);
	}
	
	@GetMapping("/list/{key}/{word}")
	public ResponseEntity<List<BoardDto>> search(@PathVariable("key") String key, @PathVariable("word") String word, Map<String, String> map) throws Exception {
//		logger.info("map 출력", map);
		map.put("key", key);
		map.put("word", word);
		return new ResponseEntity<List<BoardDto>>(boardService.listArticle(map), HttpStatus.OK);
	}
	
	@GetMapping("/inform/list/{key}/{word}")
	public ResponseEntity<List<BoardDto>> informsearch(@PathVariable("key") String key, @PathVariable("word") String word, Map<String, String> map) throws Exception {
//		logger.info("map 출력", map);
		map.put("key", key);
		map.put("word", word);
		return new ResponseEntity<List<BoardDto>>(boardService.informlistArticle(map), HttpStatus.OK);
	}
	
	@GetMapping("/{articleno}")
	public ResponseEntity<BoardDto> getArticle(@PathVariable("articleno") int articleno) throws Exception {
		logger.info("getArticle - 호출 : " + articleno);
		boardService.updateHit(articleno);
		return new ResponseEntity<BoardDto>(boardService.getArticle(articleno), HttpStatus.OK);
	}
	
	@GetMapping("/showImage/{articleno}")
    public ResponseEntity<?> showImage(@PathVariable("articleno") int articleno) throws Exception {
		System.out.println(articleno);
		FileInfoDto fileDto = boardService.getUserImg(articleno);
		if(fileDto==null) {
			System.out.println("noImage???");
            return new ResponseEntity<String>("noImage", HttpStatus.OK); // 리턴 결과 반환 404
		}
		System.out.println("noImage!!!");
        String os = System.getProperty("os.name").toLowerCase();
		ServletContext ctx = getServletContext();
		String realPath = ctx.getRealPath("/upload");

//        imageRoot = imageRoot + String.valueOf(map.get("ofile"));
//        imageRoot = new File("").getAbsolutePath() + "/resources/img/"+saveFolder+"/"+saveFile;
		String saveFolder = realPath + File.separator + fileDto.getSaveFolder() + "\\" + fileDto.getSaveFile();
		System.out.println(saveFolder);
		System.out.println("?????????????????");

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
	
	@PutMapping
	public ResponseEntity<String> modifyArticle(@RequestBody BoardDto boardDto) throws Exception {
		logger.info("modifyArticle - 호출 {}", boardDto);
		
		boardService.modifyArticle(boardDto);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@DeleteMapping("/{articleno}")
	public ResponseEntity<String> deleteArticle(@PathVariable("articleno") int articleno) throws Exception {
		logger.info("deleteArticle - 호출");
		boardService.deleteArticle(articleno, uploadPath);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

	@PostMapping("/download")
//	public ModelAndView downloadFile(@RequestBody Map<String, Object> map) {
	public ResponseEntity<FileSystemResource> downloadFile(@RequestBody Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("이거 돼?????");

		ServletContext ctx = getServletContext();
		String realPath = ctx.getRealPath("/upload");
		
//		Map<String, Object> fileInfo = (Map<String, Object>) model.get("downloadFile"); // 전송받은 모델(파일 정보)
        
        String saveFolder = (String) map.get("sfolder");	// 파일 경로
        String originalFile = (String) map.get("ofile");	// 원본 파일명(화면에 표시될 파일 이름)
        String saveFile = (String) map.get("sfile");    	// 암호화된 파일명(실제 저장된 파일 이름)
        File file = new File(realPath + File.separator  + saveFolder, saveFile);
        FileSystemResource resource = new FileSystemResource(file);
        if(resource.exists() == false) {
        	return new ResponseEntity<FileSystemResource>(HttpStatus.NOT_FOUND);
        }

		org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
		try {
			headers.add("Content-Disposition", "attachment; filename="+originalFile);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<FileSystemResource>(resource, headers, HttpStatus.OK);
		
//		headers.add("content-disposition",  "inline;filename="+map.get("ofile"));
//		renderMergedOutputModel(map, request, response);
//		return new ModelAndView("fileDownLoadView", "downloadFile", map);
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		ServletContext ctx = getServletContext();
		String realPath = ctx.getRealPath("/upload");
		
//		Map<String, Object> fileInfo = (Map<String, Object>) model.get("downloadFile"); // 전송받은 모델(파일 정보)
		Map<String, Object> fileInfo = model; // 전송받은 모델(파일 정보)
        
        String saveFolder = (String) fileInfo.get("sfolder");	// 파일 경로
        String originalFile = (String) fileInfo.get("ofile");	// 원본 파일명(화면에 표시될 파일 이름)
        String saveFile = (String) fileInfo.get("sfile");    	// 암호화된 파일명(실제 저장된 파일 이름)
        File file = new File(realPath + File.separator  + saveFolder, saveFile);
		
        response.setContentType(getContentType());
        response.setContentLength((int) file.length());
        
        String header = request.getHeader("User-Agent");
        boolean isIE = header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1;
        String fileName = null;
        // IE는 다르게 처리
        if (isIE) {
        	fileName = URLEncoder.encode(originalFile, "UTF-8").replaceAll("\\+", "%20");
        } else {
            fileName = new String(originalFile.getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try { 
                    fis.close(); 
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        out.flush();
	}
}
