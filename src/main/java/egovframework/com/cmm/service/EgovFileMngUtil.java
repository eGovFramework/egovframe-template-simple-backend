package egovframework.com.cmm.service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;

import jakarta.annotation.Resource;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Class Name  : EgovFileMngUtil.java
 * @Description : 메시지 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.02.13       이삼섭                  최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *   2026.05.13  PHJ            보안취약점 대응
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */
@Slf4j
@Component("EgovFileMngUtil")
public class EgovFileMngUtil {

    public static final int BUFF_SIZE = 2048;

    private static final long MAX_FILE_SIZE = 10L * 1024 * 1024; // 10MB
    
    @Value("${Globals.fileUpload.Extensions:.gif.jpg.jpeg.png.xls.xlsx}")
    private String allowedExtensionsRaw;

    private Set<String> allowedExtensions() {
        return Arrays.stream(allowedExtensionsRaw.split("\\."))
                     .filter(s -> !s.isEmpty())
                     .map(String::toLowerCase)
                     .collect(Collectors.toSet());
    }

    @Resource(name = "propertiesService")
    protected EgovPropertyService propertyService;

    @Resource(name = "egovFileIdGnrService")
    private EgovIdGnrService idgenService;

    /**
     * 첨부파일에 대한 목록 정보를 취득한다.
     *
     * @param files
     * @return
     * @throws Exception
     */
    public List<FileVO> parseFileInf(Map<String, MultipartFile> files, String KeyStr, int fileKeyParam, String atchFileId, String storePath) throws Exception {
	int fileKey = fileKeyParam;

	String storePathString = "";
	String atchFileIdString = "";

	if ("".equals(storePath) || storePath == null) {
	    storePathString = propertyService.getString("Globals.fileStorePath");
	} else {
	    storePathString = propertyService.getString(storePath);
	}
	
	atchFileId = atchFileId.replaceAll("\\s", "");

	if ("".equals(atchFileId) || atchFileId == null) {
	    atchFileIdString = idgenService.getNextStringId();
	} else {
	    atchFileIdString = atchFileId;
	}

	File saveFolder = new File(EgovWebUtil.filePathBlackList(storePathString));

	if (!saveFolder.exists() || saveFolder.isFile()) {
	    saveFolder.mkdirs();
	}

	Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
	MultipartFile file;
	String filePath = "";
	List<FileVO> result  = new ArrayList<FileVO>();
	FileVO fvo;

	while (itr.hasNext()) {
	    Entry<String, MultipartFile> entry = itr.next();

	    file = entry.getValue();
	    String orginFileName = file.getOriginalFilename();
	    
	    //--------------------------------------
	    // 원 파일명이 null인 경우 처리
	    //--------------------------------------
	    if (orginFileName == null) {
	    	orginFileName = "";
	    }
	    ////------------------------------------

	    //--------------------------------------
	    // 원 파일명이 없는 경우 처리
	    // (첨부가 되지 않은 input file type)
	    //--------------------------------------
	    if ("".equals(orginFileName)) {
		continue;
	    }
	    ////------------------------------------
	    
		int index = orginFileName.lastIndexOf(".");
	    if (index < 0) {
	        throw new EgovBizException("확장자가 없는 파일은 업로드할 수 없습니다.");
	    }
	    String fileExt = orginFileName.substring(index + 1).toLowerCase();

	    // 확장자 화이트리스트 검증
	    if (!allowedExtensions().contains(fileExt)) {
	        throw new EgovBizException("허용되지 않는 파일 확장자입니다: " + fileExt);
	    }

	    // 파일 크기 검증
	    long _size = file.getSize();
	    if (_size > MAX_FILE_SIZE) {
	        throw new EgovBizException("파일 크기가 허용 한도(10MB)를 초과했습니다.");
	    }

	    // 파일명 정규화 (경로 탈출·제어문자 제거)
	    String safeOriginFileName = orginFileName.replaceAll("[\\p{Cntrl}\\\\/:*?\"<>|]", "_");

	    String newName = KeyStr + EgovStringUtil.getTimeStamp() + fileKey;

	    if (!"".equals(orginFileName)) {
	    String osName = System.getProperty("os.name").toLowerCase();
	    	
		filePath = storePathString + File.separator + newName;
		Path winFilePath = Paths.get(filePath).toAbsolutePath();
		
		if (osName.contains("win")) {
			file.transferTo(new File(EgovWebUtil.filePathBlackList(winFilePath.toString())));
		} else {
			file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
		}
		
	    }
	    fvo = new FileVO();
	    fvo.setFileExtsn(fileExt);
	    fvo.setFileStreCours(storePathString);
	    fvo.setFileMg(Long.toString(_size));
	    fvo.setOrignlFileNm(safeOriginFileName);
	    fvo.setStreFileNm(newName);
	    fvo.setAtchFileId(atchFileIdString);
	    fvo.setFileSn(String.valueOf(fileKey));

	    //writeFile(file, newName, storePathString);
	    result.add(fvo);

	    fileKey++;
	}

	return result;
    }

}
