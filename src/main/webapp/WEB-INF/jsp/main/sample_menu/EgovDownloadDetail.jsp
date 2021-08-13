<%--
  Class Name : EgovDownloadDetail.jsp
  Description : 샘플화면 - 자료실 상세조회(sample)
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
				<div id="content_img_div"><img src="<c:url value='/'/>images/subtitle/img_subtitle03-01.gif" width="776" height="230" alt="자료실 전자정부표준프레임워크 경량환경 페이지의 다양한 종류의 자룔르 다운 받으실 수 있습니다."/></div>
	
				<div id="download_div02">
					<div class="downloadDetail_title">
						<div class="downloadDetail_program_loc"><span class="downloadDetail_titletxt">egovframe installer v1.03</span></div>
						<span>작성자 : innovate</span>
						<span>2011-08-01 23:22:11</span> 
					</div>
					<div class="sum_img_div_wrap">
						<div class="sum_img_div_loc"> 
							<div><img src="<c:url value='/'/>images/sample/img_download.gif" alt="다운로드 받을 프로그램 이미지"/></div>							
						</div> 
					<div class="download_btn_area"><a href="#"><img src="<c:url value='/'/>images/btn/btn_download.gif"  alt="download" /></a></div>
					</div>
					<div class="download_modify_table">
						<div id="download_detailtable">
						<table summary="권장사항" cellpadding="0" cellspacing="0">
							<colgroup> 
								<col width="100">
								<col width="%">
							</colgroup>
							<tbody>
							<tr>
								<th>운영체제 </th>
								<td>Win95/Win98/WinME/WinNT/Win2000/WinXP/WinVISTA/Win7/</td>
							</tr>
							<tr>
								<th>권장사양</th>
								<td>펜티엄3</td>
							</tr>
							<tr>
								<th>파일정보</th> 
								<td>7MB (총 1 개)/ egovframework-common-all.zip [15,083,713 byte]</td>
							</tr>
							<tr>
								<th>등록일자</th>
								<td>2011-08-08 11:11:11</td>
							</tr>
							<tr>
								<th>언어</th>
								<td>영어</td>
							</tr>
							</tbody>
						</table>	
						</div>				
					</div>
				</div>
		        <!-- main content 시작 -->
				<div class="content_field"><h3>자료상세설명</h3></div>
				<!-- //main content 끝 -->		
				<div class="download_modify_txtarea_wrap">
					<div class="download_modify_content">
                    안녕하세요..
                    
                    공통컴포넌트 전체 소스입니다.
                    
                    관련된 내용은 다음 가이드를 참조하십시오.
                    http://www.egovframe.go.kr/wiki/doku.php?id=egovframework:com:v3:init_guide
                    
                    감사합니다.				
					</div>
				</div>
				<div class="buttons" style="clear:both;float:right;padding-top:10px;padding-bottom:10px;"><a href="javascript:fn_main_headPageMove('31','main/sample_menu/EgovDownload')">목록 </a></div> 
				<div class="btm_prev">이전글 egovframe installer v1.03</div>
				<div class="btm_next">다음글 egovframe installer v1.03 </div> 	
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