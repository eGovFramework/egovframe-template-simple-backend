<%--
  Class Name : EgovNormalCalendar.jsp
  Description : EgovNormalCalendar 화면(system)
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.04.01   이중호              최초 생성
     2011.08.31  JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이중호
    since    : 2009.04.01
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>일반달력</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta http-equiv="content-language" content="ko">
<base target="_self">
<link type="text/css" rel="stylesheet" href="<c:url value="/css/cal.css"/>" />
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<script type="text/javascript" src="<c:url value='/js/showModalDialogCallee.js'/>" ></script>
<script type="text/javaScript" language="javascript">
<!--

/* ********************************************************
 * 초기화
 ******************************************************** */
function fnInit(){
	getDialogArguments();
	var varParam        = window.dialogArguments;
	var varForm			= document.all["Form"];
	var pForm			= parent.document.all["pForm"];
	if (varParam.sDate) {
		var sDate = varParam.sDate;
		if(sDate.length == 10) {
			if(pForm.init.value != "OK") {
				pForm.init.value  = "OK";
				varForm.action      = "<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>";
				varForm.year.value  = sDate.substr(0,4);
				varForm.month.value = sDate.substr(5,2);
				varForm.submit();
			}
		}
	}
}

/* ********************************************************
 * 연월변경
 ******************************************************** */
function fnChangeCalendar(year, month){
	var varForm			= document.all["Form"];
	varForm.action      = "<c:url value='/sym/cmm/EgovselectNormalCalendar.do'/>";
	varForm.year.value  = year;
	varForm.month.value = month;
	varForm.submit();
}

/* ********************************************************
 * 결과연월일 반환 
 ******************************************************** */
function fnReturnDay(day){
	var retVal   = new Object();
	var sYear    = "0000"+document.Form.year.value;
	var sMonth   = "00"+document.Form.month.value;
	var sDay     = "00"+day;
	retVal.year  = sYear.substr(sYear.length-4,4);
	retVal.month = sMonth.substr(sMonth.length-2,2);
	retVal.day   = sDay.substr(sDay.length-2,2);
	retVal.sDate = retVal.year + retVal.month + retVal.day;
	retVal.vDate = retVal.year + "-" + retVal.month + "-" + retVal.day;
	
    setReturnValue(retVal);
	
	parent.window.returnValue = retVal;
	parent.window.close();
}	
//-->	
</script>
</head>
<body topmargin="0" leftmargin="0">
<form name="Form" action ="${pageContext.request.contextPath}/sym/cmm/EgovselectNormalCalendar.do" method="post">
<input type="hidden" name="init" value="${init}" />
<input type="hidden" name="year" value="${resultList[0].year}" />
<input type="hidden" name="month" value="${resultList[0].month}" />
<input type="hidden" name="day" />
	<table cellpadding="1" class="table-line">
	 <thead>
	  <tr>
	    <th class="title" width="36"  colspan="1">
	    	<a href="#LINK" onclick="javascript:fnChangeCalendar(${resultList[0].year-1},${resultList[0].month}); return false;"  style="selector-dummy:expression(this.hideFocus=false);cursor:pointer;cursor:hand;"><img src="<c:url value='/images/btn/icon_pre_year.gif' />" alt="이전년도"></a>
	    </th>
	    <th class="title" width="36"  colspan="1">
	    	<a href="#LINK" onclick="javascript:fnChangeCalendar(${resultList[0].year},${resultList[0].month-1}); return false;"  style="selector-dummy:expression(this.hideFocus=false);cursor:pointer;cursor:hand;"><img src="<c:url value='/images/btn/icon_pre_month.gif' />" alt="이전달"></a>
	    </th>
	    <th class="title" width="108"  colspan="3">${resultList[0].year}년${resultList[0].month}월</th>
	    <th class="title" width="36"  colspan="1">
	    	<a href="#LINK" onclick="javascript:fnChangeCalendar(${resultList[0].year},${resultList[0].month+1}); return false;"  style="selector-dummy:expression(this.hideFocus=false);cursor:pointer;cursor:hand;"><img src="<c:url value='/images/btn/icon_aft_month.gif' />" alt="다음달"></a>
	    </th>
	    <th class="title" width="36"  colspan="1">
	    	<a href="#LINK" onclick="javascript:fnChangeCalendar(${resultList[0].year+1},${resultList[0].month}); return false;"  style="selector-dummy:expression(this.hideFocus=false);cursor:pointer;cursor:hand;"><img src="<c:url value='/images/btn/icon_aft_year.gif' />" alt="다음년도"></a>
	    </th>
	  </tr>
	  <tr>
	    <th class="title" width="36" >일</th>
	    <th class="title" width="36" >월</th>
	    <th class="title" width="36" >화</th>
	    <th class="title" width="36" >수</th>
	    <th class="title" width="36" >목</th>   
	    <th class="title" width="36" >금</th>
	    <th class="title" width="36" >토</th>         
	  </tr>
	 </thead>    
	 <tbody>
		<tr>
	 		<c:forEach var="result" items="${resultList}" varStatus="status">
				<c:choose>
				<c:when test='${result.day == ""}'>
			 		<c:choose>
			 		<c:when test='${result.weeks != 6}'>
						<td></td>
					</c:when>
					</c:choose>
				</c:when>
				<c:otherwise>
			 		<c:choose>
			 		<c:when test='${result.restAt == "Y" }'>
					    <td class="lt_text3"  STYLE="color:red;cursor:pointer;cursor:hand" onClick="javascript:fnReturnDay(${result.day});">
					    	${result.day}
					    </td>
					</c:when>
					<c:otherwise>
					    <td class="lt_text3"  STYLE="color:black;cursor:pointer;cursor:hand" onClick="javascript:fnReturnDay(${result.day});">
					    	${result.day}
					    </td>
					</c:otherwise>
					</c:choose>
			 		<c:choose>
			 		<c:when test='${result.week == 7}'>
					    <c:out value="</tr>" escapeXml="false"/>
					    <c:out value="<tr>" escapeXml="false"/>
					</c:when>
					</c:choose>
				</c:otherwise>
				</c:choose>
			</c:forEach>	
		</tr>
	 </tbody>  
	</table>
<input type="submit" id="invisible" class="invisible"/>	
</form>
</body>
</html>