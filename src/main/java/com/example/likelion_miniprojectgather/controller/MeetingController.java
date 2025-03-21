package com.example.likelion_miniprojectgather.controller;


import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.dto.request.MeetingRequestDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingListResponseDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingResponseDto;
import com.example.likelion_miniprojectgather.dto.response.user.UserJoinResponseDto;
import com.example.likelion_miniprojectgather.service.MeetingService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/{meetingId}")
    public ResponseEntity<MeetingResponseDto> updateMeeting(
            @PathVariable("meetingId") Long meetingId,
            @Valid @RequestBody MeetingRequestDto meetingRequestDto){
        MeetingResponseDto meetingResponseDto = meetingService.updateMeeting(meetingId,meetingRequestDto);
        return ResponseEntity.ok(meetingResponseDto);
    }

    @DeleteMapping("/{meetingId}")
    public ResponseEntity deleteMeeting(
            @PathVariable("meetingId") Long meetingId){
        meetingService.deleteMeeting(meetingId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{meetingId}/join")
    public ResponseEntity joinMeeting(@PathVariable("meetingId") Long meetingId){
        meetingService.joinMeeting(meetingId);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/{meetingId}/participants")
    public ResponseEntity<List<UserJoinResponseDto>> getParticipantsList(@PathVariable("meetingId")Long meetingId){
        return ResponseEntity.ok(meetingService.getUsersJoin(meetingId));

    }


    //사용자가 미팅에서 나가고, 미팅의 이용자 수가 0이면 미팅이 삭제된다.
    @DeleteMapping("/{meetingId}/participants")
    public ResponseEntity leaveMeeting(@PathVariable("meetingId")Long meetingId){

        meetingService.leaveMeeting(meetingId);
        return ResponseEntity.ok().build();

    }






}
