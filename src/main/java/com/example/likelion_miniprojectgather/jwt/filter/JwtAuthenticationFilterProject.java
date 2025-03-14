package com.example.likelion_miniprojectgather.jwt.filter;

import com.example.likelion_miniprojectgather.jwt.jwtExceptionCode.JwtExceptionCode;
import com.example.likelion_miniprojectgather.jwt.token.JwtAuthenticationToken;
import com.example.likelion_miniprojectgather.jwt.token.JwtTokenizer;
import com.example.likelion_miniprojectgather.security.CustomerUserDetails;
import com.example.likelion_miniprojectgather.service.JwtBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilterProject extends OncePerRequestFilter{

    private final JwtTokenizer jwtTokenizer;
    private final JwtBlacklistService jwtBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = getToken(request);
        log.info(token);

        if(StringUtils.hasText(token)){
            try{
                // 블랙리스트 확인 (로그아웃된 토큰인지 체크)
                // 블랙리스트에 포함된 토큰이라면, 로그아웃한 토큰인것이다.
                if (jwtBlacklistService.isBlacklisted(token)) { //true라면 로그아웃된 토큰
                    log.error("블랙리스트된 토큰 사용 시도: {}", token);
                    //
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Token is blacklisted");
                    return;
                }
                Authentication authentication = getAuthentication(token);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (ExpiredJwtException e){
                request.setAttribute("exception", JwtExceptionCode.EXPIRED_TOKEN.getCode());
                log.error("Expired Token : {}",token,e);
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("Expired token exception", e);
            }catch (UnsupportedJwtException e){
                request.setAttribute("exception", JwtExceptionCode.UNSUPPORTED_TOKEN.getCode());
                log.error("Unsupported Token: {}", token, e);
                SecurityContextHolder.clearContext();
                throw new BadCredentialsException("Unsupported token exception", e);
            } catch (MalformedJwtException e) {
                request.setAttribute("exception", JwtExceptionCode.INVALID_TOKEN.getCode());
                log.error("Invalid Token: {}", token, e);

                SecurityContextHolder.clearContext();

                throw new BadCredentialsException("Invalid token exception", e);
            } catch (IllegalArgumentException e) {
                request.setAttribute("exception", JwtExceptionCode.NOT_FOUND_TOKEN.getCode());
                log.error("Token not found: {}", token, e);

                SecurityContextHolder.clearContext();

                throw new BadCredentialsException("Token not found exception", e);
            }
        }
        filterChain.doFilter(request,response);
        }


    private String getToken(HttpServletRequest request){
        //헤더에 있는지 확인
        String authorization = request.getHeader("Authorization");
        if(StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")){
            log.info(authorization.substring(7));
            return authorization.substring(7);
        }

        //쿠키에 있는지 확인
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie : cookies){
                if("accessToken".equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }

        //헤더에도 없고, 쿠키에도 없다면 ?
        return null;

    }

    private Authentication getAuthentication(String token){
        Claims claims = jwtTokenizer.parseAccessToken(token);
        String email = claims.getSubject();
        Long userId = claims.get("userId",Long.class);

        CustomerUserDetails customerUserDetails
                = new CustomerUserDetails(userId,email,"");
        //권한이 없기 때문에 빈 리스트 값을 넘겨준다.
        return new JwtAuthenticationToken(Collections.emptyList(),customerUserDetails,null);

    }


}
