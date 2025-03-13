package com.example.likelion_miniprojectgather.jwt.token;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenizer {

    private final byte[] accessSecret;
    private final byte[] refreshSecret;


    //토큰 만료 시간이 달라지면 안되기 때문에 static으로 사용해야 한다.
    public static final Long ACCESS_TOKEN_EXPIRE_COUNT = 5 * 60 * 60 * 1000L;
    // Access Token 유효기간: 5시간 (5시간 * 60분 * 60초 * 1000ms)
    public static final Long REFRESH_TOKEN_EXPIRE_COUNT = 24 * 60 * 60 * 1000L;
    // Refresh Token 유효기간: 1일 (24시간 * 60분 * 60초 * 1000ms)


    public JwtTokenizer(@Value("${jwt.secretKey}") String accessSecret, @Value("${jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes(StandardCharsets.UTF_8);
        this.refreshSecret = refreshSecret.getBytes(StandardCharsets.UTF_8);
    }

    //token 생성
    public String createToken(Long id, String email,byte[] secretKey,Long expire){

        //이 토큰의 주인이 누구인지 나타내는 클레임
        //고유한 식별자 값 저장
        Claims claims = Jwts.claims().setSubject(email);

        claims.put("userId",id);
        claims.put("email",email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+expire))
                .signWith(getSigningKey(secretKey))
                .compact();


    }



    //access token 생성
    public String createAccessToken(Long id, String email){
        return createToken(id, email, accessSecret,ACCESS_TOKEN_EXPIRE_COUNT);
    }



    //refresh token
    public String refreshAccessToken(Long id, String email){
        return createToken(id, email, refreshSecret, REFRESH_TOKEN_EXPIRE_COUNT);
    }


    public Long getUserIdFromToken(String token){
        if(token == null || token.isBlank()){
            throw new IllegalArgumentException("JWT 토큰이 없습니다.");
        }

        if(!token.startsWith("Bearer ")){
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }
        //Bearer로 시작해서 그거 없애주려고
        String [] tokenArr = token.split(" ");

        token = tokenArr[1];
        Claims claims = parseToken(token, accessSecret);

        if(claims == null){
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }

        Object userId = claims.get("userId");

        //return Long.valueOf((Integer)claims.get("id"));

        if(userId instanceof Number){
            return ((Number)userId).longValue();
        }else{
            throw new IllegalArgumentException("JWT토큰에서 userId를 찾을 수 없습니다.");
        }

    }

    public Claims parseAccessToken(String accessToken){
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken){
        return parseToken(refreshToken,accessSecret);
    }

    public String getUserEmailFromToken(String token){
        if(token == null || token.isBlank()){
            throw new IllegalArgumentException("JWT 토큰이 없습니다.");
        }

        if(!token.startsWith("Bearer ")){
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }
        //Bearer로 시작해서 그거 없애주려고
        String [] tokenArr = token.split(" ");

        token = tokenArr[1];
        Claims claims = parseToken(token, accessSecret);

        if(claims == null){
            throw new IllegalArgumentException("유효하지 않은 형식입니다.");
        }

        Object email = claims.get("email");

        //return Long.valueOf((Integer)claims.get("id"));

        if(email instanceof String){
            return ((String)email);
        }else{
            throw new IllegalArgumentException("JWT토큰에서 email를 찾을 수 없습니다.");
        }

    }

    //받은 토큰에서 데이터 받는 메서드 ->여기서 유효한 토큰인지 같이 확인한다.
    public Claims parseToken(String token, byte[] secretKey){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    // JWT 토큰을 서명(Signing)할 때 사용할 키를 생성하는 역할
    private static Key getSigningKey(byte[] secretKey){
        return Keys.hmacShaKeyFor(secretKey);
    }


    //refreshToken 생성
}
