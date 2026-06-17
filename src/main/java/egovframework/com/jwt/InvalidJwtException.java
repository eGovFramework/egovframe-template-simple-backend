package egovframework.com.jwt;

public class InvalidJwtException extends RuntimeException {
    private static final long serialVersionUID = 8805378080076099368L;

	public InvalidJwtException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidJwtException(String message) {
        super(message);
    }
}

