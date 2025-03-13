package com.example.likelion_miniprojectgather.service;


import com.example.likelion_miniprojectgather.domain.Meeting;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserMeeting;
import com.example.likelion_miniprojectgather.dto.request.MeetingRequestDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingListResponseDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingResponseDto;
import com.example.likelion_miniprojectgather.dto.response.user.UserJoinResponseDto;
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
        User createUser = userService.findByUserEmail(tokenService.getEmailFromToken());

        Meeting meeting = Meeting.builder()
                .name(meetingRequestDto.getName())
                .description(meetingRequestDto.getDescription())
                .maxParticipants(meetingRequestDto.getMaxParticipants())
                .user(createUser)
                .build();


        meetingRepository.save(meeting);
        userMeetingRepository.save(new UserMeeting(createUser,meeting));

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

        //meeting에 참여한 유저 List 가지고오기
        List<User> joinUsers = userMeetingRepository.findUserByMeetingId(meetingId).get();

        //이미 참여한 유저인지 확인하는 메서드
        if(UserAlreadyJoin(currentUser,joinUsers)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이미 모임에 참여한 유저입니다.");
        }

        userMeetingRepository.save(UserMeeting.builder()
                .user(currentUser)
                .meeting(meeting)
                .build());
    }

    //만약 이미 참여한 유저라면 xx 다시 참여 안해도 됨
    public boolean UserAlreadyJoin(User currentUser,List<User> joinUsers){
        return joinUsers.contains(currentUser);
    }

    @Transactional
    public List<UserJoinResponseDto> getUsersJoin(Long meetingId){
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "모임이 존재하지 않습니다. "));
        List<UserJoinResponseDto> userList = new ArrayList<>();
        //고민
        //meeting에 있는 oneToMany에 있는 getUserList를 사용하는 것이 맞는가 -> getUsersJoin
        //아니면 userMeetingDatabase에서 데이터를 받아오는게 맞는가 -> getUsersJoin
        List<UserMeeting> userMeetings = meeting.getUserList();


        for(UserMeeting data : userMeetings){
            userList.add(UserJoinResponseDto.builder()
                    .email(data.getUser().getEmail())
                    .userId(data.getUser().getId())
                    .build());
        }

        return userList;

    }

    @Transactional
    public Meeting findByMeetingId(Long meetingId){
        return meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "모임이 존재하지 않습니다. "));
    }

    @Transactional
    public void leaveMeeting(Long meetingId){
        User currentUser = userService.findUserById(tokenService.getIdFromToken());
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "모임이 존재하지 않습니다. "));

        UserMeeting userMeeting = userMeetingRepository.findByMeetingIdAndUserID(currentUser.getId(),meeting.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 모임에 참여한 유저가 아닙니다.  "));

        userMeetingRepository.delete(userMeeting);

        //만약 현재 미팅에 사람이 없으면 미팅도 같이 삭제된다.
        if(meeting.getUserList().isEmpty()){
         meetingRepository.delete(meeting);
        }

    }

}
