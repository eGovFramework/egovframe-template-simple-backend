<%--
  Class Name : EgovIndvdlSchdulManageMonthList.jsp
  Description : 일정관리 월별 조회
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
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="org.egovframe.rte.psl.dataaccess.util.EgovMap"%>
<%
java.util.Calendar cal = java.util.Calendar.getInstance();
String sImgUrl = "images/btn/";

String strYear = request.getParameter("year");
String strMonth = request.getParameter("month");

int year = cal.get(java.util.Calendar.YEAR);
int month = cal.get(java.util.Calendar.MONTH);
int date = cal.get(java.util.Calendar.DATE);

if(strYear != null)
{
  year = Integer.parseInt(strYear);
  month = Integer.parseInt(strMonth);
}else{
    
}
//년도/월 셋팅
cal.set(year, month, 1);

int startDay = cal.getMinimum(java.util.Calendar.DATE);
int endDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
int start = cal.get(java.util.Calendar.DAY_OF_WEEK);
int newLine = 0;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<title>일정 월별 목록</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >

    <script type="text/javaScript" language="javascript">

    var gOpener = parent.document.all? parent.document.IndvdlSchdulManageVO : parent.document.getElementById("IndvdlSchdulManageVO") ;
    /* ********************************************************
    * 주관 부서 팝업창열기
    ******************************************************** */
    function fn_egov_regist_IndvdlSchdulManage(sDate){
    
        gOpener.schdulBgnde.value = sDate;
        gOpener.schdulEndde.value = sDate;
        gOpener.action = '/cop/smt/sim/EgovIndvdlSchdulManageRegist.do';
        gOpener.submit();
    }


    /* ********************************************************
    * 주관 부서 팝업창열기
    ******************************************************** */
    function fn_egov_detail_IndvdlSchdulManage(schdulId){

        gOpener.schdulId.value = schdulId;
        gOpener.action = '/cop/smt/sim/EgovIndvdlSchdulManageDetail.do';
        gOpener.submit();
    }

    
    var ifr= parent.document.all? parent.document.all.SchdulView : parent.document.getElementById("SchdulView") ;

    function do_resize() {
        resizeFrame(1);
    }

    //가로길이는 유동적인 경우가 드물기 때문에 주석처리!
    function resizeFrame(re) {

        if(ifr){

            var innerHeight = document.body.scrollHeight + (document.body.offsetHeight - document.body.clientHeight);
                
            if(ifr.style.height != innerHeight) //주석제거시 다음 구문으로 교체 -> if (ifr.style.height != innerHeight || ifr.style.width != innerWidth)
            {ifr.style.height = innerHeight;}
        }
    }

    function fnEgovSchdulSe(setValue) {

        location.href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year=<%=year%>&month=<%=month%>&searchCondition=SCHDUL_SE&searchKeyword=" + setValue;

    }
    function fnSchduleSearch(year, month) {
        var setValue = document.IndvdlSchdulManageVO.schdulSe.options[document.IndvdlSchdulManageVO.schdulSe.selectedIndex].value; 
    	location.href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year="+year+
    	              "&month="+month+"&searchCondition=SCHDUL_SE&searchKeyword=" + setValue;
    }
    window.onload = function(){
        do_resize();
    }
    </script>
    <style TYPE="text/css"> 
        body { 
        scrollbar-face-color: #F6F6F6; 
        scrollbar-highlight-color: #bbbbbb; 
        scrollbar-3dlight-color: #FFFFFF; 
        scrollbar-shadow-color: #bbbbbb; 
        scrollbar-darkshadow-color: #FFFFFF; 
        scrollbar-track-color: #FFFFFF; 
        scrollbar-arrow-color: #bbbbbb;
        margin-left:"0px"; margin-right:"0px"; margin-top:"0px"; margin-bottom:"0px";
        }
        
        td {font-family: "돋움"; font-size: 9pt; color:#595959;}
        th {font-family: "돋움"; font-size: 9pt; color:#000000;}
        select {font-family: "돋움"; font-size: 9pt; color:#595959;}
        
        
        .divDotText {
        overflow:hidden; 
        text-overflow:ellipsis;
        }
        
A:link { font-size:9pt; font-family:"돋움";color:#000000; text-decoration:none; }
A:visited { font-size:9pt; font-family:"돋움";color:#000000; text-decoration:none; }
A:active { font-size:9pt; font-family:"돋움";color:red; text-decoration:none; }
A:hover { font-size:9pt; font-family:"돋움";color:red;text-decoration:none;}


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
							<li><strong>일정관리</strong></li>
						</ul>
					</div>
				</div>
                <form name="IndvdlSchdulManageVO" id="IndvdlSchdulManageVO" action="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do" method="post">
                <input type="submit" id="invisible" class="invisible"/>
	                <DIV id="content2" style="width:712px;">
	                <!-- 날짜 네비게이션  -->
						<!-- 검색 필드 박스 시작 -->
						<div id="search_field">
							<div id="search_field_loc"><h2><strong>일정관리 월별 목록조회</strong></h2></div>
							  	<fieldset><legend>조건정보 영역</legend>	  
							  	<div class="sf_start">
							  		<ul id="search_first_ul">
							  			<li>
									        <select name="schdulSe" title="검색조건" class="select" id="schdulSe" onChange="fnEgovSchdulSe(this.form.schdulSe.options[this.form.schdulSe.selectedIndex].value);">
									           <option selected value=''>-- 전체 --</option>
									            <c:forEach var="result" items="${schdulSe}" varStatus="status">
									                <option value='${result.code}' <c:if test="${searchKeyword == result.code}">selected</c:if>>${result.codeNm}</option>
									            </c:forEach>                                                   
									        </select>    
							  			</li>
							  			<li>

								            <%--<a href="#" onClick="location.href='/cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year=<%=year-1%>&amp;month=<%=month%>'"> << </a>--%>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year=<%=year-1%>&amp;month=<%=month%>" 
								               onclick="fnSchduleSearch('<%=year-1%>', '<%=month%>'); return false;"
								            > 
								            <img alt="이전년도로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_prev.gif" align="middle" style="margin-right:4px;border:0px;">
								            </a>
								            <%=year%>년
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year=<%=year+1%>&amp;month=<%=month%>" 
								               onclick="fnSchduleSearch('<%=year+1%>', '<%=month%>'); return false;"
								            > 
								            <img alt="다음년도로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_next.gif" align="middle" style="margin-left:4px;border:0px;">
								            </a>
								            <%if(month > 0 ){ %>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year=<%=year%>&amp;month=<%=month-1%>" 
								               onclick="fnSchduleSearch('<%=year%>', '<%=month-1%>'); return false;"
								            > 
                                            <img alt="이전월로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_prev.gif" align="middle" style="margin-right:4px;border:0px;">
                                            </a>
								            <%}%>
								            <%=month+1%>월
								            <%if(month < 11 ){ %>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageMonthList.do?year=<%=year%>&amp;month=<%=month+1%>" 
								                onclick="fnSchduleSearch('<%=year%>', '<%=month+1%>'); return false;"
								            > 
                                            <img alt="다음월로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_next.gif" align="middle" style="margin-left:4px;border:0px;">
                                            </a>
								            <%}%>
							  			</li>		
							  		</ul>
								</div>
								</fieldset>
						</div>
						<!-- //검색 필드 박스 끝 -->
					    
					    <div id="page_info"></div>					
					    <!-- table add start -->
						<table border="0" cellspacing="1" cellpadding="1" bgcolor="#FFFFFF">
						<THEAD>
						<TR bgcolor="#CECECE">
						    <TD width='100' height='23'>
						    <DIV align="center"><font color="red">일</font></DIV>
						    </TD>
						    <TD width='100'>
						    <DIV align="center">월</DIV>
						    </TD>
						    <TD width='100'>
						    <DIV align="center">화</DIV>
						    </TD>
						    <TD width='100'>
						    <DIV align="center">수</DIV>
						    </TD>
						    <TD width='100'>
						    <DIV align="center">목</DIV>
						    </TD>
						    <TD width='100'>
						    <DIV align="center">금</DIV>
						    </TD>
						    <TD width='100'>
						    <DIV align="center"><font color="#529dbc">토</font></DIV>
						    </TD>
						</TR>
						</THEAD>
						<TBODY>
						<TR>
						<%
						
						List listResult = (List)request.getAttribute("resultList");
						EgovMap egovMap = new EgovMap();
						//처음 빈공란 표시
						for(int index = 1; index < start ; index++ ) {
						  out.println("<TD >&nbsp;</TD>");
						  newLine++;
						}
						
						for(int index = 1; index <= endDay; index++) {
						    String color = "";
						    
						    if(newLine == 0){           color = "RED";
						    }else if(newLine == 6){     color = "#529dbc";
						    }else{                      color = "BLACK"; };
						    
						    String sUseDate = Integer.toString(year);
						    
						    sUseDate += Integer.toString(month+1).length() == 1 ? "0" + Integer.toString(month+1) : Integer.toString(month+1);  
						    sUseDate += Integer.toString(index).length() == 1 ? "0" + Integer.toString(index) : Integer.toString(index);
						    
						    int iUseDate = Integer.parseInt(sUseDate);
						
						    out.println("<TD valign='top' align='left' height='92' bgcolor='#EFEFEF' >");
						    out.println("<font color='"+color+"'><a href=\"EgovIndvdlSchdulManageRegist.do?schdulBgnde="+iUseDate+"&amp;schdulEndde="+iUseDate+"\" target=\"_top\">"+index+"</a></font>");
						    out.println("<BR>");
						    
						    if(listResult != null){
						
						        for(int i=0;i < listResult.size(); i++){
						            egovMap = (EgovMap)listResult.get(i);
						            
						            int iBeginDate = Integer.parseInt(((String)egovMap.get("schdulBgnde")).substring(0, 8));
						            int iBeginEnd = Integer.parseInt(((String)egovMap.get("schdulEndde")).substring(0, 8));
						            
						            if(iUseDate >= iBeginDate && iUseDate <= iBeginEnd){
						                out.print("<table><tr><td nowrap><div class='divDotText' style='width:92px;border:solid 0px;'>");
						                out.println("<a href=\"EgovIndvdlSchdulManageDetail.do?schdulId="+(String)egovMap.get("schdulId")+"\" target=\"_top\">");
						                out.print((String)egovMap.get("schdulNm"));
						                out.println("</a></div></td></tr></table>");
						            }
						    
						    
						        }
						    }
						
						    out.println("</TD>");
						    newLine++;
						    
						    if(newLine == 7)
						    {
						      out.println("</TR>");
						      if(index <= endDay)
						      {
						        out.println("<TR>");
						      }
						      newLine=0;
						    }
						}
						//마지막 공란 LOOP
						while(newLine > 0 && newLine < 7)
						{
						  out.println("<TD>&nbsp;</TD>");
						  newLine++;
						}
						%>
                        <td></td>
						</TR> 
						
						</TBODY>
						</TABLE>
				</DIV>
			</form>
		</div>
		<!-- //페이지 네비게이션 끝 -->	
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