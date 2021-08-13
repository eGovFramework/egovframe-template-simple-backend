<%--
  Class Name : EgovTemplateInqirePop.jsp
  Description : 템플릿 목록 조회 팝업화면
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.18   이삼섭          최초 생성
     2011.08.31   JJY       경량환경 버전 생성
 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.18 
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<c:set var="ImgUrl" value="/images/egovframework/cop/com/"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="content-language" content="ko">
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<script type="text/javascript" src="<c:url value='/js/showModalDialogCallee.js'/>" ></script>
<script type="text/javascript">
	function press(event) {
		if (event.keyCode==13) {
			fn_egov_select_tmplatInfo('1');
		}
	}
	function fn_egov_select_tmplatInfo(pageNo){
		document.frm.pageIndex.value = pageNo;
		document.frm.action = "<c:url value='/cop/com/selectTemplateInfsPop.do'/>";
		document.frm.submit();	
	}
	
	function fn_egov_returnTmplatInfo(tmplatId, tmplatNm){
		getDialogArguments();
		var retVal = tmplatId +"|"+tmplatNm;
		
		setReturnValue(retVal);
		 
		parent.window.returnValue = retVal;
		parent.window.close();
	}

</script>
<title>템플릿 목록</title>

<style type="text/css">
	h1 {font-size:12px;}
	caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>


</head>
<body>
<form name="frm" action ="<c:url value='/cop/com/selectTemplateInfsPop.do'/>" method="post">
    <input type="hidden" name="tmplatId" value="" />
    <input type="submit" id="invisible" class="invisible"/>

    <!-- 검색 필드 박스 시작 -->
    <div id="search_field">
        <div id="search_field_loc"><h2><strong>템플릿 목록</strong></h2></div>
            <fieldset><legend>조건정보 영역</legend>    
            <div class="sf_start">
                <ul id="search_first_ul">
                    <li>
                        <label for="search_select"></label>
      <select name="searchCnd" class="select" title="검색조건 선택">
         <option value="0" <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> >템플릿명</option>
         <option value="1" <c:if test="${searchVO.searchCnd == '1'}">selected="selected"</c:if> >템플릿구분</option>   
                        </select>
                    </li>
                    <li>
                        <input name="searchWrd" type="text" size="35" value='<c:out value="${searchVO.searchWrd}"/>' maxlength="35" onkeypress="press(event);" title="검색어 입력"> 
                    </li>       
                </ul>
                <ul id="search_second_ul">
                    <li>
                        <div class="buttons" style="position:absolute;left:700px;top:47px;">
                            <a href="<c:url value='/cop/com/selectTemplateInfsPop.do'/>" onclick="javascript:fn_egov_select_tmplatInfo('1'); return false;"><img src="<c:url value='/images/img_search.gif' />" alt="search" />조회 </a>
                        </div>                              
                    </li>
                </ul>           
            </div>          
            </fieldset>
    </div>
    <!-- //검색 필드 박스 끝 -->

    <div id="page_info"><div id="page_info_align"></div></div>                    
    <!-- table add start -->
    <div class="default_tablestyle">
        <table summary="번호, 템플릿명, 템플릿구분, 템플릿경로, 사용여부, 등록일자, 선택   목록입니다" cellpadding="0" cellspacing="0">
        <caption>사용자목록관리</caption>
        <colgroup>
        <col width="5%">
        <col width="15%">  
        <col width="10%">
        <col width="37%">
        <col width="5%">
        <col width="10%">
        <col width="8%">
        </colgroup>
        <thead>
        <tr>
            <th scope="col" class="f_field" nowrap="nowrap">번호</th>
            <th scope="col" nowrap="nowrap">템플릿명</th>
            <th scope="col" nowrap="nowrap">템플릿구분</th>
            <th scope="col" nowrap="nowrap">템플릿경로</th>
            <th scope="col" nowrap="nowrap">사용여부</th>
            <th scope="col" nowrap="nowrap">등록일자</th>
            <th scope="col" nowrap="nowrap">선택</th>
        </tr>
        </thead>
        <tbody>                 

        <c:forEach var="result" items="${resultList}" varStatus="status">
        <!-- loop 시작 -->                                
			 <tr>
			   <td nowrap="nowrap"><strong><c:out value="${(searchVO.pageIndex-1) * searchVO.pageSize + status.count}"/></strong></td>          
			   <td nowrap="nowrap"><c:out value="${result.tmplatNm}"/></td>
			   <td nowrap="nowrap"><c:out value="${result.tmplatSeCodeNm}"/></td>
			   <td nowrap="nowrap"><c:out value="${result.tmplatCours}"/></td>
			   <td nowrap="nowrap">
			       <c:if test="${result.useAt == 'N'}"><spring:message code="button.notUsed" /></c:if>
			       <c:if test="${result.useAt == 'Y'}"><spring:message code="button.use" /></c:if>
			   </td>  
			   <td nowrap="nowrap"><c:out value="${result.frstRegisterPnttm}"/></td    >     
			   <td nowrap="nowrap">
			       <c:if test="${result.useAt == 'Y'}">
			           <input type="button" name="selectTmplat" value="선택" 
			               onClick="javascript:fn_egov_returnTmplatInfo('<c:out value="${result.tmplatId}"/>','<c:out value="${result.tmplatNm}"/>')" />
			       </c:if>         
			   </td>  
			 </tr>
        </c:forEach>     
        <c:if test="${fn:length(resultList) == 0}">
	       <tr>
	           <td nowrap colspan="6" ><spring:message code="common.nodata.msg" /></td>  
	       </tr>      
	    </c:if>
        </tbody>
        </table>
    </div>

    <!-- 페이지 네비게이션 시작 -->
    <div id="paging_div">
        <ul class="paging_align">
           <ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fn_egov_select_tmplatInfo" />
        </ul>
    </div>                          
    <!-- //페이지 네비게이션 끝 -->  
    <input name="pageIndex" type="hidden" value="<c:out value='${searchVO.pageIndex}'/>"/>

	<table width="100%" border="0" cellspacing="0" cellpadding="0">
	  <tr> 
	    <td height="10"></td>
	  </tr>
	</table>
	<div class="buttons" align="center" >
		<table border="0" cellspacing="0" cellpadding="0" align="center">
			<tr> 
				<td><a href="#LINK" onclick="javascript:parent.close(); return false;">닫기</a></td>
			</tr>
		</table>	
	</div>
</form>
</body>
</html>