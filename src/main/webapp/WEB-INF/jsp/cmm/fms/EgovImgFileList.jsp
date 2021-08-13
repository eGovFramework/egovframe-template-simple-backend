<%--
  Class Name : EgovImgFileList.jsp
  Description : 이미지 파일 조회화면(include)
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.31  이삼섭          최초 생성
     2011.08.31  JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.26  
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table>
    <c:forEach var="fileVO" items="${fileList}" varStatus="status">
        <tr>
            <td>
                <img src='<c:url value='/cmm/fms/getImage.do'/>?atchFileId=<c:out value="${fileVO.atchFileId}"/>&amp;fileSn=<c:out value="${fileVO.fileSn}"/>' width="640" alt="파일보기링크" />
            </td>
        </tr>
    </c:forEach>
</table>
