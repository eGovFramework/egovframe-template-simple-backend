<%--
  Class Name : EgovIncTopnav.jsp
  Description : 상단메뉴(include)
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 실행환경개발팀 JJY
    since    : 2011.08.31 
--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import ="egovframework.com.cmm.LoginVO" %>

<script type="text/javascript">
    function fn_main_headPageMove(menuNo, url){
	    document.selectOne.menuNo.value=menuNo;
	    document.selectOne.link.value=url;
	    document.selectOne.action = "<c:url value='/EgovPageLink.do'/>";
	    //alert(document.selectOne.action);
	    document.selectOne.submit();
    }
    function fn_main_headPageAction(menuNo, url){
        document.selectOne.menuNo.value=menuNo;
        document.selectOne.link.value="";
        document.selectOne.action = "<c:url value='/' />"+url;
        document.selectOne.method = "post";
        //alert(document.selectOne.action);
        document.selectOne.submit();
    }
</script>

<!-- topmenu start -->
<form name="selectOne" action="#LINK">
<input name="menuNo" type="hidden" />
<input name="link" type="hidden" />
</form>

<ul>
	<li><a href="#LINK" onclick="javascript:fn_main_headPageMove('11','main/sample_menu/EgovAboutSite')" >사이트소개</a></li>
    <li><a href="#LINK" onclick="javascript:fn_main_headPageMove('21','main/sample_menu/EgovProductInfo')" >정보마당</a></li>
    <li><a href="#LINK" onclick="javascript:fn_main_headPageMove('31','main/sample_menu/EgovDownload')" >고객지원</a></li>
    <li><a href="#LINK" onclick="javascript:fn_main_headPageAction('42','cop/smt/sim/EgovIndvdlSchdulManageWeekList.do')" >알림마당</a></li>
    <%
       LoginVO loginVO = (LoginVO)session.getAttribute("LoginVO"); 
       if(loginVO != null){ 
    %>
    <li><a href="#LINK" onclick="javascript:fn_main_headPageAction('51','cop/smt/sim/EgovIndvdlSchdulManageMonthList.do')" >사이트관리(관리자)</a></li>
    <%
       }
    %>
</ul>

<!-- //topmenu end -->