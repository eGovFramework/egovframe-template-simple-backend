package egovframework.com.sns;

import lombok.Getter;

class SnsVO {
	/**
	 * 네이버용 토큰, 응답, 프로필  변수 VO 시작
	 */
	@Getter
	static class NaverTokenVO {
		private String access_token;
	    private String refresh_token;
	    private String token_type;
	    private String expires_in;
	    private String error;
	    private String error_description;
	}
	@Getter
	static class NaverResponseVO {
		private String resultcode;
	    private String message;
	    private Object response;
	}
	@Getter
	static class NaverProfileVO {
	    private String id;
	    private String email;
	    private String name;
	}
	/**
	 * 카카오용 토큰, 응답, 프로필  변수 VO 끝
	 */
	@Getter
	static class KakaoTokenVO {
		private String access_token;
	    private String refresh_token;
	    private String token_type;
	    private String expires_in;
	    private String scope;
	    private String refresh_token_expires_in;
	    private String error;
	    private String error_description;
	    private String error_code;
	}
	@Getter
	static class KakaoResponseVO {
		private String id;
		private String connected_at;
		private Object properties;
		private Object kakao_account;
		private String msg;
	    private int code;
	}
	@Getter
	static class KakaoProfileVO {
		private String id;
	    private String nickname;
	}
}