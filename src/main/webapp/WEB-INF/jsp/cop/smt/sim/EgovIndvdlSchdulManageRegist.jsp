<%--
  Class Name : EgovIndvdlSchdulManageRegist.jsp
  Description : 일정관리 등록 페이지
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2008.03.09    장동한          최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 장동한
    since    : 2009.03.09
   
--%>
<%@ page contentType="text/html; charset=utf-8"%>
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
<title>일정 등록</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>

<meta http-equiv="Content-Language" content="ko" >
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >

<script type="text/javascript" src="<c:url value='/js/EgovCalPopup.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/EgovMultiFile.js'/>" ></script>

<script type="text/javascript" src="<c:url value="/validator.do"/>"></script>
<validator:javascript formName="indvdlSchdulManageVO" staticJavascript="false" xhtml="true" cdata="false"/>

<script type="text/javaScript" language="javascript">


/* ********************************************************
 * 초기화
 ******************************************************** */
 function fn_egov_init_IndvdlSchdulManage(){

     var maxFileNum = document.getElementById('posblAtchFileNumber').value;
     
     if(maxFileNum==null || maxFileNum==""){
          maxFileNum = 3;
      }
          
     var multi_selector = new MultiSelector( document.getElementById( 'egovComFileList' ), maxFileNum );
     
     multi_selector.addElement( document.getElementById( 'egovComFileUploader' ) );


     document.getElementsByName('reptitSeCode')[0].checked = true;


     if("${indvdlSchdulManageVO.schdulBgnde}".length > 0){
         var schdulBgnde = "${indvdlSchdulManageVO.schdulBgnde}";
         document.getElementById("schdulBgndeYYYMMDD").value = schdulBgnde.substring(0,4) + "-" + schdulBgnde.substring(4,6) + "-" + schdulBgnde.substring(6,8);
     }

     if("${indvdlSchdulManageVO.schdulEndde}".length > 0){
         var schdulEndde = "${indvdlSchdulManageVO.schdulEndde}";
         document.getElementById("schdulEnddeYYYMMDD").value = schdulEndde.substring(0,4) + "-" + schdulEndde.substring(4,6) + "-" + schdulEndde.substring(6,8);
     }
}
/* ********************************************************
* 목록 으로 가기
******************************************************** */
function fn_egov_list_IndvdlSchdulManage(){
  location.href = "${pageContext.request.contextPath}/cop/smt/sim/EgovIndvdlSchdulManageMonthList.do";
}
/* ********************************************************
* 저장처리화면
******************************************************** */
function fn_egov_save_IndvdlSchdulManage(){
  //form.submit();return;
  var form = document.getElementById("indvdlSchdulManageVO");
  if(confirm("<spring:message code="common.save.msg" />")){
      if(!validateIndvdlSchdulManageVO(document.indvdlSchdulManageVO)){  
          return;
      }else{
          var schdulBgndeYYYMMDD = document.getElementById('schdulBgndeYYYMMDD').value;
          var schdulEnddeYYYMMDD = document.getElementById('schdulEnddeYYYMMDD').value;
          schdulBgndeYYYMMDD = schdulBgndeYYYMMDD.replaceAll('-','');
          schdulEnddeYYYMMDD = schdulEnddeYYYMMDD.replaceAll('-','');
          if(schdulBgndeYYYMMDD > schdulEnddeYYYMMDD) { alert("일정종료일자가  일정시작일자보다 작을수 없습니다"); return false; }
          form.schdulBgnde.value = schdulBgndeYYYMMDD.replaceAll('-','') + fn_egov_SelectBoxValue('schdulBgndeHH') +  fn_egov_SelectBoxValue('schdulBgndeMM') + '00';
          form.schdulEndde.value = schdulEnddeYYYMMDD.replaceAll('-','') + fn_egov_SelectBoxValue('schdulEnddeHH') +  fn_egov_SelectBoxValue('schdulEnddeMM') + '00';
          form.submit();
      }
  }
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
<body onLoad="fn_egov_init_IndvdlSchdulManage()">
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
                            <li><strong>일정관리 등록</strong></li>
                        </ul>
                    </div>
                </div>
                <!-- 검색 필드 박스 시작 -->
                <div id="search_field">
                    <div id="search_field_loc"><h2><strong>일정관리 등록</strong></h2></div>
                </div>
                <form:form modelAttribute="indvdlSchdulManageVO" action="${pageContext.request.contextPath}/cop/smt/sim/EgovIndvdlSchdulManageRegistActor.do" name="indvdlSchdulManageVO" method="post" enctype="multipart/form-data">
                    <div class="modify_user" >
                        <table>
                            <tr>
                                <th width="20%" height="23" class="required_text"  >일정구분<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                                <td width="80%" >
                                    <form:select path="schdulSe">
                                        <form:option value="" label="선택"/>
                                        <form:options items="${schdulSe}" itemValue="code" itemLabel="codeNm"/>
                                    </form:select>
                                    <form:errors path="schdulSe" cssClass="error"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="20%" height="23" class="required_text"  >중요도<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                                <td width="80%" >
                                    <form:select path="schdulIpcrCode">
                                        <form:option value="" label="선택"/>
                                        <form:options items="${schdulIpcrCode}" itemValue="code" itemLabel="codeNm"/>
                                    </form:select>
                                    <form:errors path="schdulIpcrCode" cssClass="error"/>
                                </td>
                            </tr>
                            <tr>
                                <th width="20%" height="23" class="required_text"  >부서<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                                <td width="80%" >
                                    <form:input path="schdulDeptName" size="73" cssClass="txaIpt" readonly="true" maxlength="1000" />
                                    <form:hidden path="schdulDeptId" />
                                    <form:errors path="schdulDeptName" cssClass="error"/>
                               </td>
                            </tr>
                            <tr>
                                <th width="20%" height="23" class="required_text"  >일정명<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                                <td width="80%" >
                                    <form:input path="schdulNm" size="73" cssClass="txaIpt"  />
                                    <form:errors path="schdulNm" cssClass="error"/>
                                </td>
                            </tr>
                            <tr>
                                <th height="23" class="required_text" >일정 내용<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                                <td>
                                    <form:textarea path="schdulCn" rows="3" cols="80" />
                                    <form:errors path="schdulCn" cssClass="error"/>
                                </td>
                            </tr>
                            <tr> 
                              <th width="20%" height="23" class="required_text"  >반복구분<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                              <td width="80%">
                                  <form:radiobutton path="reptitSeCode" value="1" />당일
                                  <form:radiobutton path="reptitSeCode" value="2"/>반복
                                  <form:radiobutton path="reptitSeCode" value="3"/>연속
                                  <form:errors path="reptitSeCode" cssClass="error"/>
                              </td>
                            </tr>
                        
                          <tr> 
                            <th width="20%" height="23" class="required_text"  >날짜/시간<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                            <td width="80%" >
                                <form:input path="schdulBgndeYYYMMDD" size="11" readonly="true" maxlength="10" />
                                <a href="#LINK" onClick="javascript:fn_egov_NormalCalendar(document.indvdlSchdulManageVO, document.indvdlSchdulManageVO.schdulBgndeYYYMMDD,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');">
                                <img src="<c:url value='/images/calendar.gif' />"  align="middle" style="border:0px" alt="일정시작달력" title="일정시작달력">
                                </a>
                                &nbsp;&nbsp;~&nbsp;&nbsp;
                                <form:input path="schdulEnddeYYYMMDD" size="11" readonly="true" maxlength="10" />
                                <a href="#LINK" onClick="javascript:fn_egov_NormalCalendar(document.indvdlSchdulManageVO, document.indvdlSchdulManageVO.schdulEnddeYYYMMDD,'','<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>');">
                                <img src="<c:url value='/images/calendar.gif' />" align="middle" style="border:0px" alt="일정종료달력" title="일정종료달력">
                                </a>&nbsp;&nbsp;
                                    
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
                            <th width="20%" height="23" class="required_text"  >담당자<img alt="required" src="<c:url value="/images/required.gif"/>" width="15" height="15" ></th>
                            <td width="80%" >
                                <form:input path="schdulChargerName" size="73" cssClass="txaIpt" readonly="true" maxlength="10" />
                                <form:errors path="schdulChargerName" cssClass="error"/>
                                <form:hidden path="schdulChargerId" />
                            </td>
                          </tr>
                          
                        <!-- 첨부파일 테이블 레이아웃 설정 Start.. -->
                          <tr>
                            <th height="23" class="required_text" >파일첨부</th>
                            <td>
                                           <input name="file_1" id="egovComFileUploader" title="파일첨부" type="file" />
                                           <div id="egovComFileList"></div>
                             </td>
                          </tr>
                        <!-- 첨부파일 테이블 레이아웃 End. -->

                        </table>
                    </div>
                    <!-- 버튼 시작(상세지정 style로 div에 지정) -->
                    <div class="buttons" style="padding-top:10px;padding-bottom:10px;">
                        <!-- 목록/저장버튼  -->
                        <table border="0" cellspacing="0" cellpadding="0" align="center">
                        <tr> 
                          <td>
                              <a href="${pageContext.request.contextPath}/cop/smt/sim/EgovIndvdlSchdulManageMonthList.do" onclick="JavaScript:fn_egov_list_IndvdlSchdulManage(); return false;"><spring:message code="button.list" /></a> 
                          </td>
                          <td>
                              <a href="#LINK" onclick="JavaScript:fn_egov_save_IndvdlSchdulManage();"><spring:message code="button.save" /></a> 
                          </td>  
                        </tr>
                        </table>
                    </div>
                    <!-- 버튼 끝 -->                           
                    <input name="cmd" id="cmd"type="hidden" value="<c:out value='save'/>"/>
                    <input type="hidden" name="schdulKindCode" id="schdulKindCode" value="2" />
                    <input type="hidden" name="cal_url" id="cal_url" value="<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>" />
                    <input type="hidden" name="schdulBgnde" id="schdulBgnde" value="" />  
                    <input type="hidden" name="schdulEndde" id="schdulEndde" value="" /> 
                    <!-- 첨부파일 갯수를 위한 hidden -->
                    <input type="hidden" name="posblAtchFileNumber" id="posblAtchFileNumber" value="3" />
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

