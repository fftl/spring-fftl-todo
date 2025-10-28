package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.dto.ApiResponse;
import com.fftl.springfftltodo.dto.RoutineRequest;
import com.fftl.springfftltodo.dto.RoutineResponse;
import com.fftl.springfftltodo.dto.TodoResponse;
import com.fftl.springfftltodo.service.MemberService;
import com.fftl.springfftltodo.service.RoutineService;
import com.fftl.springfftltodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/routine")
@RequiredArgsConstructor
@RestController
public class RoutineController {

    private final RoutineService routineService;
    private final TodoService todoService;
    private final MemberService memberService;

    @PostMapping
    public ApiResponse<?> save(@RequestBody RoutineRequest request, @AuthenticationPrincipal Integer memberId){

        Member member = memberService.readMemberId(memberId);
        RoutineResponse routineResponse = routineService.create(member, request);

        return ApiResponse.success(routineResponse);
    }

    @GetMapping
    public ApiResponse<?> get(@AuthenticationPrincipal Integer memberId){

        Member member = memberService.readMemberId(memberId);
        List<RoutineResponse> routineResponses = routineService.readRoutine(member);

        return ApiResponse.success(routineResponses);
    }

    @PostMapping("/make")
    public ApiResponse<?> makeTodo(@RequestBody Map<String, String> map, @AuthenticationPrincipal Integer memberId){

        int routineId = Integer.parseInt(map.get("routineId"));
        Member member = memberService.readMemberId(memberId);
        List<TodoResponse> todoes = routineService.createTodoes(member, routineId);

        return ApiResponse.success(todoes);
    }
}
