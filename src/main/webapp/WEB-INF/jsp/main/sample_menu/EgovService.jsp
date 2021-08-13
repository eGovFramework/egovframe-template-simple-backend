<%--
  Class Name : EgovService.jsp
  Description : 샘플화면 - 대표서비스 조회화면(sample)
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
			<!-- content 시작 -->			
			<div id="content">
				<!-- 현재위치 네비게이션 시작 -->
				<div id="cur_loc">
					<div id="cur_loc_align">
						<ul>
                            <li>HOME</li>
                            <li>&gt;</li>
                            <li>고객지원</li>
                            <li>&gt;</li>
                            <li><strong>서비스신청</strong></li>
						</ul>
					</div>
				</div>	
				<!-- //현재위치 네비게이션 끝 -->
				<!-- 타이틀 이미지 -->			
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle03-03.gif" width="776" height="230" alt="서비스 신청 표준프레임워크 경량환경의 원하시는 서비스를 신청을 하실 수 있습니다."/></div>
				<div class="content_field">
						<div><h2>서비스 신청</h2></div>
							<h3>주요서비스안내</h3>
							<fieldset><legend>조건정보 영역</legend>
									<p>서비스필요시 다음과 같은 절차로 신청하시면 됩니다.</p>
									<p>1. 필요한 서비스 확인</p>
									<p>2. 자료실에서 필요한 서비스 존재여부 확인</p>
									<p>3. 서비스요청을 통해 필요한 서비스 신청</p>
									<a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovServiceInfo'/>">대표 서비스 자세히 보기</a>
							</fieldset>			
						
						</div>
						
				</div>				
					
			</div>
			<!-- //페이지 네비게이션 끝 -->				
			<!-- //content 끝 -->
	<!-- //container 끝 -->
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter" /></div>
	<!-- //footer 끝 -->				
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>