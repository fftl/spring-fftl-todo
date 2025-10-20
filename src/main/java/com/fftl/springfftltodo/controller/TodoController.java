package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.Entity.Todo;
import com.fftl.springfftltodo.dto.TodoResponse;
import com.fftl.springfftltodo.repository.TodoRepository;
import com.fftl.springfftltodo.service.MemberService;
import com.fftl.springfftltodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
import java.time.LocalDate;
import java.util.HashMap;
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
    public ResponseEntity<?> save(@RequestBody Map<String, String> map, @AuthenticationPrincipal Integer memberId){
        Member member = memberService.readMemberId(memberId);
        System.out.println(map.get("text"));
        Todo todo = todoService.create(member, map.get("text"));
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    /**
     * 투두 체크
     * @param map
     * @param memberId
     * @return
     */
    @PostMapping("/check")
    public ResponseEntity<?> checkTodo(@RequestBody Map<String, Integer> map, @AuthenticationPrincipal Integer memberId){
        boolean check = todoService.checkTodo(map.get("todoId"));
        return new ResponseEntity<>(check, HttpStatus.OK);
    }

    /**
     * 멤버의 투두 전부 가져오기
     * @param memberId
     * @return
     */
    @GetMapping()
    public ResponseEntity<?> getAll(@AuthenticationPrincipal Integer memberId){
        Member member = memberService.readMemberId(memberId);
        List<TodoResponse> todoResponses = todoService.readAll(member);
        return new ResponseEntity<>(todoResponses, HttpStatus.OK);
    }

    @GetMapping("/day")
    public ResponseEntity<?> getOneDay(@AuthenticationPrincipal Integer memberId, @RequestParam(name = "date") LocalDate date){
        Member member = memberService.readMemberId(memberId);
        List<TodoResponse> todoResponses = todoService.readAllDay(member, date);
        return new ResponseEntity<>(todoResponses, HttpStatus.OK);
    }
}
