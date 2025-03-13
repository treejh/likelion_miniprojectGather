package com.example.likelion_miniprojectgather.controller;


import com.example.likelion_miniprojectgather.dto.request.ScheduleRequestDto;
import com.example.likelion_miniprojectgather.dto.response.schedule.ScheduleResponseDto;
import com.example.likelion_miniprojectgather.service.ScheduleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/meetings/{meetingId}/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto,
                                         @PathVariable("meetingId") Long meetingId){
    scheduleService.createSchedule(meetingId, scheduleRequestDto);
    return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getSchedules(@PathVariable("meetingId") Long meetingId){

        return ResponseEntity.ok(scheduleService.getScheduleList(meetingId));
    }


}
