package com.example.likelion_miniprojectgather.controller;


import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.dto.request.UserRequestDto;
import com.example.likelion_miniprojectgather.dto.response.UserLoginResponseDto;
import com.example.likelion_miniprojectgather.jwt.token.JwtTokenizer;
import com.example.likelion_miniprojectgather.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenizer jwtTokenizer;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserRequestDto userRequestDto){
        userService.registerUser(userRequestDto);
        return ResponseEntity.ok().build();

    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody UserRequestDto userRequestDto){
        User user = userService.findByUserEmail(userRequestDto.getEmail());

        if(!userService.validPassword(userRequestDto.getPassword(),user.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = jwtTokenizer.createAccessToken(user.getId(),user.getEmail());
        String refreshToken = jwtTokenizer.refreshAccessToken(user.getId(),user.getEmail());

        UserLoginResponseDto loginResponseDto=UserLoginResponseDto.builder()
                .accessToken(accessToken)
                .userId(user.getId())
                .email(user.getEmail())
                .build();


        return ResponseEntity.ok(loginResponseDto);

    }


}
