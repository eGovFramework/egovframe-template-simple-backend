<%--
  Class Name : EgovIndvdlSchdulManageModify.jsp
  Description : 일정관리 수정 페이지
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
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="validator" uri="http://www.springmodules.org/tags/commons-validator" %>

<c:set var="ImgUrl" value="/images/egovframework/cop/smt/sim/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<title>일정 수정</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

<script type="text/javascript" src="<c:url value='/js/EgovCalPopup.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/EgovMultiFile.js'/>" ></script>

<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="indvdlSchdulManageVO" staticJavascript="false" xhtml="true" cdata="false"/>
<script type="text/javaScript" language="javascript">


/* ********************************************************
 * 초기화
 ******************************************************** */
function fn_egov_init_IndvdlSchdulManage(){

    var existFileNum = document.getElementById("indvdlSchdulManageVO").fileListCnt.value;     
    var maxFileNum = document.getElementById("indvdlSchdulManageVO").posblAtchFileNumber.value;


    if(existFileNum=="undefined" || existFileNum ==null){
        existFileNum = 0;
    }

    if(maxFileNum=="undefined" || maxFileNum ==null){
        maxFileNum = 0;
    }       

    var uploadableFileNum = maxFileNum - existFileNum;

    if(uploadableFileNum<0) {
        uploadableFileNum = 0;
    }
                    
    if(uploadableFileNum != 0){
        
        fn_egov_check_file('Y');
        
        var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), uploadableFileNum );
        multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );
        
    }else{
        fn_egov_check_file('N');
    }   
}
/* ********************************************************
 * 목록 으로 가기
 ******************************************************** */
function fn_egov_list_IndvdlSchdulManage(){
    location.href = "<c:url value='/'/>/cop/smt/sim/EgovIndvdlSchdulManageMonthList.do";
}
/* ********************************************************
 * 저장처리화면
 ******************************************************** */
function fn_egov_save_IndvdlSchdulManage(){
	var form = document.getElementById("indvdlSchdulManageVO");
    if(confirm("<spring:message code="common.save.msg" />")){
        if(!validateIndvdlSchdulManageVO(form)){            
            return;
        }else{
            var schdulBgndeYYYMMDD = document.getElementById('schdulBgndeYYYMMDD').value;
            var schdulEnddeYYYMMDD = document.getElementById('schdulEnddeYYYMMDD').value;
            schdulBgndeYYYMMDD = schdulBgndeYYYMMDD.replaceAll('-','');
            schdulEnddeYYYMMDD = schdulEnddeYYYMMDD.replaceAll('-','');
            if(schdulBgndeYYYMMDD > schdulEnddeYYYMMDD) { alert("일정종료일자가  일정시작일자보다 작을수 없습니다"); return false; }
            form.schdulBgnde.value = schdulBgndeYYYMMDD.replaceAll('-','') + fn_egov_SelectBoxValue('schdulBgndeHH') +  fn_egov_SelectBoxValue('schdulBgndeMM') +'00';
            form.schdulEndde.value = schdulEnddeYYYMMDD.replaceAll('-','') + fn_egov_SelectBoxValue('schdulEnddeHH') +  fn_egov_SelectBoxValue('schdulEnddeMM') +'00';

            form.action="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageModifyActor.do"
            form.submit();
        }
    }
}

function fn_egov_check_file(flag) {
    if(flag=="Y") {
        document.getElementById('file_upload_posbl').style.display = "block";
        document.getElementById('file_upload_imposbl').style.display = "none";          
    } else {
        document.getElementById('file_upload_posbl').style.display = "none";
        document.getElementById('file_upload_imposbl').style.display = "block";
    }
}   

/* ********************************************************
* 주관 부서 팝업창열기
******************************************************** */
function fn_egov_schdulDept_DeptSchdulManage(){

    var arrParam = new Array(1);
    arrParam[0] = self;
    arrParam[1] = "typeDeptSchdule";
    
    window.showModalDialog("/uss/olp/mgt/EgovMeetingManageLisAuthorGroupPopup.do", arrParam ,"dialogWidth=800px;dialogHeight=500px;resizable=yes;center=yes");
}


/* ********************************************************
* 아이디  팝업창열기
******************************************************** */
function fn_egov_schdulCharger_DeptSchdulManagee(){
    var arrParam = new Array(1);
    arrParam[0] = window;
    arrParam[1] = "typeDeptSchdule";

    window.showModalDialog("/uss/olp/mgt/EgovMeetingManageLisEmpLyrPopup.do", arrParam,"dialogWidth=800px;dialogHeight=500px;resizable=yes;center=yes");
}

