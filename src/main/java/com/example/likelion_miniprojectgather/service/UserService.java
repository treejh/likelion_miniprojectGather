package com.example.likelion_miniprojectgather.service;


import com.example.likelion_miniprojectgather.config.SecurityConfigProject;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.dto.request.UserRequestDto;
import com.example.likelion_miniprojectgather.jwt.token.JwtTokenizer;
import com.example.likelion_miniprojectgather.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRequestDto userRequestDto){
        User user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));

        userRepository.save(user);
    }

    public void loginUser(UserRequestDto userRequestDto){

    }

    public User findByUserEmail(String email){
        User user = userRepository.findByEmail(email);

        if(user==null){
            throw new NullPointerException("email이 존재하지 않습니다");
        }

        return user;
    }

    public boolean validPassword(String dtoPassword, String userPassword){
        return passwordEncoder.matches(dtoPassword,userPassword);
    }



}
