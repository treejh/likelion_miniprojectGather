package com.example.likelion_miniprojectgather.service;


import com.example.likelion_miniprojectgather.domain.Meeting;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserMeeting;
import com.example.likelion_miniprojectgather.dto.request.MeetingRequestDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingListResponseDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingResponseDto;
import com.example.likelion_miniprojectgather.repository.MeetingRepository;
import com.example.likelion_miniprojectgather.repository.UserMeetingRepository;
import jakarta.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserMeetingRepository userMeetingRepository;
    private final UserService userService;
    private final TokenService tokenService;

    @Transactional
    public MeetingResponseDto createMeeting(MeetingRequestDto meetingRequestDto){
        Meeting meeting = Meeting.builder()
                .name(meetingRequestDto.getName())
                .description(meetingRequestDto.getDescription())
                .maxParticipants(meetingRequestDto.getMaxParticipants())
                .build();
        User user = userService.findByUserEmail(tokenService.getEmailFromToken());

        meeting.setUser(user);

        meetingRepository.save(meeting);
        userMeetingRepository.save(new UserMeeting(user,meeting));

        return MeetingResponseDto.builder()
                .name(meeting.getName())
                .description(meeting.getDescription())
                .maxParticipants(meetingRequestDto.getMaxParticipants())
                .build();
    }

    @Transactional
    public List<MeetingListResponseDto> getMeetingList(){
        List<Meeting> meetingList = meetingRepository.findAll();
        List<MeetingListResponseDto> meetingListDto = new ArrayList<>();

        for(Meeting meeting : meetingList){
            meetingListDto.add(
                    MeetingListResponseDto.builder()
                            .id(meeting.getId())
                            .name(meeting.getName())
                            .description(meeting.getDescription())
                            .maxParticipants(meeting.getMaxParticipants())
                            .currentParticipants(Long.valueOf(meeting.getUserList().size()))
                            .build()
            );

        }

        return meetingListDto;
    }

    public MeetingResponseDto updateMeeting(Long meetingId,MeetingResponseDto meetingResponseDto){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() ->  new NullPointerException("모임이 존재 하지 않습니다"+ meetingId));

        meeting.setDescription(meetingResponseDto.getDescription());
        meeting.setName(meetingResponseDto.getName());
        meeting.setMaxParticipants(meetingResponseDto.getMaxParticipants());

        meetingRepository.save(meeting);

        return MeetingResponseDto.builder()
                .name(meeting.getName())
                .description(meeting.getDescription())
                .maxParticipants(meeting.getMaxParticipants())
                .build();
    }



}
