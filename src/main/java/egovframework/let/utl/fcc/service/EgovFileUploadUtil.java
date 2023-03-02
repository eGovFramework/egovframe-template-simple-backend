package egovframework.let.utl.fcc.service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.com.cmm.EgovWebUtil;

/**
 * @Class Name  : EgovFileUploadUtil.java
 * @Description : Spring 기반 File Upload 유틸리티
 * @Modification Information
 *
 *  수정일               수정자            수정내용
 *  ----------   --------   ---------------------------
 *  2009.08.26   한성곤            최초 생성
 *  2018.08.17   신용호            uploadFilesExt(확장자 기록) 추가
 *  2019.12.06   신용호            checkFileExtension(), checkFileMaxSize() 추가
 *  2020.08.05   신용호            uploadFilesExt Parameter 수정
 *  2021.02.16   신용호            WebUtils.getNativeRequest(request,MultipartHttpServletRequest.class);
 *
 * @author 공통컴포넌트 개발팀 한성곤
 * @since 2009.08.26
 * @version 1.0
 * @see
 */
public class EgovFileUploadUtil extends EgovFormBasedFileUtil {
	/**
	 * 파일을 Upload 처리한다.
	 * EgovFileUploadUtil.uploadFilesExt(확장자 확인) 대체
	 *
	 * @param request
	 * @param where
	 * @param maxFileSize
	 * @return
	 * @throws Exception
	 */
	/*public static List<EgovFormBasedFileVo> uploadFiles(HttpServletRequest request, String where, long maxFileSize)
		throws Exception {
		List<EgovFormBasedFileVo> list = new ArrayList<EgovFormBasedFileVo>();

		//MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest) request;
		MultipartHttpServletRequest mptRequest = WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);

		if (mptRequest != null) {//2022.01 Possible null pointer dereference due to return value of called method 조치
			Iterator<?> fileIter = mptRequest.getFileNames();

			while (fileIter.hasNext()) {
				MultipartFile mFile = mptRequest.getFile((String)fileIter.next());

				EgovFormBasedFileVo vo = new EgovFormBasedFileVo();

				if (mFile != null) {//2022.11 Null pointers should not be dereferenced 조치
					String tmp = mFile.getOriginalFilename();
					if(tmp != null) {//2022.01 Possible null pointer dereference due to return value of called method
						if (tmp.lastIndexOf("\\") >= 0) {
							tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
						}

						vo.setFileName(tmp);
						vo.setContentType(mFile.getContentType());
						vo.setServerSubPath(getTodayString());
						vo.setPhysicalName(getPhysicalFileName());
						vo.setSize(mFile.getSize());

						if (tmp.lastIndexOf(".") >= 0) {
							vo.setPhysicalName(vo.getPhysicalName()); // 2012.11 KISA 보안조치
						}

						if (mFile.getSize() > 0) {
							InputStream is = null;

							try {
								is = mFile.getInputStream();
								saveFile(is, new File(EgovWebUtil.filePathBlackList(
									where + SEPERATOR + vo.getServerSubPath() + SEPERATOR + vo.getPhysicalName())));
							} finally {
								if (is != null) {
									is.close();
								}
							}
							list.add(vo);
						}
					}
				}	
			}
		}

		return list;
	}
	*/

	/**
	 * 파일을 Upload(확장명 저장 및 확장자 제한) 처리한다.
	 *
	 * @param request
	 * @param where
	 * @param maxFileSize
	 * @return
	 * @throws Exception
	 */
	public static List<EgovFormBasedFileVo> uploadFilesExt(MultipartHttpServletRequest mptRequest, String where,
		long maxFileSize, String extensionWhiteList) throws Exception {
		List<EgovFormBasedFileVo> list = new ArrayList<EgovFormBasedFileVo>();

		if (mptRequest != null) {
			Iterator<?> fileIter = mptRequest.getFileNames();

			while (fileIter.hasNext()) {
				MultipartFile mFile = mptRequest.getFile((String)fileIter.next());

				EgovFormBasedFileVo vo = new EgovFormBasedFileVo();
				
				if(mFile != null) {//2022.11 Null pointers should not be dereferenced 조치
					String tmp = mFile.getOriginalFilename();
					if(tmp != null) {//2022.01 Possible null pointer dereference due to return value of called method
						if (tmp.lastIndexOf("\\") >= 0) {
							tmp = tmp.substring(tmp.lastIndexOf("\\") + 1);
						}
						String ext = "";
						if (tmp.lastIndexOf(".") > 0) {
							ext = getFileExtension(tmp).toLowerCase();
						} else {
							throw new SecurityException("Unacceptable file extension."); // 허용되지 않는 확장자 처리
						}
						if (extensionWhiteList.indexOf(ext) < 0) {
							throw new SecurityException("Unacceptable file extension."); // 허용되지 않는 확장자 처리
						}

						vo.setFileName(tmp);
						vo.setContentType(mFile.getContentType());
						vo.setServerSubPath(getTodayString());
						vo.setPhysicalName(getPhysicalFileName() + "." + ext);
						vo.setSize(mFile.getSize());

						if (tmp.lastIndexOf(".") >= 0) {
							vo.setPhysicalName(vo.getPhysicalName()); // 2012.11 KISA 보안조치
						}

						if (mFile.getSize() > 0) {
							InputStream is = null;

							try {
								is = mFile.getInputStream();
								saveFile(is, new File(EgovWebUtil.filePathBlackList(
									where + SEPERATOR + vo.getServerSubPath() + SEPERATOR + vo.getPhysicalName())));
							} finally {
								if (is != null) {
									is.close();
								}
							}
							list.add(vo);
						}
					}
				}
			}
		}

		return list;
	}

	/**
	 * 파일 확장자를 추출한다.
	 *
	 * @param fileNamePath
	 * @return 확장자 : "" 또는 추출된 확장자
	 */
	public static String getFileExtension(String fileNamePath) {

		if (fileNamePath == null) {
			return "";
		}
		String ext = fileNamePath.substring(fileNamePath.lastIndexOf(".") + 1, fileNamePath.length());

		return (ext == null) ? "" : ext;
	}

	/**
	 * 파일 확장자의 허용유무를 검증한다.
	 *
	 * @param fileNamePath
	 * @param whiteListExtensions : ex) .png.pdf.txt
	 * @return true : 허용
	 * @return true : 불가
	 */
	public static boolean checkFileExtension(String fileNamePath, String whiteListExtensions) {
		String extension = getFileExtension(fileNamePath);

		if ("".equals(extension)) {
			return false;
		}

		if (whiteListExtensions == null) {
			return false;
		}
		if ("".equals(whiteListExtensions)) {
			return false;
		}

		if (whiteListExtensions.indexOf("." + extension) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 최대 파일 사이즈 허용유무를 검증한다.
	 *
	 * @param multipartFile
	 * @param maxFileSize : ex) 1048576 = 1M , 1K = 1024
	 * @return true : 허용
	 * @return true : 불가
	 */
	public static boolean checkFileMaxSize(MultipartFile multipartFile, long maxFileSize) {

		if (multipartFile == null) {
			return false;
		}

		if (multipartFile.getSize() <= maxFileSize) {
			return true;
		} else {
			return false;
		}
	}

}
