<%--
  Class Name : EgovIndvdlSchdulManageDailyList.jsp
  Description : 일정관리 일별 조회
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2008.03.09    장동한          최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 장동한
    since    : 2009.03.09
   
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="org.egovframe.rte.psl.dataaccess.util.EgovMap"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%!
    public String DateTypeIntForString(int iInput){
        String sOutput = "";
        if(Integer.toString(iInput).length() == 1){
            sOutput = "0" + Integer.toString(iInput);
        }else{
            sOutput = Integer.toString(iInput);
        }
        
       return sOutput;
    }
%>

<%

String sImgUrl = "images/btn/";

int iNowYear = (Integer)request.getAttribute("year");
int iNowMonth = (Integer)request.getAttribute("month");
int iNowDay = (Integer)request.getAttribute("day");


java.util.Calendar cal = java.util.Calendar.getInstance();
//년도/월 셋팅
cal.set(iNowYear, iNowMonth, 1);

int iEndDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
%> 

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<title>일정 일별 목록</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
    <script type="text/javaScript" language="javascript">

    var gOpener = parent.document.all? parent.document.IndvdlSchdulManageVO : parent.document.getElementById("IndvdlSchdulManageVO") ;
    /* ********************************************************
    * 주관 부서 팝업창열기
    ******************************************************** */
    function fn_egov_regist_DeptSchdulManage(sDate){
    
        gOpener.schdulBgnde.value = sDate;
        gOpener.schdulEndde.value = sDate;
        gOpener.action = "<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageRegist.do";
        gOpener.submit();
    }


    /* ********************************************************
    * 주관 부서 팝업창열기
    ******************************************************** */
    function fn_egov_detail_DeptSchdulManage(schdulId){

        location.href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDetail.do?schdulId=" + schdulId;
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
        location.href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear%>&month=<%=iNowMonth%>&day=<%=iNowDay%>&searchCondition=SCHDUL_SE&searchKeyword=" + setValue;
    }

    function fnSchduleSearch(year, month, day) {
        var setValue = document.deptSchdulManageVO.schdulSe.options[document.deptSchdulManageVO.schdulSe.selectedIndex].value; 
        location.href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year="+year+
                      "&month="+month+"&day="+day+"&searchCondition=SCHDUL_SE&searchKeyword=" + setValue;
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
                            <li>알림마당</li>
                            <li>&gt;</li>
                            <li><strong>오늘의 행사</strong></li>
                        </ul>
                    </div>
                </div>
                
                <form name="deptSchdulManageVO" id="deptSchdulManageVO" action="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do" method="post">
                    <input type="submit" id="invisible" class="invisible"/>
                    <DIV id="content2" style="width:712px;">
                    <!-- 날짜 네비게이션  -->
                        <!-- 검색 필드 박스 시작 -->
                        <div id="search_field">
                            <div id="search_field_loc"><h2><strong>일정관리 일별 목록조회</strong></h2></div>
                                <fieldset>
                                <legend>조건정보 영역</legend>
                                <div class="sf_start">
                                    <ul id="search_first_ul">
                                        <li>
									        <select name="schdulSe" title="검색조건" class="select" id="schdulSe" onchange="fnEgovSchdulSe(this.form.schdulSe.options[this.form.schdulSe.selectedIndex].value);">
									           <option selected value=''>-- 전체 --</option>
									            <c:forEach var="result" items="${schdulSe}" varStatus="status">
									                <option value='${result.code}' <c:if test="${searchKeyword == result.code}">selected</c:if>>${result.codeNm}</option>
									            </c:forEach>                                                   
									        </select>    
                                        </li>
                                        <li>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear-1%>&amp;month=<%=iNowMonth%>&amp;day=<%=iNowDay%>"
								               onclick="fnSchduleSearch('<%=iNowYear-1%>','<%=iNowMonth%>','<%=iNowDay%>'); return false;">
								            <img alt="이전년도로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_prev.gif" align="middle" style="margin-right:4px;border:0px;">
								            </a>
								            <%=iNowYear%>년
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear+1%>&amp;month=<%=iNowMonth%>&amp;day=<%=iNowDay%>"
								               onclick="fnSchduleSearch('<%=iNowYear+1%>','<%=iNowMonth%>','<%=iNowDay%>'); return false;">
								            <img alt="다음년도로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_next.gif" align="middle" style="margin-left:4px;border:0px;">
								            </a>&nbsp;&nbsp;&nbsp;
								            <%if(iNowMonth > 0 ){ %>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear%>&amp;month=<%=iNowMonth-1%>&amp;day=<%=iNowDay%>"
								               onclick="fnSchduleSearch('<%=iNowYear%>','<%=iNowMonth-1%>','<%=iNowDay%>'); return false;">
								            <img alt="이전월로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_prev.gif" align="middle" style="margin-right:4px;border:0px;">
								            </a>
								            <%}%>
								            <%=iNowMonth+1%>월
								            <%if(iNowMonth < 11 ){ %>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear%>&amp;month=<%=iNowMonth+1%>&amp;day=<%=iNowDay%>"
								               onclick="fnSchduleSearch('<%=iNowYear%>','<%=iNowMonth+1%>','<%=iNowDay%>'); return false;">
								            <img alt="다음월로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_next.gif" align="middle" style="margin-left:4px;border:0px;">
								            </a> 
								            <%}%>&nbsp;&nbsp;&nbsp;
								            <%if(iNowDay > 0 ){ %>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear%>&amp;month=<%=iNowMonth%>&amp;day=<%=iNowDay-1%>"
								               onclick="fnSchduleSearch('<%=iNowYear%>','<%=iNowMonth%>','<%=iNowDay-1%>'); return false;">
								            <img alt="이전날짜로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_prev.gif" align="middle" style="margin-right:4px;border:0px;">
								            </a>
								            <%}%>
								            <%=iNowDay%>일
								            <%if(iNowDay < iEndDay-1 ){ %>
								            <a href="<c:url value='/'/>cop/smt/sim/EgovIndvdlSchdulManageDailyList.do?year=<%=iNowYear%>&amp;month=<%=iNowMonth%>&amp;day=<%=iNowDay+1%>"
								               onclick="fnSchduleSearch('<%=iNowYear%>','<%=iNowMonth%>','<%=iNowDay+1%>'); return false;">
								            <img alt="다음날짜로 이동" src="<c:url value='/'/><%=sImgUrl %>btn_next.gif" align="middle" style="margin-left:4px;border:0px;">
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
		                <div class="default_tablestyle">
	                    <table cellpadding="0" cellspacing="0">
		                    <caption>일정관리 일별 목록조회 테이블</caption>
		                    <colgroup>
    		                    <col width="20%" >
    		                    <col width="65%" >  
    		                    <col width="15%" >
		                    </colgroup>
		                    <thead>
		                    <tr>
		                        <th scope="col" class="f_field" nowrap="nowrap">시간</th>
		                        <th scope="col" nowrap="nowrap">제목</th>
		                        <th scope="col" nowrap="nowrap">담당자</th>
		                    </tr>
		                    </thead>
		                    <tbody>                 
		 
		                    <!-- loop 시작 -->                                
							<%
							List listResult = (List)request.getAttribute("resultList");
							EgovMap egovMap = new EgovMap();
							if(listResult != null){
							    for(int i=0;i < listResult.size(); i++){
							    egovMap = (EgovMap)listResult.get(i);
							    System.out.println("xxxxxx:"+(String)egovMap.get("schdulBgnde"));
							%>
                            
    					   <tr>  
							    <td nowrap="nowrap">
                                
								    <%
								    out.print("<a href=\"JavaScript:fn_egov_detail_DeptSchdulManage('" + (String)egovMap.get("schdulId") + "')\">");
								    out.print( ((String)egovMap.get("schdulBgnde")).substring(8,10) +"시");
								    out.print( ((String)egovMap.get("schdulBgnde")).substring(10,12) +"분~");
								    out.print( ((String)egovMap.get("schdulEndde")).substring(8,10) +"시");
								    out.print( ((String)egovMap.get("schdulEndde")).substring(10,12) +"분 ");
								    out.println("</a>");
								    %>
							    </td>
							    <td nowrap align="left">
								    <%
								    out.print("<a href=\"JavaScript:fn_egov_detail_DeptSchdulManage('" + (String)egovMap.get("schdulId") + "')\">");
								    out.print((String)egovMap.get("schdulNm"));
								    out.println("</a>");
								    %> 
							    <td nowrap="nowrap"></td>  
						  </tr>
						 <%
						    }
						} 
						%>
                        <%
                            if(listResult.isEmpty()) {
                        %>
                        <tr>  
                            <td colspan="3" nowrap="nowrap">검색된 결과가 없습니다.
                            </td>
                        </tr>
                        
                        <%
						}
                        %>
						</tbody>
				    </table>
                </DIV>
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