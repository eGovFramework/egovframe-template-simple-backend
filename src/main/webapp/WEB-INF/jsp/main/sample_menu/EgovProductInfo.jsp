<%--
  Class Name : EgovProductInfo.jsp
  Description : 샘플화면 - 대표제품 소개페이지(sample)
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 실행환경개발팀 JJY
    since    : 2011.08.31 
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="ko" >
<title>표준프레임워크 경량환경 홈페이지템플릿</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
</head>
<body>
<noscript>자바스크립트를 지원하지 않는 브라우저에서는 일부 기능을 사용하실 수 없습니다.</noscript>	
<!-- 전체 레이어 시작 -->
<div id="wrap">
	<!-- header 시작 -->
    <div id="header_mainsize">
        <c:import url="/EgovPageLink.do?link=main/inc/EgovIncHeader" />
    </div>
    <div id="topnavi">
        <c:import url="/EgovPageLink.do?link=main/inc/EgovIncTopnav" />
    </div>
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
							<li>정보마당</li>
							<li>&gt;</li>
							<li><strong>주요사업소개</strong></li> 
						</ul>
					</div> 
				</div>				
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle02.gif" width="776" height="230" alt="" /></div>   
				<!-- main content 시작 -->
				<div class="content_field">
						<div><h2>주요사업소개</h2></div>
							<h3>구성</h3>
							<fieldset><legend>조건정보 영역</legend>
									<p>전자정부 표준프레임워크는 실행환경, 개발환경, 관리환경, 운영환경 등 4개의 표준프레임워크 환경과 공통컴포넌트로 구성된다. 
									        전자정부 및 공공사업에서 웹 어플리케이션 시스템 구축 시 어플리케이션 아키텍처와 기본 기능 및 공통컴포넌트를 표준으로 제공하는 
									        개발프레임워크로서 다음과 같이 실행, 개발, 운영, 관리환경과 공통컴포넌트로 구성됨</p>
							</fieldset>			
							<h3>실행 환경</h3>
							<fieldset><legend>조건정보 영역</legend>
									<p>전자정부 사업에서 개발하는 업무 프로그램의 실행에 필요한 공통모듈 등  업무 프로그램 개발 시 화면,서버 프로그램, 
									        데이터 개발을 표준화가 용이하도록 지원하는 응용프로그램 환경</p>
							</fieldset>	
							<div style="clear:both;padding-left:110px;"><img src="<c:url value='/'/>images/sample/egov_intro.jpg" alt="그림테스트" />	</div>
						</div>
				</div>
				<!-- //main content 끝 -->	
	</div>	
	<!-- //container 끝 -->
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter" /></div>
	<!-- //footer 끝 -->
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>