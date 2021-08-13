<%--
  Class Name : EgovQADetail.jsp
  Description : 샘플화면 - 묻고답하기 상세조회(sample)
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
							<li>묻고답하기</li>
							<li>&gt;</li>
							<li><strong>Q&amp;A상세조회</strong></li>
						</ul>
					</div>
				</div>	
				<!-- //현재위치 네비게이션 끝 -->
				<!-- 타이틀 이미지 -->			
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle03-02.gif" width="776" height="230" alt="묻고/답하기 각종 문의 사항에 대한 답변을 친절하게 제공해 드리고 있습니다."/>					
				</div>
				<!-- main content 시작 -->
				<div class="content_field"><h2>Q&amp;A 상세조회</h2></div> 
				<!-- //main content 끝 -->	
				<div class="download_div03">
					<div class="qatable_wrap" style="">
						<div id="qna_detailtable">
						<table summary="Q&amp;A상세조회" cellpadding="0" cellspacing="0">
							<colgroup> 
								<col width="120">
								<col width="%">
								<col width="120">
								<col width="%">
							</colgroup>
							<tbody>
							<tr>
								<th>제목</th>
								<td>jsp파일을 못찼습니다.</td>
								<th>이메일</th>
								<td>agits@nate.com</td>
							</tr>
							<tr>
								<th>이메일답변여부</th> 
								<td>답변요청</td>
								<th>등록일자</th>
								<td>2011-08-08 11:11:11</td>
							</tr>
							<tr>
								<th>작성자</th>
								<td>박성환</td>
								<th>전 화</th>
								<td>011-000-0000</td>
							</tr>
							<tr>
								<th>작성일</th>
								<td>2011-07-22</td>
								<th>조회</th>
								<td>1222</td>
							</tr>
							<tr>
								<th>처리상태</th>
								<td colspan="3">접수대기</td>
							</tr>
							<tr>
								<th>첨부파일</th>
								<td colspan="3">log.txt [85,320 byte]</td>
							</tr>
							</tbody>
						</table>	
						</div>				
					</div>
				</div>
		        <!-- main content 시작 -->
				<div class="content_field"><h3>질문</h3></div>
				<!-- //main content 끝 -->		
				<div class="qa_1st_wrap">
					<div class="qa_1st_loc">
						<p>안녕하세요 웹호스팅에 올렸더니 jsp파일에서 이런에러로그가 남았는데요 jsp파일을 못찾는것같습니다? xml을 수정해야하나요?</p>
						<p>심각: Servlet.service() for servlet action threw exception</p>
						<p>javax.servlet.ServletException: Could not get RequestDispatcher for [/WEB-INF/jsp/egovframework//main/main.jsp]: check that this file exists within your WAR
						</p>
						<p>	at org.springframework.web.servlet.view.InternalResourceView.renderMergedOutputModel(InternalResourceView.java:217)		
						</p>
					</div>
				</div>	
				
				<div class="qa_answer"> 
					<div>
						<ul>
							<li>chanjin님의 답변</li>
							<li>2011-08-08 12:33:33</li>
							<li><p>심각: Servlet.service() for servlet action threw exception은 jsp파일을 열어서 보셔야합니다.</p>
							<p>javax.servlet.ServletException: Could not get RequestDispatcher for [/WEB-INF/jsp/egovframework//main/main.jsp]: check that this file exists within your WAR</p>
							
							</li>
							<li><div class="qa_btn_delete">삭제하기</div></li>
						</ul>
					</div>
				</div>
				<div class="qa_answer">
					<div>
						<ul>
							<li>sunrise님의 답변</li>
							<li>2011-08-07 11:11:11</li>
							<li style=""><p>tomcat서버를 재시동해보세요. 전 그렇게 하니깐 되던데요.</p></li>
							<li><div class="qa_btn_delete">삭제하기</div></li>
						</ul>
					</div>
				</div>
				<div class="qa_answer"> 
					<div>
						<ul>
							<li style="display:inline;">auto님의 답변</li>
							<li style="display:inline;">2011-08-07 11:11:11</li>
							<li style=";"><p>제가 살펴볼께요 메일로 주세요. test@naver.com</p></li>
							<li><div class="qa_btn_delete">삭제하기</div></li>
						</ul>
					</div>
				</div>
				<div class="qa_write_wrap"> 
					<div class="qa_write_label"><label><strong>답변달기</strong></label>
					<textarea rows="2" cols="80" class="qa_write_txtarea"></textarea>					
					<input type="image" src="images/btn/btn_regist.gif" alt="등록" />
					</div>
					
				</div>
					
				</div>
				
			<!-- //페이지 네비게이션 끝 -->
			</div>				
			<!-- //content 끝 -->
	<!-- //container 끝 -->
	<!-- footer 시작 -->
	<div id="footer"><c:import url="/EgovPageLink.do?link=main/inc/EgovIncFooter" /></div>
	<!-- //footer 끝 -->				
</div>
<!-- //전체 레이어 끝 -->
</body>
</html>