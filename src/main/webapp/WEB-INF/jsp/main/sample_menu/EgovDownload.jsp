<%--
  Class Name : EgovAboutSite.jsp
  Description : 샘플화면 - 자료실목록조회(sample)
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
							<li><strong>자료실</strong></li>
						</ul>
					</div>
				</div>	
				<!-- //현재위치 네비게이션 끝 -->
				<!-- 타이틀 이미지 -->			
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle03-01.gif" width="776" height="230" alt="자료실 전자정부표준프레임워크 경량환경 페이지의 다양한 종류의 쟈료를 다운 받으실 수 있습니다."/></div>

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
					  			<li><div class="buttons" style="float:left;padding-left:2px;">
									<a href="#"><img src="<c:url value='/'/>images/img_search.gif" alt="search" />검색 </a>
									</div></li>		
					  		</ul>		
						</div>			
						</fieldset>
					</form>
				</div>
				<!-- //검색 필드 박스 끝 -->		
				<!-- 추천다운로드 시작 -->
				<div id="download_div01">
					<h3>추천 다운로드 자료</h3>
					<div class="download_loc">
						<div class="download_content_top_wrap">
							<div class="download_img_loc">
								<img src="<c:url value='/'/>images/sample/img_download.gif" alt="다운로드 자료 이미지"/>
							</div>
							<div class="download_text_loc">
								<ul>
									<li><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadDetail'/>">egovframe installer v1.03</a></li>
									<li>egovframe의 템플릿 설치를 도와주는 인스톨러.....egovframe의 템플릿 설치를 도와주는 인스톨러</li>
								</ul>	
							</div>
						</div>
						<div class="download_content_btm_wrap">
							<div class="download_img_loc">
								<img src="<c:url value='/'/>images/sample/img_download.gif" alt="다운로드 자료 이미지"/>
							</div>
							<div class="download_text_loc">
								<ul>
									<li>egovframe installer v1.03</li>
									<li>egovframe의 템플릿 설치를 도와주는 인스톨러.....egovframe의 템플릿 설치를 도와주는 인스톨러</li>
								</ul>	
							</div>
						</div>
					
					</div>
					<div style="float:left;width:400px;height:270px;padding-top:10px; background-color:#f7f7f7">
						<div class="download_content_top_wrap">
							<div class="download_img_loc">
								<img src="<c:url value='/'/>images/sample/img_download.gif" alt="다운로드 자료 이미지"/>
							</div>
							<div class="download_text_loc">
								<ul>
									<li>egovframe installer v1.03</li>
									<li>egovframe의 템플릿 설치를 도와주는 인스톨러.....egovframe의 템플릿 설치를 도와주는 인스톨러</li>
								</ul>	
							</div>
						</div>
						<div class="download_content_btm_wrap">
							<div class="download_img_loc">
								<img src="<c:url value='/'/>images/sample/img_download.gif" alt="다운로드 자료 이미지"/>
							</div>
							<div class="download_text_loc">
								<ul>
									<li>egovframe installer v1.03</li>
									<li>egovframe의 템플릿 설치를 도와주는 인스톨러.....egovframe의 템플릿 설치를 도와주는 인스톨러</li>
								</ul>	
							</div>
						</div>					
					</div>
				</div>
				<!-- //추천다운로드 끝-->
				<!-- 최신등록자료 시작 -->
				<div id="download_new"><h3>최신등록자료</h3></div>
				<div id="top10_div">
					<div class="top10_loc">
						<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number01.gif" alt="" /></span><a class="top10name" href="#">2011년도 표준프레임워크 기술지원 안내</a><span class="top10date">2011-06-03</span></li>
					 	</ol>
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number02.gif" alt="" /></span><a class="top10name" href="#">2011년도 표준프레임워크 기술지원 안내</a><span class="top10date">2011-06-03</span></li>
					 	</ol>	
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number03.gif" alt="" /></span><a class="top10name" href="#">2011년도 표준프레임워크 기술지원 안내</a><span class="top10date">2011-06-03</span></li>
					 	</ol>
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number04.gif" alt="" /></span><a class="top10name" href="#">2011년도 표준프레임워크 기술지원 안내</a><span class="top10date">2011-06-03</span></li>
					 	</ol>	
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number05.gif" alt="" /></span><a class="top10name" href="#">2011년도 표준프레임워크 기술지원 안내</a><span class="top10date">2011-06-03</span></li>
					 	</ol>				
					</div>				
					<div class="top10_rightloc">						
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number06.gif" alt="" /></span><a class="top10name" href="#">egovframework online installer v1.03</a><span class="top10date">2011-06-03</span></li>
					 	</ol>	
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number07.gif" alt="" /></span><a class="top10name" href="#">egovframework online installer v1.03</a><span class="top10date">2011-06-03</span></li>
					 	</ol>
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number08.gif" alt="" /></span><a class="top10name" href="#">egovframework online installer v1.03</a><span class="top10date">2011-06-03</span></li>
					 	</ol>	
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number09.gif" alt="" /></span><a class="top10name" href="#">egovframework online installer v1.03</a><span class="top10date">2011-06-03</span></li>
					 	</ol>	
					 	<ol>
					 		<li><span class="top10_img"><img src="<c:url value='/'/>images/num/ico_number10.gif" alt="" /></span><a class="top10name" href="#">egovframework online installer v1.03</a><span class="top10date">2011-06-03</span></li>
					 	</ol>		
						</div>
				</div>
				<!-- //최신등록자료 끝 -->
				<!-- 검색결과 시작 -->
				<div id="page_info"><div id="page_info_align"></div></div>					
				<!-- table add start -->
				<div class="default_tablestyle">
					<table summary="사용자목록관리" cellpadding="0" cellspacing="0">
					<caption>사용자목록관리</caption>
					<colgroup>
    					<col width="38" >
    					<col width="550" >  
    					<col width="50" >
    					<col width="50" >
    					<col width="%" >
					</colgroup>
					<thead>
					<tr>
						<th scope="col" class="f_field">번호</th>
						<th scope="col">소프트웨어명</th>
						<th scope="col">다운</th>
						<th scope="col">크기</th>
						<th scope="col">등록일</th>
					</tr>
					</thead>
					<tbody>		  			
					<!-- loop 시작 -->								
					<tr>
						<td><strong>1</strong></td>
						<td class="align_left_text"><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadDetail'/>">전자정부표준프레임워크 인스톨러(Egovframework installer) V1.037</a></td>
						<td>100</td>
						<td>16MB</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>2</strong></td>
						<td class="align_left_text"><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadDetail'/>">전자정부표준프레임워크 인스톨러(Egovframework installer) V1.037</a></td>
						<td>100</td>
						<td>16MB</td>
						<td>2011-04-04</td>
					</tr>
					<tr>
						<td><strong>3</strong></td>
						<td class="align_left_text"><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadDetail'/>">전자정부표준프레임워크 인스톨러(Egovframework installer) V1.037</a></td>
						<td>100</td>
						<td>16MB</td>
						<td>2011-04-04</td>
					</tr>	
					<tr>
						<td><strong>4</strong></td>
						<td class="align_left_text"><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadDetail'/>">전자정부표준프레임워크 인스톨러(Egovframework installer) V1.037</a></td>
						<td>100</td>
						<td>16MB</td>
						<td>2011-04-04</td>
					</tr>	
					<tr>
						<td><strong>5</strong></td>
						<td class="align_left_text"><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadDetail'/>">전자정부표준프레임워크 인스톨러(Egovframework installer) V1.037</a></td>
						<td>100</td>
						<td>16MB</td>
						<td>2011-04-04</td>
					</tr>													
					</tbody>
					</table> 
				</div>
				<!-- //검색결과 끝 -->
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
				<!-- //페이지 네비게이션 끝 -->
				<div class="buttons" style="clear:both;float:right;padding-left:2px;"><a href="<c:url value='/EgovPageLink.do?link=main/sample_menu/EgovDownloadModify'/>">자료올리기 </a></div> 						
			</div>
		
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