<%--
  Class Name : EgovIndvdlSchdulManageDetail.jsp
  Description : 일정관리 상세보기
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2008.03.09    장동한          최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 장동한
    since    : 2009.03.09
   
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%pageContext.setAttribute("crlf", "\r\n"); %>
<%
String sLinkType = request.getParameter("linkType") == null ? "" : (String)request.getParameter("linkType");
%>
<c:set var="ImgUrl" value="/images/egovframework/cop/smt/sim/"/>
<c:set var="sLinkType" value="<%=sLinkType %>"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<title>일정 상세</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>


<script type="text/javaScript" language="javascript">


/* ********************************************************
 * 초기화
 ******************************************************** */
function fn_egov_init_IndvdlSchdulManage(){

}

/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list_IndvdlSchdulManage(){
    <%-- 일정목록 이동 --%>
    <c:if test="${sLinkType eq ''}">
    history.back();
    </c:if>
    
    <%-- 전체일정목록 이동 --%>
    <c:if test="${sLinkType eq 'asm'}">
        location.href = "<c:url value='/'/>/cop/smt/sim/EgovIndvdlSchdulManageMonthList.do";
    </c:if>
}
/* ********************************************************
 * 저장처리화면
 ******************************************************** */
function fn_egov_modify_IndvdlSchdulManage(){
    var vFrom = document.IndvdlSchdulManageForm;
    vFrom.cmd.value = '';
    vFrom.action = "<c:url value='/'/>/cop/smt/sim/EgovIndvdlSchdulManageModify.do";;
    vFrom.submit();

}
/* ********************************************************
 * 삭제처리
 ******************************************************** */
function fn_egov_delete_IndvdlSchdulManage(){
    var vFrom = document.IndvdlSchdulManageForm;
    if(confirm("삭제 하시겠습니까?")){
        vFrom.cmd.value = 'del';
        vFrom.action = "<c:url value='/'/>/cop/smt/sim/EgovIndvdlSchdulManageDetail.do";
        vFrom.submit();
    }else{
        vFrom.cmd.value = '';
    }
}
</script>

