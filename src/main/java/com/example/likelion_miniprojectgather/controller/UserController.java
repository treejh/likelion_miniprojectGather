package com.example.likelion_miniprojectgather.controller;


import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.dto.request.UserRequestDto;
import com.example.likelion_miniprojectgather.dto.response.user.UserLoginResponseDto;
import com.example.likelion_miniprojectgather.jwt.token.JwtTokenizer;
import com.example.likelion_miniprojectgather.service.JwtBlacklistService;
import com.example.likelion_miniprojectgather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;
    private final JwtBlacklistService jwtBlacklistService;


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRequestDto userRequestDto, HttpServletResponse response){
        userService.registerUser(userRequestDto);
        return ResponseEntity.ok().build();

    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(HttpServletResponse response,@RequestBody UserRequestDto userRequestDto){
        User user = userService.findByUserEmail(userRequestDto.getEmail());

        if(!userService.validPassword(userRequestDto.getPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtTokenizer.createAccessToken(user.getId(),user.getEmail());
        String refreshToken = jwtTokenizer.refreshAccessToken(user.getId(),user.getEmail());



        //만약 쿠키로 저장하고 싶다면 ?
        Cookie accessTokenCookie = new Cookie("accessToken",accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");

        accessTokenCookie.setMaxAge(Math.toIntExact(JwtTokenizer.ACCESS_TOKEN_EXPIRE_COUNT/1000));

        response.addCookie(accessTokenCookie);

        UserLoginResponseDto loginResponseDto=UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .userId(user.getId())
                .email(user.getEmail())
                .build();

        return ResponseEntity.ok(loginResponseDto);

    }

    //refresh token이 아닌, accessToken 넣었음
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Authorization header is missing or invalid");
        }

        String token = authorization.substring(7); // "Bearer " 제거
        jwtBlacklistService.blacklistToken(token); // 토큰 블랙리스트에 추가

        return ResponseEntity.ok("Logged out successfully");
    }



}
