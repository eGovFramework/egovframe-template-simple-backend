/****************************************************************
 *
 * 파일명 : showModalDialog.js
 * 설  명 : showModalDialog 기능을 대체하는 JavaScript
 *
 *    수정일      수정자     Version        Function 명
 * ------------    ---------   -------------  ----------------------------
 * 2014.09.25    Vincent Han       1.0             최초생성
 *
 */
// fix for deprecated method in Chrome 37

this.otherParameters = new Array();
this.showModalDialogSupported = true;

this.callbackMethod = null;

if (!window.showModalDialog) {
	
	showModalDialogSupported = false;

	window.showModalDialog = function(arg1, arg2, arg3, callback) {

		var w;
		var h;
		var resizable = "no";
		var scroll = "no";
		var status = "no";

		// get the modal specs
		var mdattrs = arg3.split(";");
		for (i = 0; i < mdattrs.length; i++) {
			var mdattr = mdattrs[i].split(":");

			var n = mdattr[0];
			var v = mdattr[1];
			if (n) {
				n = n.trim().toLowerCase();
			}
			if (v) {
				v = v.trim().toLowerCase();
			}

			if (n == "dialogheight") {
				h = v.replace("px", "");
			} else if (n == "dialogwidth") {
				w = v.replace("px", "");
			} else if (n == "resizable") {
				resizable = v;
			} else if (n == "scroll") {
				scroll = v;
			} else if (n == "status") {
				status = v;
			} else {
				// no-op
			}
		}

		var left = window.screenX + (window.outerWidth / 2) - (w / 2);
		var top = window.screenY + (window.outerHeight / 2) - (h / 2);
		var targetWin = window.open(arg1, "ShowModalDialog" + arg1, 'toolbar=no, location=no, directories=no, status=' + status + ', menubar=no, scrollbars=' + scroll + ', resizable=' + resizable + ', copyhistory=no, width=' + w	+ ', height=' + h + ', top=' + top + ', left=' + left);

		dialogArguments = arg2;
		
		if (callback != null) {
			callbackMethod = callback;
		} else {
			callbackMethod = null;
		}

		targetWin.focus();
	};

	window.getDialogArgumentsInner = function() {
		return dialogArguments;
	}; 
	
	window.getCallbackMethodName = function() {
		return callbackMethod;
	}
  }