</head>
<body onLoad="fn_egov_init_IndvdlSchdulManage();">
<noscript>자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>	
<!-- 전체 레이어 시작 -->
<div id="wrap">
	<!-- header 시작 -->
	<div id="header_mainsize"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncHeader" /></div>
	<div id="topnavi"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncTopnav" /></div>		
	<!-- //header 끝 -->	
	<!-- container 시작 -->
	<div id="container">
		<!-- 좌측메뉴 시작 -->
		<div id="leftmenu"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncLeftmenu" /></div>
		<!-- //좌측메뉴 끝 -->
			<!-- 현재위치 네비게이션 시작 -->
			<div id="content">
				<div id="cur_loc">
					<div id="cur_loc_align">
						<ul>
							<li>HOME</li>
							<li>&gt;</li>
							<li>사용자관리</li>
							<li>&gt;</li>
							<li><strong>일정관리 상세보기</strong></li>
						</ul>
					</div>
				</div>
				<!-- 검색 필드 박스 시작 -->
				<div id="search_field">
					<div id="search_field_loc"><h2><strong>일정관리 상세보기</strong></h2></div>
				</div>
                <form name="IndvdlSchdulManageForm" id="IndvdlSchdulManageFormh" action="<c:url value='/'/>/cop/smt/sim/EgovIndvdlSchdulManageDetail.do" method="post">
                <input type="submit" id="invisible" class="invisible"/>
					<div class="modify_user" >
						<table>
							<tr>
								<th width="20%" height="23" class="required_text" nowrap >일정구분</th>
							    <td width="80%" >
							    <c:forEach items="${schdulSe}" var="schdulSeInfo" varStatus="status">
							    <c:if test="${schdulSeInfo.code eq resultList[0].schdulSe}">    
							     <c:out value="${fn:replace(schdulSeInfo.codeNm , crlf , '<br/>')}" escapeXml="false" />
							    </c:if>
							    </c:forEach>
							    </td>
							</tr>
							<tr>
								<th width="20%" height="23" class="required_text" nowrap >중요도</th>
							    <td width="80%" >
							    <c:forEach items="${schdulIpcrCode}" var="schdulSeInfo" varStatus="status">
							    <c:if test="${schdulSeInfo.code eq resultList[0].schdulIpcrCode}">  
							     <c:out value="${fn:replace(schdulSeInfo.codeNm , crlf , '<br/>')}" escapeXml="false" />
							    </c:if>
							    </c:forEach>
							    </td>
							</tr>
							<tr>
								<th width="20%" height="23" class="required_text" nowrap >부서</th>
							    <td width="80%" >
							        <c:out value="${fn:replace(resultList[0].schdulDeptName , crlf , '<br/>')}" escapeXml="false" />
							    </td>
							</tr>
							<tr>
								<th width="20%" height="23" class="required_text" nowrap >일정명</th>
							    <td width="80%" >
							      <c:out value="${fn:replace(resultList[0].schdulNm , crlf , '<br/>')}" escapeXml="false" />
							    </td>
							</tr>
							<tr>
								<th height="23" class="required_text" >일정 내용</th>
							    <td>
							        <c:out value="${fn:replace(resultList[0].schdulCn , crlf , '<br/>')}" escapeXml="false" />
							    </td>
							</tr>
						    <tr> 
						      <th width="20%" height="23" class="required_text" nowrap >반복구분</th>
						      <td width="80%">
						          <c:forEach items="${reptitSeCode}" var="schdulSeInfo" varStatus="status">
						          <c:if test="${schdulSeInfo.code eq resultList[0].reptitSeCode}">    
						          <c:out value="${fn:replace(schdulSeInfo.codeNm , crlf , '<br/>')}" escapeXml="false" />
						          </c:if>
						          </c:forEach>
						      </td>
						    </tr>
						
						  <tr> 
						    <th width="20%" height="23" class="required_text" nowrap >날짜/시간</th>
						    <td width="80%" >
						    ${fn:substring(resultList[0].schdulBgnde, 0, 4)}-${fn:substring(resultList[0].schdulBgnde, 4, 6)}-${fn:substring(resultList[0].schdulBgnde, 6, 8)} ${fn:substring(resultList[0].schdulBgnde, 8, 10)}시  ${fn:substring(resultList[0].schdulBgnde, 10, 12)}분 ~      
						    ${fn:substring(resultList[0].schdulEndde, 0, 4)}-${fn:substring(resultList[0].schdulEndde, 4, 6)}-${fn:substring(resultList[0].schdulEndde, 6, 8)} ${fn:substring(resultList[0].schdulEndde, 8, 10)}시  ${fn:substring(resultList[0].schdulEndde, 10, 12)}분 
						    </td>
						  </tr>
						  
						  <tr> 
						    <th width="20%" height="23" class="required_text" nowrap >담당자</th>
						    <td width="80%" >
						    <c:out value="${fn:replace(resultList[0].schdulChargerName , crlf , '<br/>')}" escapeXml="false" />
						    </td>
						  </tr>
						  
						<!-- 첨부파일 테이블 레이아웃 설정 Start.. -->
						  <tr>
						    <th height="23" class="required_text" nowrap="nowrap">파일첨부</th>
						    <td>
						        <c:import charEncoding="utf-8" url="/cmm/fms/selectFileInfs.do" > 
						        <c:param name="param_atchFileId" value="${resultList[0].atchFileId}" /> 
						        </c:import>       
						     </td>
						  </tr>
						<!-- 첨부파일 테이블 레이아웃 End. -->

						</table>
					</div>
					<!-- 버튼 시작(상세지정 style로 div에 지정) -->
					<div class="buttons" style="padding-top:10px;padding-bottom:10px;">
						<!-- 목록/저장버튼 -->
						<table border="0" cellspacing="0" cellpadding="0" align="center">
						<tr> 
						  <td>
						      <a href="#LINK" onclick="JavaScript:fn_egov_list_IndvdlSchdulManage(); return false;"><spring:message code="button.list" /></a> 
						  </td>
						  <td width="10"></td>
						  <td>
						      <% if(null != session.getAttribute("LoginVO")){ %>
						      <a href="#LINK" onclick="JavaScript:fn_egov_delete_IndvdlSchdulManage(); return false;"><spring:message code="button.delete" /></a> 
						      <% } %>
						  </td>
                          <td width="10"></td>
						  <td>
						  <% if(null != session.getAttribute("LoginVO")){ %>
						      <a href="#LINK" onclick="JavaScript:fn_egov_modify_IndvdlSchdulManage(); return false;"><spring:message code="button.update" /></a> 
						  <% }%>
						  </td>
						</tr>
						</table>
					</div>
					<!-- 버튼 끝 -->							
					<input name="schdulId" type="hidden" value="${resultList[0].schdulId}">
					<input name="linkType" type="hidden" value="${sLinkType}">
					<input name="cmd" type="hidden" value="<c:out value=''/>"/>
				</form>

			</div>	
			<!-- //content 끝 -->	
	</div>	
	<!-- //container 끝 -->
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter" /></div>
	<!-- //footer 끝 -->
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>

