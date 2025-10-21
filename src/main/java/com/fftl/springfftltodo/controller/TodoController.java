package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.Entity.Todo;
import com.fftl.springfftltodo.dto.ApiResponse;
import com.fftl.springfftltodo.dto.TodoResponse;
import com.fftl.springfftltodo.service.MemberService;
import com.fftl.springfftltodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequestMapping("/todo")
@RequiredArgsConstructor
@RestController
public class TodoController {

    private final TodoService todoService;
    private final MemberService memberService;

    /**
     * 투두 등록
     * @param map
     * @param memberId
     * @return
     */
    @PostMapping()
    public ApiResponse<?> save(@RequestBody Map<String, String> map, @AuthenticationPrincipal Integer memberId){
        Member member = memberService.readMemberId(memberId);
        Todo todo = todoService.create(member, map.get("text"));
        return ApiResponse.success(todo);
    }

    /**
     * 투두 체크
     * @param map
     * @param memberId
     * @return
     */
    @PostMapping("/check")
    public ApiResponse<?> checkTodo(@RequestBody Map<String, Integer> map, @AuthenticationPrincipal Integer memberId){
        boolean check = todoService.checkTodo(map.get("todoId"));
        return ApiResponse.success(check);
    }

    /**
     * 멤버의 투두 전부 가져오기
     * @param memberId
     * @return
     */
    @GetMapping()
    public ApiResponse<?> getAll(@AuthenticationPrincipal Integer memberId){
        Member member = memberService.readMemberId(memberId);
        List<TodoResponse> todoResponses = todoService.readAllDay(member, LocalDate.now());
        return ApiResponse.success(todoResponses);
    }

    /**
     * 특정 날짜의 투두를 전부 가져오기
     * @param memberId
     * @param date
     * @return
     */
    @GetMapping("/day")
    public ApiResponse<?> getOneDay(@AuthenticationPrincipal Integer memberId, @RequestParam(name = "date") LocalDate date){
        Member member = memberService.readMemberId(memberId);
        List<TodoResponse> todoResponses = todoService.readAllDay(member, date);
        return ApiResponse.success(todoResponses);
    }
}
