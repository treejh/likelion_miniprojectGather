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
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public MeetingResponseDto updateMeeting(Long meetingId,MeetingRequestDto meetingRequestDto){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "모임이 존재하지 않습니다."));

        meeting.setDescription(meetingRequestDto.getDescription());
        meeting.setName(meetingRequestDto.getName());
        meeting.setMaxParticipants(meetingRequestDto.getMaxParticipants());

        meetingRepository.save(meeting);

        return MeetingResponseDto.builder()
                .name(meeting.getName())
                .description(meeting.getDescription())
                .maxParticipants(meeting.getMaxParticipants())
                .build();
    }

    @Transactional
    public void deleteMeeting(Long meetingId) {
        Long userId = tokenService.getIdFromToken();
        User user = userService.findUserById(userId);
        Meeting meeting = meetingRepository.findById(meetingId).
                orElseThrow(()->new NullPointerException("모임이 존재하지 않습니다."));

        if(meeting.getUser().equals(user)){
            meetingRepository.delete(meeting);
        }else{
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "모임을 삭제할 권한이 없습니다.");
        }
    }

    @Transactional
    public void joinMeeting(Long meetingId) {
        //존재하는 모임인지 확인
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "모임이 존재하지 않습니다. "));

        //현재 로그인한 유저 가지고 오기
        User currentUser = userService.findUserById(tokenService.getIdFromToken());

        //joinUsers에 참여한 유저 List 가지고오기
        List<User> joinUsers = userMeetingRepository.findUserByMeetingId(meetingId).get();

        //만약 이미 참여한 유저라면 xx 다시 참여 안해도 됨
        for (User user : joinUsers) {
            if (user.equals(currentUser)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이미 모임에 참여한 유저입니다.");
            }
        }

        UserMeeting userMeeting = UserMeeting.builder()
                .user(currentUser)
                .meeting(meeting)
                .build();

        userMeetingRepository.save(userMeeting);
    }
}
