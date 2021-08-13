<%--
  Class Name : EgovTemplateRegist.jsp
  Description : 템플릿 속성 등록화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.18   이삼섭          최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.18
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="templateInf" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript">
    function fn_egov_regist_tmplatInfo(){
        if (!validateTemplateInf(document.templateInf)){
            return;
        }
        
        if (confirm('<spring:message code="common.regist.msg" />')) {
            document.templateInf.action = "<c:url value='/cop/com/insertTemplateInf.do'/>";
            document.templateInf.submit();
        }
    }
    
    function fn_egov_select_tmplatInfo(){
        document.templateInf.action = "<c:url value='/cop/com/selectTemplateInfs.do'/>";
        document.templateInf.submit();  
    }

    function fn_egov_selectTmplatType(obj){
        if (obj.value == 'TMPT01') {
            document.getElementById('sometext').innerHTML = "게시판 템플릿은 CSS만 가능합니다.";
        } else if (obj.value == '') {
            document.getElementById('sometext').innerHTML = "";
        } else {
            document.getElementById('sometext').innerHTML = "템플릿은 JSP만 가능합니다.";
        }       
    }

    function fn_egov_previewTmplat() {
        var frm = document.templateInf;
        
        var url = frm.tmplatCours.value;

        var target = "";

        if (frm.tmplatSeCode.value == 'TMPT01') {
            target = "<c:url value='/cop/bbs/previewBoardList.do'/>";
            width = "1024";
        } else {
        	alert('<spring:message code="cop.tmplatCours" /> 지정 후 선택해 주세요.');
        }

        if (target != "") {
            window.open(target + "?searchWrd="+url, "preview", "width=" + width + "px, height=500px;");
        }
    }
</script>
<title>템플릿 등록</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>
</head>

<body>
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
                            <li>사이트관리</li>
                            <li>&gt;</li>
                            <li><strong>게시판템플릿관리</strong></li>
                        </ul>
                    </div>
                </div>
                <!-- 검색 필드 박스 시작 -->
                <div id="search_field">
                    <div id="search_field_loc"><h2><strong>템플릿 등록</strong></h2></div>
                </div>
				<form:form modelAttribute="templateInf" name="templateInf" method="post" >
    				<input type="hidden" name="pageIndex" value="<c:out value='${searchVO.pageIndex}'/>" />

                    <div class="modify_user" >
                        <table >
					      <tr> 
					        <th width="20%" height="23" class="required_text" nowrap="nowrap">
					            <label for="tmplatNm">
					                <spring:message code="cop.tmplatNm" />
					            </label>    
					            <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="80%" nowrap="nowrap">
					          <input name="tmplatNm" type="text" size="60" value="" maxlength="60" style="width:100%" id="tmplatNm"  title="템플릿명">
					          <br/><form:errors path="tmplatNm" /> 
					        </td>
					      </tr>
					      
					      <tr> 
					        <th height="23" class="required_text" >
					            <label for="tmplatSeCode">  
					                <spring:message code="cop.tmplatSeCode" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td>
					        <select name="tmplatSeCode" class="select" onchange="fn_egov_selectTmplatType(this)" id="tmplatSeCode" title="템플릿구분">
					               <option selected value=''>--선택하세요--</option>
					            <c:forEach var="result" items="${resultList}" varStatus="status">
					                <option value='<c:out value="${result.code}"/>'><c:out value="${result.codeNm}"/></option>
					            </c:forEach>    
					        </select>&nbsp;&nbsp;&nbsp;<span id="sometext"></span>
					           <br/><form:errors path="tmplatSeCode" />
					        </td>
					      </tr> 
					      <tr> 
					        <th width="20%" height="23" class="required_text" nowrap="nowrap">
					            <label for="tmplatCours">   
					                <spring:message code="cop.tmplatCours" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="80%" nowrap="nowrap">
					          <input name="tmplatCours" type="text" size="60" value="" maxlength="60" style="width:100%" id="tmplatCours"  title="템플릿경로">
					          <br/><form:errors path="tmplatCours" /> 
					        </td>
					      </tr>
					      <tr> 
					        <th width="20%" height="23" class="required_text" nowrap="nowrap">
					            <label> 
					                <spring:message code="cop.useAt" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="80%" nowrap="nowrap">
					            Y : <input type="radio" name="useAt" class="radio2" value="Y" checked="checked">&nbsp;
					            N : <input type="radio" name="useAt" class="radio2" value="N">
					            <br/><form:errors path="useAt" />
					        </td>
					      </tr>  
                        </table>
                    </div>


                    <!-- 버튼 시작(상세지정 style로 div에 지정) -->
                    <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
                      <!-- 목록/저장버튼  -->
                      <table border="0" cellspacing="0" cellpadding="0" align="center">
                        <tr> 
                          <td>
                              <a href="<c:url value='/cop/com/insertTemplateInf.do'/>" onclick="fn_egov_regist_tmplatInfo(); return false;">저장</a> 
                          </td>
                          <td width="10"></td>
                          <td>
                              <a href="<c:url value='/cop/com/selectTemplateInfs.do'/>" title="목록" >목록</a>
                          </td>
                          <td width="10"></td>
                          <td>
                              <a href="#LINK" onclick="fn_egov_previewTmplat();" title="새창" >미리보기</a>
                          </td>  
                        </tr>
                      </table>
                    </div>
                    <!-- 버튼 끝 -->                           
                </form:form>

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

