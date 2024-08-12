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
	 * 네이버용 토큰, 응답, 프로필  변수 VO 끝
	 */
}
