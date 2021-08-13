/****************************************************************
 *
 * 파일명 : showModalDialogCallee.js
 * 설  명 : showModalDialog 기능을 대체하는 JavaScript
 *
 *    수정일      수정자     Version        Function 명
 * ------------    ---------   -------------  ----------------------------
 * 2014.09.25    Vincent Han       1.0             최초생성
 *
 */

//----------------------
//popped up page
//----------------------
function getDialogArguments() {
	if (!window.showModalDialog) {
		if (opener != null && !opener.closed) {
			try {
				window.dialogArguments = opener.getDialogArgumentsInner();
			} catch (err) {
				alert('팝업 처리 시 오류가 발생하였습니다. \n오류내용 : ' + err);
			}
		} else if (parent.opener != null && !parent.opener.closed) {
			try {
				window.dialogArguments = parent.opener.getDialogArgumentsInner();
				parent.window.dialogArguments = window.dialogArguments;
			} catch (err) {
				alert('팝업 처리 시 오류가 발생하였습니다. \n오류내용 : ' + err);
			}
		} else {
			alert('업무 화면에 대한 변동이 있습니다. 다시 시도해 주십시오.');
		}
	}
}

function setReturnValue(obj) {
	if (!window.showModalDialog) {
		if (opener != null && !opener.closed) {
			
			var callbackMethod = opener.getCallbackMethodName();
			
			try {
				if (callbackMethod != null) {
					opener[callbackMethod](obj);
				} else {
					opener.showModalDialogCallback(obj);
				}
			} catch (err) {
				alert('팝업 처리 시 오류가 발생하였습니다. \n오류내용 : ' + err);
			}
		} else if (parent.opener != null && !parent.opener.closed) {
			
			var callbackMethod = parent.opener.getCallbackMethodName();
			
			try {
				if (callbackMethod != null) {
					parent.opener[callbackMethod](obj);
				} else {
					parent.opener.showModalDialogCallback(obj);
				}
			} catch (err) {
				alert('팝업 처리 시 오류가 발생하였습니다. \n오류내용 : ' + err);
			}
		} else {
			alert('업무 화면에 대한 변동이 있습니다. 다시 시도해 주십시오.');
		}
	}
}