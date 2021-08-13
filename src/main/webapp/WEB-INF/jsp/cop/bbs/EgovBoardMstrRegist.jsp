<%--
  Class Name : EgovBoardMstrRegist.jsp
  Description : 게시판 생성 화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.12   이삼섭          최초 생성
     2009.06.26   한성곤          2단계 기능 추가 (댓글관리, 만족도조사)
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.12  
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="content-language" content="ko">
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<link href="<c:url value='/css/button.css' />" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<c:url value='/js/EgovBBSMng.js' />"></script>
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="boardMaster" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="<c:url value='/js/showModalDialog.js'/>" ></script>
<script type="text/javascript">
    function fn_egov_regist_brdMstr(){
        if (!validateBoardMaster(document.boardMaster)){
            return;
        }

        if (confirm('<spring:message code="common.regist.msg" />')) {
            form = document.boardMaster;
            form.action = "<c:url value='/cop/bbs/insertBBSMasterInf.do'/>";
            form.submit();
        }
    }
    
    function fn_egov_select_brdMstrList(){
        form = document.boardMaster;
        form.action = "<c:url value='/cop/bbs/SelectBBSMasterInfs.do'/>";
        form.submit();  
    }
    
    function fn_egov_inqire_tmplatInqire(){
        form = document.boardMaster;
        var retVal;
        var url = "<c:url value='/cop/com/openPopup.do?requestUrl=/cop/com/selectTemplateInfsPop.do&typeFlag=BBS&width=850&height=360'/>";      
        var openParam = "dialogWidth: 890px; dialogHeight: 400px; resizable: 0, scroll: 1, center: 1";
         
        retVal = window.showModalDialog(url,"p_tmplatInqire", openParam);
        
        if(retVal != null){
            var tmp = retVal.split("|");
            form.tmplatId.value = tmp[0];
            form.tmplatNm.value = tmp[1];
        }
    }
    
    function showModalDialogCallback(retVal) {
    	if(retVal != null){
            var tmp = retVal.split("|");
            form.tmplatId.value = tmp[0];
            form.tmplatNm.value = tmp[1];
        }
    }
    
</script>

<title>게시판 생성</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

