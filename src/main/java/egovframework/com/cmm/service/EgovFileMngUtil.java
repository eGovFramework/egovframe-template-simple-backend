package egovframework.com.cmm.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
//import java.util.HashMap;

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
	    //String fileName = orginFileName.substring(0, index);
	    String fileExt = orginFileName.substring(index + 1);
	    String newName = KeyStr + EgovStringUtil.getTimeStamp() + fileKey;
	    long _size = file.getSize();

	    if (!"".equals(orginFileName)) {
		filePath = storePathString + File.separator + newName;
		file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
	    }
	    fvo = new FileVO();
	    fvo.setFileExtsn(fileExt);
	    fvo.setFileStreCours(storePathString);
	    fvo.setFileMg(Long.toString(_size));
	    fvo.setOrignlFileNm(orginFileName);
	    fvo.setStreFileNm(newName);
	    fvo.setAtchFileId(atchFileIdString);
	    fvo.setFileSn(String.valueOf(fileKey));

	    //writeFile(file, newName, storePathString);
	    result.add(fvo);

	    fileKey++;
	}

	return result;
    }

    /**
     * 첨부파일을 서버에 저장한다.
     *
     * @param file
     * @param newName
     * @param stordFilePath
     * @throws Exception
     */
    protected void writeUploadedFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
	InputStream stream = null;
	OutputStream bos = null;
	String stordFilePathReal = EgovWebUtil.filePathBlackList(stordFilePath);
	newName = EgovWebUtil.filePathBlackList(newName);
	try {
	    stream = file.getInputStream();
	    File cFile = new File(stordFilePathReal);

	    if (!cFile.isDirectory()) {
		boolean _flag = cFile.mkdir();
		if (!_flag) {
		    throw new IOException("Directory creation Failed ");
		}
	    }

	    bos = new FileOutputStream(stordFilePathReal + File.separator + newName);

	    int bytesRead = 0;
	    byte[] buffer = new byte[BUFF_SIZE];

	    while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
		bos.write(buffer, 0, bytesRead);
	    }
	} catch (FileNotFoundException fnfe) {
		log.debug("fnfe: {}", fnfe);
	} catch (IOException ioe) {
		log.debug("ioe: {}", ioe);
	} finally {
	    if (bos != null) {
		try {
		    bos.close();
		} catch (IOException ignore) {
			log.debug("IGNORED: {}", ignore.getMessage());
		}
	    }
	    if (stream != null) {
		try {
		    stream.close();
		} catch (IOException ignore) {
			log.debug("IGNORED: {}", ignore.getMessage());
		}
	    }
	}
    }

    /**
     * 서버의 파일을 다운로드한다.
     *
     * @param request
     * @param response
     * @throws Exception
     */
    public static void downFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

    String downFileName = EgovWebUtil.filePathBlackList(EgovStringUtil.isNullToString(request.getAttribute("downFile")));
    String orgFileName = EgovWebUtil.filePathBlackList(EgovStringUtil.isNullToString(request.getAttribute("orgFileName")));

	/*if ((String)request.getAttribute("downFile") == null) {
	    downFileName = "";
	} else {
	    downFileName = EgovStringUtil.isNullToString(request.getAttribute("downFile"));
	}*/

	/*if ((String)request.getAttribute("orgFileName") == null) {
	    orgFileName = "";
	} else {
	    orgFileName = (String)request.getAttribute("orginFile");
	}*/

	File file = new File(downFileName);

	if (!file.exists()) {
	    throw new FileNotFoundException(downFileName);
	}

	if (!file.isFile()) {
	    throw new FileNotFoundException(downFileName);
	}

	byte[] b = new byte[BUFF_SIZE]; //buffer size 2K.
    String fName = (new String(orgFileName.getBytes(), "UTF-8")).replaceAll("\r\n","");
	response.setContentType("application/x-msdownload");
	response.setHeader("Content-Disposition:", "attachment; filename=" + fName);
	response.setHeader("Content-Transfer-Encoding", "binary");
	response.setHeader("Pragma", "no-cache");
	response.setHeader("Expires", "0");

	BufferedInputStream fin = null;
	BufferedOutputStream outs = null;

	try {
		fin = new BufferedInputStream(new FileInputStream(file));
	    outs = new BufferedOutputStream(response.getOutputStream());
	    int read = 0;

		while ((read = fin.read(b)) != -1) {
		    outs.write(b, 0, read);
		}
	} finally {
	    if (outs != null) {
			try {
			    outs.close();
			} catch (IOException ignore) {
				log.debug("IGNORED: {}", ignore.getMessage());
			}
		    }
		    if (fin != null) {
			try {
			    fin.close();
			} catch (IOException ignore) {
				log.debug("IGNORED: {}", ignore.getMessage());
			}
		    }
		}
    }

    /**
     * 첨부로 등록된 파일을 서버에 업로드한다.
     *
     * @param file
     * @return
     * @throws Exception

    public static HashMap<String, String> uploadFile(MultipartFile file) throws Exception {

	HashMap<String, String> map = new HashMap<String, String>();
	//Write File 이후 Move File????
	String newName = "";
	String stordFilePath = EgovProperties.getProperty("Globals.fileStorePath");
	String orginFileName = file.getOriginalFilename();

	int index = orginFileName.lastIndexOf(".");
	//String fileName = orginFileName.substring(0, _index);
	String fileExt = orginFileName.substring(index + 1);
	long size = file.getSize();

	//newName 은 Naming Convention에 의해서 생성
	newName = EgovStringUtil.getTimeStamp() + "." + fileExt;
	writeFile(file, newName, stordFilePath);
	//storedFilePath는 지정
	map.put(Globals.ORIGIN_FILE_NM, orginFileName);
	map.put(Globals.UPLOAD_FILE_NM, newName);
	map.put(Globals.FILE_EXT, fileExt);
	map.put(Globals.FILE_PATH, stordFilePath);
	map.put(Globals.FILE_SIZE, String.valueOf(size));

	return map;
    }
*/
    /**
     * 파일을 실제 물리적인 경로에 생성한다.
     *
     * @param file
     * @param newName
     * @param stordFilePath
     * @throws Exception
     */
    protected static void writeFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
	InputStream stream = null;
	OutputStream bos = null;
	newName = EgovWebUtil.filePathBlackList(EgovStringUtil.isNullToString(newName));
	stordFilePath = EgovWebUtil.filePathBlackList(EgovStringUtil.isNullToString(stordFilePath));
	try {
	    stream = file.getInputStream();
	    File cFile = new File(stordFilePath);

	    if (!cFile.isDirectory()) {
			cFile.mkdir();
		}

	    bos = new FileOutputStream(stordFilePath + File.separator + newName);

	    int bytesRead = 0;
	    byte[] buffer = new byte[BUFF_SIZE];

	    while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
		bos.write(buffer, 0, bytesRead);
	    }
	} catch (FileNotFoundException fnfe) {
		log.debug("fnfe: {}", fnfe);
	} catch (IOException ioe) {
		log.debug("ioe: {}", ioe);
	} finally {
	    if (bos != null) {
		try {
		    bos.close();
		} catch (IOException ignore) {
			log.debug("IGNORED: {}", ignore.getMessage());
		}
	    }
	    if (stream != null) {
		try {
		    stream.close();
		} catch (IOException ignore) {
			log.debug("IGNORED: {}", ignore.getMessage());
		}
	    }
	}
    }

    /**
     * 서버 파일에 대하여 다운로드를 처리한다.
     *
     * @param response
     * @param streFileNm
     *            : 파일저장 경로가 포함된 형태
     * @param orignFileNm
     * @throws Exception
     */
    public void downFile(HttpServletResponse response, String streFileNm, String orignFileNm) throws Exception {
    //	String downFileName = EgovStringUtil.isNullToString(request.getAttribute("downFile")).replaceAll("..","");
    //	String orgFileName = EgovStringUtil.isNullToString(request.getAttribute("orgFileName")).replaceAll("..","");
    String downFileName = EgovWebUtil.filePathBlackList(EgovStringUtil.isNullToString(streFileNm));
    String orgFileName = EgovWebUtil.filePathBlackList(EgovStringUtil.isNullToString(orignFileNm));

	File file = new File(downFileName);
	//log.debug(this.getClass().getName()+" downFile downFileName "+downFileName);
	//log.debug(this.getClass().getName()+" downFile orgFileName "+orgFileName);

	if (!file.exists()) {
	    throw new FileNotFoundException(downFileName);
	}

	if (!file.isFile()) {
	    throw new FileNotFoundException(downFileName);
	}

	//byte[] b = new byte[BUFF_SIZE]; //buffer size 2K.
	int fSize = (int)file.length();
	if (fSize > 0) {
	    BufferedInputStream in = null;

	    try {
		in = new BufferedInputStream(new FileInputStream(file));

    	    	String mimetype = "text/html"; //"application/x-msdownload"

    	    	response.setBufferSize(fSize);
		response.setContentType(mimetype);
		response.setHeader("Content-Disposition:", "attachment; filename=" + orgFileName);
		response.setContentLength(fSize);
		//response.setHeader("Content-Transfer-Encoding","binary");
		//response.setHeader("Pragma","no-cache");
		//response.setHeader("Expires","0");
		FileCopyUtils.copy(in, response.getOutputStream());
	    } finally {
		if (in != null) {
		    try {
			in.close();
		    } catch (IOException ignore) {
		    	log.debug("IGNORED: {}", ignore.getMessage());
		    }
		}
	    }
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	}

	/*
	String uploadPath = propertiesService.getString("fileDir");

	File uFile = new File(uploadPath, requestedFile);
	int fSize = (int) uFile.length();

	if (fSize > 0) {
	    BufferedInputStream in = new BufferedInputStream(new FileInputStream(uFile));

	    String mimetype = "text/html";

	    response.setBufferSize(fSize);
	    response.setContentType(mimetype);
	    response.setHeader("Content-Disposition", "attachment; filename=\""
					+ requestedFile + "\"");
	    response.setContentLength(fSize);

	    FileCopyUtils.copy(in, response.getOutputStream());
	    in.close();
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
	} else {
	    response.setContentType("text/html");
	    PrintWriter printwriter = response.getWriter();
	    printwriter.println("<html>");
	    printwriter.println("<br><br><br><h2>Could not get file name:<br>" + requestedFile + "</h2>");
	    printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
	    printwriter.println("<br><br><br>&copy; webAccess");
	    printwriter.println("</html>");
	    printwriter.flush();
	    printwriter.close();
	}
	//*/


	/*
	response.setContentType("application/x-msdownload");
	response.setHeader("Content-Disposition:", "attachment; filename=" + new String(orgFileName.getBytes(),"UTF-8" ));
	response.setHeader("Content-Transfer-Encoding","binary");
	response.setHeader("Pragma","no-cache");
	response.setHeader("Expires","0");

	BufferedInputStream fin = new BufferedInputStream(new FileInputStream(file));
	BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());
	int read = 0;

	while ((read = fin.read(b)) != -1) {
	    outs.write(b,0,read);
	}
	log.debug(this.getClass().getName()+" BufferedOutputStream Write Complete!!! ");

	outs.close();
    	fin.close();
	//*/
    }
}
