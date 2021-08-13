/****************************************************************
 * 
 * 파일명 : EgovCalPopup.js
 * 설  명 : 전자정부 공통서비스 달력 팝업 JavaScript
 * 
 *    수정일      수정자     Version        Function 명
 * ------------    ---------   -------------  ----------------------------
 * 2009.03.30    이중호       1.0             최초생성
 * 
 * 
 */
function dirname(path) {
	if (path.lastIndexOf("/") == -1)
		return "./";
	return path.replace(/\\/g, '/').replace(/\/[^\/]*\/?$/, '') + "/";
}

function getActiveScript() {
	var d = document.getElementsByTagName("script");
	var path = dirname(d[d.length - 1].src);
	delete d;
	
	var offset=path.indexOf(location.host)+location.host.length;
	return path.substring(offset);
} 


function getContextPath(){
    var offset=location.href.indexOf(location.host)+location.host.length;
    var ctxPath=location.href.substring(offset, location.href.indexOf('/',offset+1));
    
    if ((/^\/js/).test(getActiveScript())) {
    	return "";
    }

    return ctxPath;
}

function loadScript(src, f) {
  var head = document.getElementsByTagName("head")[0];
  var script = document.createElement("script");
  script.src = src;
  var done = false;
  script.onload = script.onreadystatechange = function() { 
    // attach to both events for cross browser finish detection:
    if ( !done && (!this.readyState ||
      this.readyState == "loaded" || this.readyState == "complete") ) {
      done = true;
      if (typeof f == 'function') f();
      // cleans up a little memory:
      script.onload = script.onreadystatechange = null;
      head.removeChild(script);
    }
  };
  head.appendChild(script);
}

loadScript(getContextPath() + '/js/showModalDialog.js');

function fn_egov_NormalCalendar(frm, sDate, vDate) {
	var retVal;

	var url = frm.cal_url.value;
	var varParam = new Object();
	varParam.sDate = sDate.value;		

	// IE
	//var openParam = "dialogWidth:252px;dialogHeight:175px;scroll:no;status:no;center:yes;resizable:yes;";
	// FIREFOX
	var openParam = "";
	var sAppName = navigator.appName ;

	if( sAppName.indexOf("Netscape") > -1){
		openParam = "dialogWidth:275px;dialogHeight:200px;scroll:no;status:no;center:yes;resizable:yes;";
	}else if(sAppName.indexOf("Microsoft") > -1){
		openParam = "dialogWidth:275px;dialogHeight:200px;scroll:no;status:no;center:yes;resizable:yes;";
	}else{
		openParam = "dialogWidth:275px;dialogHeight:200px;scroll:no;status:no;center:yes;resizable:yes;";
	}
	
	retVal = window.showModalDialog(url, varParam, openParam, "calendarCallback");
	
	otherParameters[0] = fn_egov_NormalCalendar.arguments.length;
	otherParameters[1] = sDate;
	otherParameters[2] = vDate;

	if(retVal) {
		if(fn_egov_NormalCalendar.arguments.length == 2){
			sDate.value = retVal.vDate;
		}else{
			sDate.value = retVal.sDate; 
			vDate.value = retVal.vDate; 
		}
	}
}

function calendarCallback(retVal) {
	if (retVal) {
		if (otherParameters[0] == 2) {
			otherParameters[1].value = retVal.vDate;
		} else {
			otherParameters[1].value = retVal.sDate;
			otherParameters[2].value = retVal.vDate;
		}
	}
}