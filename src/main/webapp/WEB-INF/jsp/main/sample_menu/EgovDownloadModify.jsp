<%--
  Class Name : EgovDownloadModify.jsp
  Description : 샘플화면 - 자료실 등록(sample)
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
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle03-01.gif" width="776" height="230" /></div>
	
				<div id="download_regdiv">
					<div class="downloadDetail_title">
						<div class="detail_inputtitle"><label for="writer"><strong>프로그램명</strong></label><input type="text" name="writer" /></div>
						<span>작성자 : innovate</span>
						<span>2011-08-01 23:22:11</span> 
					</div>
					<div class="download_reg_loc">
						<div class="download_reg_img"> 
							<img src="<c:url value='/'/>images/sample/img_download.gif" />							
						</div> 					
						<div class="download_reg_imgtext"><p>썸네일 이미지는 width:160px, height:109px 크기의 이미지를 올려주세요</p></div>
					</div>
					<div class="download_tbstatus">
						<div class="download_regtable">
						<table summary="권장사항" cellpadding="0" cellspacing="0">
							<colgroup> 
								<col width="100"></col>
								<col width=""></col>
							</colgroup>
							<tbody>
							<tr>
								<th>운영체제 </th>
								<td><input type="text" /></td>
							</tr>
							<tr>
								<th>권장사양</th>
								<td><input type="text" /></td>
							</tr>
							<tr>
								<th>파일정보</th> 
								<td><input type="file" name="datafile" class="input_file" size="60" /></td>
							</tr>
							<tr>
								<th>파일크기</th> 
								<td><input type="text" value="13.0MB (13,670,274 바이트)" /></td>
							</tr>
							<tr>
								<th>등록일자</th>
								<td><input type="text" /></td>
							</tr>
							<tr>
								<th>언어</th>
								<td><input type="text" /></td>
							</tr>
							</tbody>
						</table>	
						</div>				
					</div>
				</div>
		        <!-- main content 시작 -->
				<div class="content_field" style="padding-top:2px;"><h3>자료설명입력</h3></div>
				<!-- //main content 끝 -->		
				<div class="datafield_wrap">
					<div class="datafield_textarea">  
					<textarea rows="14" cols="120" style="padding:5px;border:1px solid #dddddd;"></textarea>		 
					</div>
				</div>
				<div class="buttons" style="clear:both;float:right;padding-top:10px;padding-bottom:10px;"><a href="#">등록 </a></div> 
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