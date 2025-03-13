package com.example.likelion_miniprojectgather.service;


import com.example.likelion_miniprojectgather.domain.Meeting;
import com.example.likelion_miniprojectgather.domain.Schedule;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserMeeting;
import com.example.likelion_miniprojectgather.domain.UserSchedule;
import com.example.likelion_miniprojectgather.dto.request.ScheduleRequestDto;
import com.example.likelion_miniprojectgather.dto.response.meeting.MeetingListResponseDto;
import com.example.likelion_miniprojectgather.dto.response.schedule.ScheduleAttendDto;
import com.example.likelion_miniprojectgather.dto.response.schedule.ScheduleResponseDto;
import com.example.likelion_miniprojectgather.enumData.StatusEnum;
import com.example.likelion_miniprojectgather.repository.ScheduleRepository;
import com.example.likelion_miniprojectgather.repository.UserScheduleRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceConfigurationError;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final UserService userService;
    private final MeetingService meetingService;
    private final TokenService tokenService;


    @Transactional
    public void createSchedule(Long meetingId , ScheduleRequestDto scheduleRequestDto){
        User currentuser = userService.findUserById(tokenService.getIdFromToken());
        Meeting currentMeeting = meetingService.findByMeetingId(meetingId);
        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .user(currentuser)
                        .meeting(currentMeeting)
                .date(scheduleRequestDto.getDate())
                .time(scheduleRequestDto.getTime())
                .title(scheduleRequestDto.getTitle())
                .location(scheduleRequestDto.getLocation())
                .build());

        //처음 스케줄을 생성한 사람의 상태는 항상 참여로
        userScheduleRepository.save(UserSchedule.builder()
                .schedule(schedule)
                .user(currentuser)
                .status(StatusEnum.ATTENDING)
                .build());
    }

    @Transactional
    public List<ScheduleResponseDto> getScheduleList(Long meetingId){
        Meeting currentMeeting = meetingService.findByMeetingId(meetingId);
        List<ScheduleResponseDto> responseDto = new ArrayList<>();
        for(Schedule schedule : currentMeeting.getScheduleList() ){
            responseDto.add(ScheduleResponseDto.builder()
                    .id(schedule.getId())
                    .title(schedule.getTitle())
                    .date(schedule.getDate())
                    .time(schedule.getTime())
                    .location(schedule.getLocation())
                    .build()
            );
        }

        return responseDto;

    }

    @Transactional
    public void joinSchedule(Long meetingId,Long scheduleId){
        User currentuser = userService.findUserById(tokenService.getIdFromToken());
        Meeting currentMeeting = meetingService.findByMeetingId(meetingId);

        //미팅에 참여한 유저를 가지고 온다.
        List<UserMeeting> valid = currentuser.getMeetingList();
        for(UserMeeting userMeeting: valid){
            userMeeting.get
        }

        Schedule currentSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "스케줄이 존재하지 않습니다. "));

        if(!currentSchedule.getMeeting().equals(currentMeeting)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 모임에 존재하는 스케줄이 아닙니다. ");
        }

        userScheduleRepository.save(
                UserSchedule.builder()
                        .user(currentuser)
                        .schedule(currentSchedule)
                        .status(StatusEnum.ATTENDING)
                        .build()
        );
    }
    @Transactional
    public void deleteLeaveSchedule(Long meetingId, Long scheduleId){
        UserSchedule userSchedule = userScheduleRepository.findByScheduleIdAndUserId(scheduleId,tokenService.getIdFromToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "스케줄이 존재하지 않습니다. "));

        if(!userSchedule.getSchedule().getMeeting().equals(meetingService.findByMeetingId(meetingId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 모임에 존재하는 스케줄이 아닙니다. ");
        }

        userScheduleRepository.delete(userSchedule);

    }

    @Transactional
    public void notAttendingSchedule(Long meetingId, Long scheduleId){
        UserSchedule userSchedule = userScheduleRepository.findByScheduleIdAndUserId(scheduleId,tokenService.getIdFromToken())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "스케줄이 존재하지 않습니다. "));

        //meeitng이 다르면 해당 미팅에 속한 스케줄이 아니라는 뜻이다.
        if(!userSchedule.getSchedule().getMeeting().equals(meetingService.findByMeetingId(meetingId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 모임에 존재하는 스케줄이 아닙니다. ");
        }

       userSchedule.setStatus(StatusEnum.NOT_ATTENDING);
    }

    @Transactional
    public List<ScheduleAttendDto> getAttendList(Long meetingId, Long scheduleId){
        Schedule currentSchedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "스케줄이 존재하지 않습니다. "));

        //meeitng이 다르면 해당 미팅에 속한 스케줄이 아니라는 뜻이다.
        if(!currentSchedule.getMeeting().equals(meetingService.findByMeetingId(meetingId))){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "해당 모임에 존재하는 스케줄이 아닙니다. ");
        }
        List<ScheduleAttendDto> scheduleAttendDtoList = new ArrayList<>();

        List<User> userList = userScheduleRepository.findByScheduleIdAndStatus(scheduleId,StatusEnum.ATTENDING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "스케줄에 유저가 존재하지 않습니다.  "));

        //.orElse(Collections.emptyList());


        for(User user : userList){
            scheduleAttendDtoList.add(
                    ScheduleAttendDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .build());

        }

        return scheduleAttendDtoList;

    }



}
