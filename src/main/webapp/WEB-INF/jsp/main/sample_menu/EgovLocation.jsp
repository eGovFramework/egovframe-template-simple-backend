<%--
  Class Name : EgovLocation.jsp
  Description : 샘플화면 - 찾아오시는길(sample)
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
							<li>사용자관리</li>
							<li>&gt;</li>
							<li><strong>찾아오시는길</strong></li>
						</ul>
					</div>
				</div>				
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle01.gif" width="776" height="230" alt="홈페이지 소개 표준프레임워크 경량환경의 개요와 연력, 조직소개, 표준프레임워크센터의 약도 등의 정보를 제공하고 있습니다."/></div>  
				<!-- main content 시작 -->
				<div class="content_field">
						<div><h2>찾아오시는길</h2></div>
							<h3>무교청사 찾아 오시는 길 </h3>
							<p><img src="<c:url value='/'/>images/img_content/img_egovframelocation.gif" width="656" height="402" alt="무교청사 약도" /></p>
						<div>
							<h3>상세안내 </h3>
							<fieldset><legend>조건정보 영역</legend>
								<ul>
									<li>-<strong>지하철</strong>
										<ul>
											<li>[1호선]1호선 시청역 5번출구 ▶ 시청삼거리에서 좌회전 ▶서울파이낸스빌딩 옆</li>
											<li>[2호선]2호선 을지로입구역 1번출구 ▶ 시청삼거리에서 우회전 ▶ 맥도날드 건너편</li>
											<li>[5호선]5호선 광화문역 5번출구▶ 동아일보사 건너편</li>
										</ul>
									</li>
								</ul>
								<ul>
									<li>-<strong>버스</strong>
										<ul>
											<li>파이낸셜빌딩 앞 서울신문사 정류장 하차. 청계천 방면 우회전, 동아일보사 건너편</li>
											<li>중앙인사위원회 건물 우측 한국정보화진흥원 빌딩 파랑색 간선노선버스와 초록색 지선노선버스인</li>
											<li>150, 162, 402, 0014, 0015, 1011, 1711, 7017, 7020, 7021 이용</li>
										</ul>
									</li>
								</ul>
								<ul>
									<li>-<strong>승용차</strong>
										<ul>
											<li> 건물뒷편 주차장 입구에서 차량용 리프트를 이용하여 지하 주차장에 주차한 후 엘리베이터를     이용하여</li>
											<li> 1F 안내데스크에서 안내를 받아 주십시오.</li>
										</ul>
									</li>
								</ul>
								<ul>
									<li>-<strong>공항에서 오시는 길(KAL LIMOUSINE BUS)</strong>
										<ul>
											<li>승차위치 : 인천공항(동4B, 11A), 코리아나호텔 정문 앞</li>
											<li>인천공항 → 코리아나호텔(첫차 : 05:55 / 간격 : 15~30분 / 막차 : 22:25분 / 소요시간 : 80분)</li>
											<li>코리아나호텔 → 인천공항(첫차 : 05:55 / 간격 : 15~30분 / 막차 : 18:45분 / 소요시간 : 80분)</li>
											<li>김포공항 → 인천공항(첫차 : 05:00 / 간격 : 15~30분 / 막차 : 21:30분 / 소요시간 : 40분)</li>
											<li>요금 : 코리아나호텔 ↔ 인천공항 13,000원 / 김포공항 → 인천공항 6,000원</li>
										</ul>
									</li>
								</ul>
								<ul>
									<li>-<strong>(605번)</strong>
										<ul>
											<li>승차위치 : 광화문 빌딩 앞 좌석버스 정류장</li>
											<li>인천공항 → 코리아나호텔(첫차 : 06:20 / 간격 : 15분 / 막차 : 21:00분 / 소요시간 : 80~90분)</li>
											<li>코리아나호텔 → 인천공항(첫차 : 06:20 / 간격 : 15분 / 막차 : 23:00분 / 소요시간 : 80~90분)</li>
											<li>요금 : 코리아나호텔 → 인천공항 5,500원 </li>
										</ul>
									</li>
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
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>