<%--
  Class Name : EgovBoardMstrUpdt.jsp
  Description : 게시판 속성정보 변경화면
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
<meta http-equiv="Content-Language" content="ko" >
<title>게시판 정보수정</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >

<script type="text/javascript" src="<c:url value="/js/EgovBBSMng.js" />" ></script>
<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="boardMaster" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javascript" src="<c:url value='/js/showModalDialog.js'/>" ></script>
<script type="text/javascript">
    function fn_egov_validateForm(obj){
        return true;
    }
    
    function fn_egov_update_brdMstr(){
        if (!validateBoardMaster(document.boardMaster)){
            return;
        }
        
        if(confirm('<spring:message code="common.update.msg" />')){
            document.boardMaster.action = "<c:url value='/cop/bbs/UpdateBBSMasterInf.do'/>";
            document.boardMaster.submit();                  
        }
    }   
    
    function fn_egov_select_brdMstrList(){
        document.boardMaster.action = "<c:url value='/cop/bbs/SelectBBSMasterInfs.do'/>";
        document.boardMaster.submit();  
    }   
    
    function fn_egov_delete_brdMstr(){
        if(confirm('<spring:message code="common.delete.msg" />')){
            document.boardMaster.action = "<c:url value='/cop/bbs/DeleteBBSMasterInf.do'/>";
            document.boardMaster.submit();  
        }       
    }
    
	function showModalDialogCallback(retVal) {
    	if (retVal) {
    		var tmp = retVal.split("|");
            document.boardMaster.tmplatId.value = tmp[0];
            document.boardMaster.tmplatNm.value = tmp[1];
    	}
    }
    
    function fn_egov_inqire_tmplatInqire(){
        var retVal;
        var url = "<c:url value='/cop/com/openPopup.do?requestUrl=/cop/com/selectTemplateInfsPop.do&typeFlag=BBS&width=850&height=360'/>";      
        var openParam = "dialogWidth: 850px; dialogHeight: 360px; resizable: 0, scroll: 1, center: 1";
         
        retVal = window.showModalDialog(url,"p_tmplatInqire", openParam);
        if(retVal != null){
            var tmp = retVal.split("|");
            document.boardMaster.tmplatId.value = tmp[0];
            document.boardMaster.tmplatNm.value = tmp[1];
        }
    }   
</script>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

