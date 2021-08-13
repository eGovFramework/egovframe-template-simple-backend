package egovframework.com.cmm.service;

public class ResultVO {

	public static final int INPUT_CHECK_ERROR = 900;
	public static final String INPUT_CHECK_ERROR_MESSAGE = "입력값 무결성 오류 입니다.";
	public static final int SAVE_ERROR = 800;
	public static final String SAVE_ERROR_MESSAGE = "저장시 내부 오류가 발생했습니다.";
	
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	int resultCode = 0;
	String resultMessage = "OK";
	
}
