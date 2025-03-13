package com.example.likelion_miniprojectgather.service;


import com.example.likelion_miniprojectgather.domain.Meeting;
import com.example.likelion_miniprojectgather.domain.Schedule;
import com.example.likelion_miniprojectgather.domain.User;
import com.example.likelion_miniprojectgather.domain.UserSchedule;
import com.example.likelion_miniprojectgather.dto.request.ScheduleRequestDto;
import com.example.likelion_miniprojectgather.enumData.StatusEnum;
import com.example.likelion_miniprojectgather.repository.ScheduleRepository;
import com.example.likelion_miniprojectgather.repository.UserScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final UserService userService;
    private final TokenService tokenService;


    @Transactional
    public void createSchedule(Long meetingId , ScheduleRequestDto scheduleRequestDto){
        User currentuser = userService.findUserById(tokenService.getIdFromToken());
        Schedule schedule = scheduleRepository.save(Schedule.builder()
                .user(currentuser)
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
}