</head>
<body>
<noscript class="noScriptTitle">자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>
<%//<form:form modelAttribute="boardMaster" name="boardMaster" method="post" action="<c:url value='/cop/bbs/SelectBBSMasterInfs.do'/>"> %>
<%//<form:form modelAttribute="boardMaster" name="boardMaster" method="post" action="${pageContext.request.contextPath}/cop/bbs/SelectBBSMasterInfs.do"> //%>

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
                            <li><strong>게시판생성관리</strong></li>
                        </ul>
                    </div>
                </div>
                <!-- 검색 필드 박스 시작 -->
                <div id="search_field">
                    <div id="search_field_loc"><h2><strong>게시판 생성</strong></h2></div>
                </div>
				<form:form modelAttribute="boardMaster" name="boardMaster" method="post" action="cop/bbs/SelectBBSMasterInfs.do">
				
				    <input type="hidden" name="pageIndex"  value="<c:out value='${searchVO.pageIndex}'/>"/>
                    <div id="border" class="modify_user" >
                        <table summary="게시판명,게시판소개,게시판 유형,게시판 속성,답장가능여부,파일첨부가능여부, ..  입니다">
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >
						            <label for="bbsNm"> 
						                <spring:message code="cop.bbsNm" />
						            </label>    
						        <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/></th>
						        <td width="80%" nowrap colspan="3">
						            <form:input title="게시판명입력" path="bbsNm" size="60" cssStyle="width:100%" />
						            <br/><form:errors path="bbsNm" />
						        </td>
					      </tr>
					      <tr> 
					        <th height="23" class="required_text" >
					            <label for="bbsIntrcn">
					                <spring:message code="cop.bbsIntrcn" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td colspan="3">
					           <form:textarea title="게시판소개입력" path="bbsIntrcn" cols="75" rows="4" cssStyle="width:100%" />
					           <br/><form:errors path="bbsIntrcn" />
					        </td>
					      </tr>
					      <tr> 
					        <th width="20%" height="23" class="required_text" nowrap >
					            <label for="bbsTyCode">
					                <spring:message code="cop.bbsTyCode" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="30%" nowrap="nowrap">
					            <form:select path="bbsTyCode" title="게시판유형선택">
					                <form:option value='' label="--선택하세요--" />
					                <form:options items="${typeList}" itemValue="code" itemLabel="codeNm"/>
					            </form:select>
					           <br/><form:errors path="bbsTyCode" />
					        </td>
					        
					        <th width="20%" height="23" class="required_text" nowrap >
					            <label for="bbsAttrbCode">  
					                <spring:message code="cop.bbsAttrbCode" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>    
					        <td width="30%" nowrap="nowrap">
					            <form:select path="bbsAttrbCode" title="게시판속성선택">
					                <form:option value='' label="--선택하세요--" />
					                <form:options items="${attrbList}" itemValue="code" itemLabel="codeNm"/>
					            </form:select>      
					            <br/><form:errors path="bbsAttrbCode" />
					        </td>    
					      </tr> 
					      
					      <tr> 
					        <th width="20%" height="23" class="required_text" >
					            <label>
					                <spring:message code="cop.replyPosblAt" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="30%" nowrap="nowrap">
					            <spring:message code="button.possible" /> : <form:radiobutton path="replyPosblAt"  value="Y" />&nbsp;
					            <spring:message code="button.impossible" /> : <form:radiobutton path="replyPosblAt"  value="N"  />
					             <br/><form:errors path="replyPosblAt" />
					        </td>
					        
					        <th width="20%" height="23" class="required_text" >
					            <label>
					                <spring:message code="cop.fileAtchPosblAt" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>    
					        <td width="30%" nowrap="nowrap">
					            <spring:message code="button.possible" /> : <form:radiobutton path="fileAtchPosblAt"  value="Y" 
					               onclick="document.boardMaster.posblAtchFileNumber.disabled='';" />&nbsp;
					            <spring:message code="button.impossible" /> : <form:radiobutton path="fileAtchPosblAt"  value="N" 
					               onclick="document.boardMaster.posblAtchFileNumber.disabled='disabled';" />
					             <br/><form:errors path="fileAtchPosblAt" />
					        </td>    
					      </tr> 
					      
					      <tr> 
					        <th width="20%" height="23" class="required_text"  >
					            <label for="posblAtchFileNumber">
					                <spring:message code="cop.posblAtchFileNumber" />
					            </label>    
					        </th>
					        <td width="30%" nowrap colspan="3" >
					            <form:select path="posblAtchFileNumber" title="첨부가능파일 숫자선택" >
					               <form:option value="0"  label="---선택하세요--" />
					               <form:option value='1'>1개</form:option>
					               <form:option value='2'>2개</form:option>
					               <form:option value='3'>3개</form:option>
					           </form:select>
					           <br/><form:errors path="posblAtchFileNumber" />
					        </td>
					      </tr>   
					      <tr> 
					        <th width="20%" height="23" class="required_text" >
					            <label for="tmplatNm">
					                <spring:message code="cop.tmplatId" />
					            </label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>
					        <td width="30%" nowrap colspan="3">
					         <form:input path="tmplatNm" size="20" readonly="true" title="템플릿정보입력"/>
					         <form:hidden path="tmplatId"  />
					         &nbsp;<a href="#LINK" onclick="fn_egov_inqire_tmplatInqire(); return false;" style="selector-dummy: expression(this.hideFocus=false);">
					         <img src="<c:url value='/images/img_search.gif' />"
					                    width="15" height="15" align="middle" alt="새창" /></a>
					         <br/><form:errors path="tmplatId" />
					        </td>
					      </tr>
					        <!-- 2009.06.26 : 2단계 기능 추가  -->
					        <c:if test="${addedOptions == 'true'}">
					
					          <tr> 
					            <th width="20%" height="23" class=""><label for="option">추가 선택사항</label></th>
					            <td width="30%" nowrap colspan="3" >
					                <form:select path="option" title="추가선택사항선택" >
					                   <form:option value=""  label="미선택" />
					                   <form:option value='comment'>댓글</form:option>
					                   <form:option value='stsfdg'>만족도조사</form:option>
					               </form:select>
					            </td>
					          </tr>          
					
					        </c:if>
					        <!-- 2009.06.26 : 2단계 기능 추가  -->       
					    </table>
                    </div>
 
                    <!-- 버튼 시작(상세지정 style로 div에 지정) -->
                    <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
                        <!-- 목록/저장버튼  -->
                        <table border="0" cellspacing="0" cellpadding="0" align="center">
						    <tr>
	                          <td><a href="#LINK" onclick="javascript:fn_egov_regist_brdMstr(); return false;"><spring:message code="button.save" /></a> 
						      </td>
						      <td width="10"></td>
                              <td><a href="#LINK" onclick="javascript:fn_egov_select_brdMstrList(); return false;"><spring:message code="button.list" /></a> 
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

