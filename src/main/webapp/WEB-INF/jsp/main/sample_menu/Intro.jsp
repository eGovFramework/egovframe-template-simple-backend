<%--
  Class Name : Intro.jsp
  Description : 샘플화면 - 구성설명(sample)
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
<title>표준프레임워크 경량환경 홈페이지템플릿 소개</title>
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
</head>
<body style="margin-left:10px">
<!-- 전체 레이어 시작 -->
<div id="wrap">

<h2>홈페이지 템플릿 소개</h2>

<pre>
- 경량환경 템플릿은 개발자가 프레임워크 쉽게 이해하고 활용할 수 있도록 지원합니다.
- 홈페이지 템플릿은 공통컴포넌트를 기반으로 아래 그림과 같이 메뉴가 구성됩니다.
- 관리자로 로그인하면 관리자용 메뉴를 추가로 사용할 수 있습니다.
- 기울임체로 표시된 메뉴는 구성을 위한 샘플페이지가 제공되며 기능은 구현되지 않은 상태입니다.
</pre>
<br>
<img src="<c:url value='/images/menu_sht.jpg'/>" alt="단순 홈페이지 메뉴구성">

<br>
<a href="http://www.egovframe.go.kr/wiki/doku.php?id=egovframework:let" target="_blank">
<font color="blue">경량환경 템플릿 위키가이드 보기</font>
</a> 
<br>

</div>

<!-- //전체 레이어 끝 -->
</body>
</html>