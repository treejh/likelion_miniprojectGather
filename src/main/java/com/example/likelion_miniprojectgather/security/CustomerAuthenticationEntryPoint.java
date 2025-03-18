package com.example.likelion_miniprojectgather.security;

import com.example.likelion_miniprojectgather.jwt.jwtExceptionCode.JwtExceptionCode;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //시큐리티에서 인증되지 않은 사용자가, 인증해야 사용할 수 있는 보호된 리소스에 접근하려고 할때 동작하는 인터페이스
    //예를들어 /info는 인증된 사용자만 접근할 수 있는데, 인증되지 않은 사용자가 /info에 접근하려고 할때 동작하는 인터페이스
    //사용자가 인증되지 않았을 때 어떻게 응답할지를 정의해주기 위해 사용
    //인증되지 않는 사용자가 접근할때 따로 설정을 해주지 않으면, 아무렇게나 뜸 ㅇㅇ (해당되는 오류나 기본으로 )
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        String exception  = (String) request.getAttribute("exception");

        if(isRestRequest(request)){
            handleRestResponse(request,response,exception);
        }else{
            //page로 요청이 들어왔을때 수행 코드
            //handlePageResponse(request,response,exception);
        }

    }

    private void handleRestResponse(HttpServletRequest request, HttpServletResponse response, String exception) throws IOException {
        log.error("Rest Request - Commence Get Exception : {}", exception);

        if (exception != null) {
            if (exception.equals(JwtExceptionCode.INVALID_TOKEN.getCode())) {
                log.error("entry point >> invalid token");
                setResponse(response, JwtExceptionCode.INVALID_TOKEN);
            } else if (exception.equals(JwtExceptionCode.EXPIRED_TOKEN.getCode())) {
                log.error("entry point >> expired token");
                setResponse(response, JwtExceptionCode.EXPIRED_TOKEN);
            } else if (exception.equals(JwtExceptionCode.UNSUPPORTED_TOKEN.getCode())) {
                log.error("entry point >> unsupported token");
                setResponse(response, JwtExceptionCode.UNSUPPORTED_TOKEN);
            } else if (exception.equals(JwtExceptionCode.NOT_FOUND_TOKEN.getCode())) {
                log.error("entry point >> not found token");
                setResponse(response, JwtExceptionCode.NOT_FOUND_TOKEN);
            } else {
                setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
            }
        } else {
            setResponse(response, JwtExceptionCode.UNKNOWN_ERROR);
        }
    }


    private void setResponse(HttpServletResponse response, JwtExceptionCode exceptionCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401

        HashMap<String, Object> errorInfo = new HashMap<>();
        errorInfo.put("message", exceptionCode.getMessage());
        errorInfo.put("code", exceptionCode.getCode());
        Gson gson = new Gson();
        String responseJson = gson.toJson(errorInfo);
        response.getWriter().print(responseJson);
    }

    //페이지 요청 중에 예외가 발생했다면, 로그 남기고, 무조건 /loginform으로 리다이렉트
    private void handlePageResponse(HttpServletRequest request, HttpServletResponse response, String exception) throws IOException {
        log.error("Page Request - Commence Get Exception : {}", exception);

        if (exception != null) {
            // 추가적인 페이지 요청에 대한 예외 처리 로직을 여기에 추가할 수 있습니다.
        }

        response.sendRedirect("/login-form");
    }




    private boolean isRestRequest(HttpServletRequest request) {
        String requestedWithHeader = request.getHeader("X-Requested-With");

        return "XMLHttpRequest".equals(requestedWithHeader)
                || request.getRequestURI().startsWith("/api/");
    }

}
