package com.example.likelion_miniprojectgather.controller;


import com.example.likelion_miniprojectgather.dto.request.MeetingRequestDto;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meetings")
public class MeetingController {


    @PostMapping
    public void createMeeting(@Valid @RequestBody MeetingRequestDto meetingRequestDto){


    }

    @GetMapping
    public String test(){
        return "test";
    }


}
