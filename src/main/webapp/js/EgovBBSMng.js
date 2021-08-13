/****************************************************************
 * 
 * 파일명 : EgovBBSMng.js
 * 설  명 : 전자정부 공통서비스 로그관리 기능 사용 JavaScript
 * 
 *    수정일      수정자     Version        Function 명
 * ------------    ---------   -------------  ----------------------------
 * 2009.03.10    이삼섭       1.0             최초생성
 * 
 * 
 * **************************************************************/

function fn_egov_trim(str) {
    if (str == null) {
        return '';
    }
    var count = str.length;
    var len = count;
    var st = 0;

    while ((st < len) && (str.charAt(st) <= ' ')) {
        st++;
    }
    
    while ((st < len) && (str.charAt(len - 1) <= ' ')) {
        len--;
    }
    
    return ((st > 0) || (len < count)) ? str.substring(st, len) : str;
}
