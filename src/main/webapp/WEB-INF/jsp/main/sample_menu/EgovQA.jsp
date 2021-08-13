<%--
  Class Name : EgovQA.jsp
  Description : 샘플화면 - 묻고답하기 목록조회(sample)
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
                            <li><strong>묻고/답하기</strong></li>
						</ul>
					</div>
				</div>
				<!-- //현재위치 네비게이션 끝 -->
				<!-- 타이틀 이미지 -->
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle03-02.gif" width="776" height="230" alt="둗고/답하기 각종 문의 사항에 대한 답변을 친절하게 제공해 드리고 있습니다."/></div>
				<!-- main content 시작 -->
				<div class="content_field"><h2>묻고 답하기(Q&amp;A)</h2></div>
				<!-- //main content 끝 -->

				 <!-- 검색 필드 박스 시작 -->
				<div id="search_field">
					<form action="form_action.jsp" method="post">
					  	<fieldset><legend>조건정보 영역</legend>
					  	<div class="sf_start">
					  		<ul id="search_first_ul">
					  			<li>
					  				<div class="search_leftselect">
									<select name="search_select" id="search_select">
									    <option value="0" selected="selected">전체</option>
									    <option value="1">제목</option>
									    <option value="2">제목/내용</option>
									    <option value="3">작성자</option>
									</select>
									</div>
					  			</li>
					  			<li><div class="inputbox_style01"><input type="text" name="st_date" /></div></li>
					  			<li>
				  				    <div class="buttons" style="float:left;padding-left:2px;">
									<a href="#"><img src="<c:url value='/'/>images/img_search.gif" alt="search" />검색 </a>
									</div>
					  			</li>
					  		</ul>
						</div>
						</fieldset>
					</form>
				</div>
				<!-- //검색 필드 박스 끝 -->
 				<div id="page_info"><div id="page_info_align"></div></div>
				<!-- table add start -->
				<div class="default_tablestyle">
					<table summary="사용자목록관리" cellpadding="0" cellspacing="0">
					<caption>사용자목록관리</caption>
					<colgroup>
    					<col width="38" >
    					<col width="500" >
    					<col width="50" >
    					<col width="150" >
    					<col width="%" >
					</colgroup>
					<thead>
					<tr>
						<th scope="col" class="f_field">번호</th>
						<th scope="col">제목</th>
						<th scope="col">작성자</th>
						<th scope="col">조회수</th>
						<th scope="col">등록일</th>
					</tr>
					</thead>
					<tbody>
					<!-- loop 시작 -->
					<tr>
						<td><strong>10</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다(15)</a></td>
						<td>홍길동</td>
						<td>1232</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>9</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">validation 처리 시.패스워드에 대한 메소드를 찾지 못합니다.</a></td>
						<td>홍길동</td>
						<td>111</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>8</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>324</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>7</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>521</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>6</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>755</td>
						<td>2011-04-04</td>
					</tr>
										<tr>
						<td><strong>5</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>324</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>4</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>521</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>3</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>755</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>2</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>521</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>1</strong></td>
						<td class="align_left_text"><a href="<c:url value='EgovPageLink.do?link=main/sample_menu/EgovQADetail'/>">공통컴포넌트 중 모니터링 관련 서비스 실행시 오류가 발생합니다</a></td>
						<td>홍길동</td>
						<td>755</td>
						<td>2011-04-04</td>
					</tr>
					</tbody>
					</table>
				</div>
				<!-- 페이지 네비게이션 시작 -->
				<div id="paging_div">
					<ul class="paging_align">
						<li class="first"><img src="<c:url value='/'/>images/btn/btn_prev.gif" alt="prev" /></li>
						<li><a href="#">1</a></li>
						<li>2</li>
						<li>3</li>
						<li>4</li>
						<li>5</li>
						<li class="first"><img src="<c:url value='/'/>images/btn/btn_next.gif" alt="next" /></li>
					</ul>
				</div>
			</div>
			<!-- //페이지 네비게이션 끝 -->


			</div>
	<!-- //container 끝 -->
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter" /></div>
	<!-- //footer 끝 -->
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>