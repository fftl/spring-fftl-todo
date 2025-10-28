package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.dto.ApiResponse;
import com.fftl.springfftltodo.dto.RoutineRequest;
import com.fftl.springfftltodo.dto.RoutineResponse;
import com.fftl.springfftltodo.service.MemberService;
import com.fftl.springfftltodo.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/routine")
@RequiredArgsConstructor
@RestController
public class RoutineController {

    private final RoutineService routineService;
    private final MemberService memberService;

    @PostMapping
    public ApiResponse<?> save(@RequestBody RoutineRequest request, @AuthenticationPrincipal Integer memberId){

        System.out.println(request);
        Member member = memberService.readMemberId(memberId);
        RoutineResponse routineResponse = routineService.create(member, request);

        return ApiResponse.success(routineResponse);
    }
}
