<%--
  Class Name : EgovServiceInfo.jsp
  Description : 샘플화면 - 대표서비스 상세조회화면(sample)
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
							<li><strong>대표서비스소개</strong></li> 
						</ul>
					</div>
				</div>				
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle02.gif" width="776" height="230" alt="" /></div>   
				<!-- main content 시작 -->
				<div class="content_field">
						<div><h2>대표서비스소개</h2></div>							
							<p>전자정부 표준 프레임워크 실행환경은 5개 서비스 그룹으로 구성되며 34개 서비스를 제공한다. 실행환경 서비스 구조는 아래 그림과 같다. </p>
							<h3>화면처리</h3>
							<fieldset><legend>조건정보 영역</legend>
									<p>화면처리 서비스그룹은 업무처리 서비스와 사용자간의 인터페이스를 담당하는 서비스로 사용자 화면 구성 및 사용자 입력 정보 검증 등의 기능을 지원한다.</p>
									<br/>
									<ul>
										<li>•Ajax Support: Ajax는 대화식 웹 애플리케이션의 제작을 위해 HTML과 CSS, DOM, 자바 스크립트, XML, XSLT 등과 같은 조합을 이용하는 웹 개발 기법으로 Ajax 기능 지원을 위한 Custom Tag Library를 제공한다.
										</li>
										<li>•Internationalization: Internationalization은 다양한 지역과 언어 환경을 지원할 수 있는 서비스로, 서버 설정 및 클라이언트 브라우저 환경에 따라 자동화된 다국어 기능을 제공한다. 
										</li>
										<li>•MVC: MVC 디자인 패턴을 적용하여 사용자 화면을 개발할 수 있도록 MVC 기반 구조를 제공한다.</li>	
										<li>•Security: 웹 응용프로그램 작성 시 발생될 수 있는 웹 보안상의 취약점(XSS, SQL Injection 등)에 대응하기 위한 기능을 제공한다.</li>
										<li>•UI Adaptor: 화면 레이어의 구현 방식에 따라 업무로직 레이어가 변경되는 것을 막기 위해서, 업무처리 Layer에서 사용할 데이터 타입을 정의하고, 화면 레이어에서 사용하는 in/out parameter를 해당 구현 방식에 맞게 변환해주는 기능 제공한다.</li>
									</ul>
							</fieldset>			
							<h3>업무처리</h3>
							<fieldset><legend>조건정보 영역</legend>
								<p>업무처리 서비스는 업무 프로그램의 업무 로직을 담당하는 서비스로 업무 흐름제어, 트랜잭션 관리, 에러 처리 등의 기능을 제공한다.</p>
									<br/>
									<ul>
										<li>•Process Control: 비지니스 로직과 업무 흐름의 분리를 지원하며, XML 등의 외부 설정으로 업무흐름 구성을 제공하고, 미리 정의된 프로세스를 실행하는 기능을 제공한다.</li>
										<li>•Exception Handling: 응용 프로그래밍의 수행 과정에서 발생하는 예외사항(Exception)을 처리하기 위해 표준화된 방법을 제공한다.</li>
									</ul>
							</fieldset>
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