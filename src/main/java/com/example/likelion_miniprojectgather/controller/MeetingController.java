package com.example.likelion_miniprojectgather.controller;


import com.example.likelion_miniprojectgather.dto.request.MeetingRequestDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingListResponseDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingResponseDto;
import com.example.likelion_miniprojectgather.service.MeetingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/meetings")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;


    @PostMapping
    public ResponseEntity<MeetingResponseDto> createMeeting(@Valid @RequestBody MeetingRequestDto meetingRequestDto){
        MeetingResponseDto meetingResponseDto =  meetingService.createMeeting(meetingRequestDto);
        return ResponseEntity.ok(meetingResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<MeetingListResponseDto>> getMeetings(){
        List<MeetingListResponseDto> meetingListResponseDto =meetingService.getMeetingList();
        return ResponseEntity.ok(meetingListResponseDto);
    }




}
