package egovframework.com.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.ResponseCode;
import egovframework.com.cmm.service.ResultVO;

/**
 * fileName       : JwtAuthenticationEntryPoint
 * author         : crlee
 * date           : 2023/06/11
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/06/11        crlee       최초 생성
 */

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ResultVO resultVO = new ResultVO();
        resultVO.setResultCode(ResponseCode.AUTH_ERROR.getCode());
        resultVO.setResultMessage(ResponseCode.AUTH_ERROR.getMessage());
        ObjectMapper mapper = new ObjectMapper();

        //Convert object to JSON string
        String jsonInString = mapper.writeValueAsString(resultVO);



        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(jsonInString);

    }
}