</head>
<body>
<noscript class="noScriptTitle">자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>
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
                    <div id="search_field_loc"><h2><strong>게시판 정보수정 및 상세보기</strong></h2></div>
					<form:form modelAttribute="boardMaster" name="boardMaster" action="<c:url value='/cop/bbs/SelectBBSMasterInfs.do'/>" method="post" >
						<input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>
						<input name="bbsId" type="hidden" value="<c:out value='${result.bbsId}'/>" />
						<input name="bbsTyCode" type="hidden" value="<c:out value='${result.bbsTyCode}'/>" />
						<input name="bbsAttrbCode" type="hidden" value="<c:out value='${result.bbsAttrbCode}'/>" />
						<input name="replyPosblAt" type="hidden" value="<c:out value='${result.replyPosblAt}'/>" />


                    <div id="border" class="modify_user" >
                        <table summary="게시판명,게시판 소개,게시판 유형,게시판 속성,답장가능여부, ..   입니다">
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >
					            <label for="bbsNm">게시판명</label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
                                </th>
                                <td width="80%" nowrap colspan="3">
						          <input title="게시판명입력" id="bbsNm" name="bbsNm" type="text" size="60" value='<c:out value="${result.bbsNm}"/>' maxlength="60" style="width:100%" > 
						          <br/><form:errors path="bbsNm" />
                                </td>
                          </tr>
                          <tr> 
                            <th height="23" class="required_text" >
                                <label for="bbsIntrcn">게시판 소개</label>
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
                            </th>
                            <td colspan="3">
					           <textarea title="게시판소개입력" id="bbsIntrcn" name="bbsIntrcn" class="textarea"  cols="75" rows="4"  style="width:100%"><c:out value="${result.bbsIntrcn}" escapeXml="true" /></textarea> 
					           <form:errors path="bbsIntrcn" />
                            </td>
                          </tr>
                          <tr> 
                            <th width="20%" height="23" class="required_text" nowrap >게시판 유형</th>
					        <td width="30%" nowrap="nowrap"><c:out value="${result.bbsTyCodeNm}"/></td>
					        <th width="20%" height="23" class="" nowrap >게시판 속성</th>    
					        <td width="30%" nowrap="nowrap"><c:out value="${result.bbsAttrbCodeNm}"/></td>    
                          </tr> 
                          
                          <tr> 
                            <th width="20%" height="23" class="" nowrap="nowrap">답장가능여부</th>
					        <td>
					            <c:choose>
					                <c:when test="${result.replyPosblAt == 'Y'}">
					                    <spring:message code="button.possible" /> 
					                </c:when>
					                <c:otherwise>
					                    <spring:message code="button.impossible" />
					                </c:otherwise>
					            </c:choose>
					        </td>
					        <th width="20%" height="23" class="required_text" nowrap >
					            <label>파일첨부가능여부</label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
					        </th>    
					        <td width="30%" nowrap="nowrap">
					            <spring:message code="button.possible" /> : <input type="radio" name="fileAtchPosblAt" class="radio2" 
					                onclick="document.boardMaster.posblAtchFileNumber.disabled='';"
					                value="Y" <c:if test="${result.fileAtchPosblAt == 'Y'}"> checked="checked"</c:if>>&nbsp;
					            <spring:message code="button.impossible" /> : <input type="radio" name="fileAtchPosblAt" class="radio2" 
					                onclick="document.boardMaster.posblAtchFileNumber.disabled='disabled';"
					                value="N" <c:if test="${result.fileAtchPosblAt == 'N'}"> checked="checked"</c:if>>
					            <br/><form:errors path="fileAtchPosblAt" />
					        </td>    
                          </tr> 
                          
                          <tr> 
					        <th width="20%" height="23" class="required_text" nowrap >
					            <label for="posblAtchFileNumber">첨부가능파일 숫자</label>    
                            </th>
					        <td width="30%" nowrap colspan="3">
					            <select title="첨부가능파일 숫자선택" id="posblAtchFileNumber" name="posblAtchFileNumber" class="select" <c:if test="${result.fileAtchPosblAt == 'N'}"> disabled="disabled"</c:if>>
					               <option selected value="0">--선택하세요--</option>
					               <option value='1' <c:if test="${result.posblAtchFileNumber == '1'}">selected="selected"</c:if>>1개</option>
					               <option value='2' <c:if test="${result.posblAtchFileNumber == '2'}">selected="selected"</c:if>>2개</option>
					               <option value='3' <c:if test="${result.posblAtchFileNumber == '3'}">selected="selected"</c:if>>3개</option>
					           </select>
					           <br/><form:errors path="posblAtchFileNumber" />
					        </td>

                          </tr>   
                          <tr> 
					        <th width="20%" height="23" class="required_text" nowrap >
					            <label for="tmplatNm">템플릿 정보</label>    
                                <img src="<c:url value='/images/required.gif' />" width="15" height="15" alt="required"/>
                            </th>
                            <td width="30%" nowrap colspan="3">
						         <input title="템플릿정보입력" id="tmplatNm" name="tmplatNm" type="text" size="20" value="<c:out value="${result.tmplatNm}"/>"  maxlength="20" readonly >
						         <input name="tmplatId" type="hidden" size="20" value='<c:out value="${result.tmplatId}"/>' >
						         &nbsp;<a href="#LINK" onclick="fn_egov_inqire_tmplatInqire(); return false;">
                                        <img src="<c:url value='/images/img_search.gif' />" width="15" height="15" align="middle" alt="새창">
                                       </a>
						         <br/><form:errors path="tmplatId" />
                            </td>
                          </tr>
					        <!-- 2009.06.26 : 2단계 기능 추가  -->
					        <c:if test="${addedOptions == 'true'}">
					
					          <tr> 
					            <th width="20%" height="23" class="">추가 선택사항</th>
					            <td width="30%" nowrap colspan="3" >
					                <select title="추가선택사항선택" name="option" class="select" <c:if test="${result.option != 'na'}">disabled="disabled"</c:if>>
					                    <option value='na' <c:if test="${result.option == 'na'}">selected="selected"</c:if>>---선택하세요--</option>
					                    <option value='' <c:if test="${result.option == ''}">selected="selected"</c:if>>미선택</option>
					                    <option value='comment' <c:if test="${result.option == 'comment'}">selected="selected"</c:if>>댓글</option>
					                    <option value='stsfdg' <c:if test="${result.option == 'stsfdg'}">selected="selected"</c:if>>만족도조사</option>
					               </select>
					              ※ 추가 선택사항은 수정 불가 (미설정된 기존 게시판의 경우 처음 설정은 가능함)
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
                              <td><a href="#LINK" onclick="javascript:fn_egov_update_brdMstr(); return false;"><spring:message code="button.save" /></a>
                              </td>
                              <td>&nbsp;&nbsp;</td>
                              <td><a href="#LINK" onclick="fn_egov_delete_brdMstr(); return false;"><spring:message code="button.delete" /></a></td>
                              <td>&nbsp;&nbsp;</td>
                              <td><a href="<c:url value='/cop/bbs/SelectBBSMasterInfs.do'/>" onclick="javascript:fn_egov_select_brdMstrList(); return false;"><spring:message code="button.list" /></a>
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
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>