/* ********************************************************
* RADIO BOX VALUE FUNCTION
******************************************************** */
function fn_egov_RadioBoxValue(sbName)
{
    var FLength = document.getElementsByName(sbName).length;
    var FValue = "";
    for(var i=0; i < FLength; i++)
    {
        if(document.getElementsByName(sbName)[i].checked == true){
            FValue = document.getElementsByName(sbName)[i].value;
        }
    }
    return FValue;
}
/* ********************************************************
* SELECT BOX VALUE FUNCTION
******************************************************** */
function fn_egov_SelectBoxValue(sbName)
{
    var FValue = "";
    for(var i=0; i < document.getElementById(sbName).length; i++)
    {
        if(document.getElementById(sbName).options[i].selected == true){
            
            FValue=document.getElementById(sbName).options[i].value;
        }
    }
    
    return  FValue;
}
/* ********************************************************
* PROTOTYPE JS FUNCTION
******************************************************** */
String.prototype.trim = function(){
    return this.replace(/^\s+|\s+$/g, "");
}

String.prototype.replaceAll = function(src, repl){
     var str = this;
     if(src == repl){return str;}
     while(str.indexOf(src) != -1) {
        str = str.replace(src, repl);
     }
     return str;
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
                            <li><strong>일정관리 수정</strong></li>
                        </ul>
                    </div>
                </div>
                
                <!-- 검색 필드 박스 시작 -->
                <div id="search_field">
                    <div id="search_field_loc"><h2><strong>일정관리 수정</strong></h2></div>
                </div>
                <form:form modelAttribute="indvdlSchdulManageVO" action="/cop/smt/sim/EgovIndvdlSchdulManageModifyActor.do" method="post" enctype="multipart/form-data">
                    <div class="modify_user" >
                        <table width="100%" border="0" cellpadding="0" cellspacing="1" class="table-register">
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >일정구분<!--  <img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                                <td width="80%" >
							        <form:select path="schdulSe">
							            <form:option value="" label="선택"/>
							            <form:options items="${schdulSe}" itemValue="code" itemLabel="codeNm"/>
							        </form:select>
							        <div><form:errors path="schdulSe" cssClass="error"/></div>
                                </td>
                            </tr>
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >중요도<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                                <td width="80%" >
							        <form:select path="schdulIpcrCode">
							            <form:option value="" label="선택"/>
							            <form:options items="${schdulIpcrCode}" itemValue="code" itemLabel="codeNm"/>
							        </form:select>
							        <div><form:errors path="schdulIpcrCode" cssClass="error"/></div>
                                </td>
                            </tr>
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >부서<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                                <td width="80%" >
 							            <form:input path="schdulDeptName" size="73" cssClass="txaIpt" readonly="true" maxlength="1000" />
							        <form:hidden path="schdulDeptId" />
							        <div><form:errors path="schdulDeptName" cssClass="error"/></div>
                                </td>
                            </tr>
                            <tr>
                                <th width="20%" height="23" class="required_text" nowrap >일정명<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                                <td width="80%" >
							      <form:input path="schdulNm" size="73" cssClass="txaIpt" maxlength="255" />
							      <div><form:errors path="schdulNm" cssClass="error"/></div>
                                </td>
                            </tr>
                            <tr>
                                <th height="23" class="required_text" >일정 내용<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                                <td>
							        <form:textarea path="schdulCn" rows="3" cols="80" cssClass="txaClass"/>
							        <div><form:errors path="schdulCn" cssClass="error"/></div>
                                </td>
                            </tr>
                            <tr> 
                              <th width="20%" height="23" class="required_text" nowrap >반복구분<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                              <td width="80%">
						       <form:radiobutton path="reptitSeCode" value="1" />당일
						       <form:radiobutton path="reptitSeCode" value="2"/>반복
						       <form:radiobutton path="reptitSeCode" value="3"/>연속
						       <div><form:errors path="reptitSeCode" cssClass="error"/></div>
                              </td>
                            </tr>
                        
                          <tr> 
                            <th width="20%" height="23" class="required_text" nowrap >날짜/시간<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                            <td width="80%" >
							    <form:input path="schdulBgndeYYYMMDD" size="10" readonly="true" maxlength="10" />
							        <a href="#LINK" onClick="javascript:fn_egov_NormalCalendar(document.getElementById('indvdlSchdulManageVO'), document.getElementById('indvdlSchdulManageVO').schdulBgndeYYYMMDD,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>'); return false;">
							    <img src="<c:url value='/images/calendar.gif' />"  align="middle" style="border:0px" alt="일정시작달력" title="일정시작달력">
							    </a>
							    &nbsp&nbsp~&nbsp&nbsp
							    <form:input path="schdulEnddeYYYMMDD" size="10" readonly="true" maxlength="10" />
							        <a href="#LINK" onClick="javascript:fn_egov_NormalCalendar(document.getElementById('indvdlSchdulManageVO'), document.getElementById('indvdlSchdulManageVO').schdulEnddeYYYMMDD,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>'); return false;">
							    <img src="<c:url value='/images/calendar.gif' />" align="middle" style="border:0px" alt="일정종료달력" title="일정종료달력">
							    </a>&nbsp;
							        
							        <form:select path="schdulBgndeHH">
							            <form:options items="${schdulBgndeHH}" itemValue="code" itemLabel="codeNm"/>
							        </form:select>시
							        <form:select path="schdulBgndeMM">
							            <form:options items="${schdulBgndeMM}" itemValue="code" itemLabel="codeNm"/>
							        </form:select>분
							        ~
							        <form:select path="schdulEnddeHH">
							            <form:options items="${schdulEnddeHH}" itemValue="code" itemLabel="codeNm"/>
							        </form:select>시
							        <form:select path="schdulEnddeMM">
							            <form:options items="${schdulEnddeMM}" itemValue="code" itemLabel="codeNm"/>
							        </form:select>분
                            </td>
                          </tr>
                          
                          <tr> 
                            <th width="20%" height="23" class="required_text" nowrap >담당자<!--<img src="${ImgUrl}icon/required.gif" width="15" height="15">--></th>
                            <td width="80%" >
						            <form:input path="schdulChargerName" size="73" cssClass="txaIpt" readonly="true" maxlength="10" />
						         <div><form:errors path="schdulChargerName" cssClass="error"/></div>
						       <form:hidden path="schdulChargerId" />
                            </td>
                          </tr>
                          
						 <!-- 첨부목록을 보여주기 위한 -->  
						  <c:if test="${indvdlSchdulManageVO.atchFileId ne null && indvdlSchdulManageVO.atchFileId ne ''}">
						    <tr> 
						        <th height="23" class="required_text">첨부파일 목록</th>
						        <td>
						            <c:import charEncoding="utf-8" url="/cmm/fms/selectFileInfs.do" >
						                <c:param name="param_atchFileId" value="${indvdlSchdulManageVO.atchFileId}" />
						            </c:import>     
						        </td>
						    </tr>
						  </c:if>   
						 
		 
			
						 <!-- 첨부화일 업로드를 위한 Start -->
						  <tr> 
						    <th height="23" class="required_text">파일첨부</th>
						    <td style="padding:0px 0px 0px 0px;margin:0px 0px 0px 0px;" >
						        <div id="file_upload_posbl"  style="display:none;" >    
						                      <input name="file_1" id="egovComFileUploader" title="파일첨부" type="file"  />
						                        <div id="egovComFileList"></div>
						        </div>
						        <div id="file_upload_imposbl"  style="display:none;" >
						        </div>  
						    </td>       
						  </tr>
						 <!-- 첨부화일 업로드를 위한 end.. -->
                        </table>
                    </div>

	                    <!-- 버튼 시작(상세지정 style로 div에 지정) -->
	                    <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
							<!-- 목록/저장버튼  -->
	                        <table border="0" cellspacing="0" cellpadding="0" align="center">
							<tr> 
							  <td>
							     <a href="<c:url value='/'/>/cop/smt/sim/EgovIndvdlSchdulManageMonthList.do" onclick="JavaScript:fn_egov_list_IndvdlSchdulManage(); return false;"><spring:message code="button.list" /></a> 
							  </td>
							  <td width="10"></td>
							  <td>
							     <a href="#LINK" onclick="JavaScript:fn_egov_save_IndvdlSchdulManage(); return false;"><spring:message code="button.save" /></a> 
							  </td>
							</tr>
							</table>
	                    </div>
	                    <!-- 버튼 끝 -->                           

						  <c:if test="${indvdlSchdulManageVO.atchFileId eq null || indvdlSchdulManageVO.atchFileId eq ''}">
						    <input type="hidden" name="fileListCnt" value="0" />
						    <input name="atchFileAt" type="hidden" value="N">
						  </c:if> 
						
						  <c:if test="${indvdlSchdulManageVO.atchFileId ne null && indvdlSchdulManageVO.atchFileId ne ''}">
						    <input name="atchFileAt" type="hidden" value="Y"> 
						  </c:if> 


						<form:hidden path="schdulId" />
						<form:hidden path="schdulKindCode" />
						<input type="hidden" name="schdulBgnde" id="schdulBgnde" value="" />  
						<input type="hidden" name="schdulEndde" id="schdulEndde" value="" />  
						
						<input type="hidden" name="posblAtchFileNumber" value="3" />  
						<input type="hidden" name="cmd" id="cmd" value="<c:out value='save'/>" />
						<input type="hidden" name="cal_url" id="cal_url" value="<c:url value='/sym/cmm/EgovNormalCalPopup.do'/>" />

            </form:form>
        
        </div>
        </div>
        <!-- //페이지 네비게이션 끝 -->  
        <!-- //content 끝 -->    
    </div>  
    <!-- //container 끝 -->
    <!-- footer 시작 -->
    <div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter" /></div>
    <!-- //footer 끝 -->
<!-- //전체 레이어 끝 -->
</body>
</html>