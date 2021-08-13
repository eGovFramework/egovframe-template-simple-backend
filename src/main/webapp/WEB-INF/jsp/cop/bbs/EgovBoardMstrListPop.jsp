<%--
  Class Name : EgovBoardMstrListPop.jsp
  Description : 게시판 속성 조회 팝업
  Modification Information
 
      수정일         수정자                   수정내용
    -------    --------    ---------------------------
     2009.03.12   이삼섭          최초 생성
     2011.08.31  JJY       경량환경 버전 생성
  <input name="searchWrd" type="text" 
    author   : 공통서비스 개발팀 이삼섭
    since    : 2009.03.12
--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" >
<meta http-equiv="content-language" content="ko">
<link href="<c:url value='/'/>css/common.css" rel="stylesheet" type="text/css" >
<script type="text/javascript" src="<c:url value='/js/EgovBBSMng.js' />"></script>
<script type="text/javascript">
    function press(event) {
        if (event.keyCode==13) {
            fn_egov_select_brdMstr('1');
        }
    }
    function fn_egov_select_brdMstr(pageNo){
        document.frm.pageIndex.value = pageNo; 
        document.frm.action = "<c:url value='/cop/bbs/SelectBBSMasterInfsPop.do'/>";
        document.frm.submit();                  
    }
    
    function fn_egov_select_brdMstrInfo(bbsId, bbsNm){
        var retVal = bbsId +"|"+bbsNm;
        parent.fn_egov_returnValue(retVal);
    }
</script>
<title>게시판 정보</title>

<style type="text/css">
    h1 {font-size:12px;}
    caption {visibility:hidden; font-size:0; height:0; margin:0; padding:0; line-height:0;}
</style>


</head>
<body>
<form name="frm" method="post" action="<c:url value='/cop/bbs/SelectBBSMasterInfsPop.do'/>">
    <input type="submit" value="실행" onclick="fn_egov_select_brdMstr('1'); return false;" id="invisible" class="invisible" />
    <input type="hidden" name="bbsId" value="">

    <!-- 검색 필드 박스 시작 -->
    <div id="search_field">
        <div id="search_field_loc"><h2><strong>게시판 정보</strong></h2></div>
            <fieldset><legend>조건정보 영역</legend>    
            <div class="sf_start">
                <ul id="search_first_ul">
                    <li>
                        <label for="searchCnd">검색유형선력</label>
			            <select id="searchCnd" name="searchCnd" class="select" title="검색유형선력">
			               <option value="0" <c:if test="${searchVO.searchCnd == '0'}">selected="selected"</c:if> >게시판명</option>
			               <option value="1" <c:if test="${searchVO.searchCnd == '1'}">selected="selected"</c:if> >게시판유형</option>   
                        </select>
                    </li>
                    <li>
                        <label for="searchWrd" class="invisible">검색어</label>
                        <input id="searchWrd" name="searchWrd" title="검색어" type="text" size="35" value='<c:out value="${searchVO.searchWrd}"/>' maxlength="35" onkeypress="press(event);">
                    </li>
                    <li>
                        <div class="buttons" style="float:right;">
                            <a href="<c:url value='/cop/bbs/SelectBBSMasterInfsPop.do'/>" onclick="javascript:fn_egov_select_brdMstr('1'); return false;"><img src="<c:url value='/images/img_search.gif' />" alt="search" />조회 </a>
                        </div>                              
                    </li>   
                </ul>
            </div>          
            </fieldset>
    </div>
    <!-- //검색 필드 박스 끝 -->

    <!-- div id="page_info"><div id="page_info_align">총 <strong>321</strong>건 (<strong>1</strong> / 12 page)</div></div-->                    
    <!-- table add start -->
    <div class="default_tablestyle">
        <table  cellpadding="0" cellspacing="0">
        <caption>게시판정보</caption>
        <colgroup>
	        <col width="10%" >  
	        <col width="36%" >
	        <col width="10%" >
	        <col width="10%" >
	        <col width="15%" >
	        <col width="8%" >
	        <col width="8%" >
        </colgroup>
        <thead>
        <tr>
            <th scope="col" class="f_field" nowrap="nowrap">번호</th>
            <th scope="col" nowrap="nowrap">게시판명</th>
            <th scope="col" nowrap="nowrap">게시판유형</th>
            <th scope="col" nowrap="nowrap">게시판속성</th>
            <th scope="col" nowrap="nowrap">생성일</th>
            <th scope="col" nowrap="nowrap">사용여부</th>
            <th scope="col" nowrap="nowrap">선택</th>
        </tr>
        </thead>
        <tbody>                 

        <c:forEach var="result" items="${resultList}" varStatus="status">
        <!-- loop 시작 -->                                
             <tr>
	            <!--td class="lt_text3" nowrap="nowrap"><input type="checkbox" name="check1" class="check2"></td-->
	            <td nowrap="nowrap"><strong><c:out value="${(searchVO.pageIndex-1) * searchVO.pageSize + status.count}"/></strong></td>
	            <td nowrap="nowrap">
	                <c:out value="${result.bbsNm}"/>
	            </td>
	            <td nowrap="nowrap"><c:out value="${result.bbsTyCodeNm}"/></td>
	            <td nowrap="nowrap"><c:out value="${result.bbsAttrbCodeNm}"/></td>
	            <td nowrap="nowrap"><c:out value="${result.frstRegisterPnttm}"/></td>
	            <td nowrap="nowrap">
	                <c:if test="${result.useAt == 'N'}"><spring:message code="button.notUsed" /></c:if>
	                <c:if test="${result.useAt == 'Y'}"><spring:message code="button.use" /></c:if>
	            </td>
	            <td nowrap="nowrap">
                    <input type="button" value="선택"  
                        onClick="javascript:fn_egov_select_brdMstrInfo('<c:out value="${result.bbsId}"/>','<c:out value="${result.bbsNm}"/>');" />
	            </td>
	          </tr>
         </c:forEach>     
         <c:if test="${fn:length(resultList) == 0}">
	          <tr>
	            <td nowrap colspan="7" ><spring:message code="common.nodata.msg" /></td>  
	          </tr>      
         </c:if>
        </tbody>
        </table>
    </div>

    <!-- 페이지 네비게이션 시작 -->
    <div id="paging_div">
        <ul class="paging_align">
           <ui:pagination paginationInfo = "${paginationInfo}" type="image" jsFunction="fn_egov_select_brdMstr" />
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