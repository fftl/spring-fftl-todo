package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.config.jwt.JwtProvider;
import com.fftl.springfftltodo.dto.LoginRequest;
import com.fftl.springfftltodo.dto.LoginResponse;
import com.fftl.springfftltodo.dto.SignUpRequest;
import com.fftl.springfftltodo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @GetMapping("/member/{username}")
    public ResponseEntity<String> getMember(@PathVariable("username") String username){
        Member member = memberService.readUsername(username);
        String getUsername = null;
        if(member==null){
            getUsername = "유저가 존재하지 않습니다";
        } else {
            getUsername = member.getUsername();
        }
        return new ResponseEntity<>(getUsername, HttpStatus.OK);
    }

    @PostMapping("/member")
    public ResponseEntity<?> saveMember(@RequestBody SignUpRequest signUpRequest) {

        //username을 통한 Member확인
        Member findMember = memberService.readUsername(signUpRequest.getUsername());
        if(findMember==null){
            Member member = memberService.create(signUpRequest.getUsername(), signUpRequest.getPassword());
        }
        return new ResponseEntity<>(signUpRequest.getUsername(), HttpStatus.OK);
    }

    @PostMapping("/member/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        //username을 통한 Member확인
        Member member = memberService.readUsername(loginRequest.getUsername());

        if(member == null){
            return new ResponseEntity<>("잘못된 정보입니다.", HttpStatus.OK);

        } else {
            if(memberService.login(member, loginRequest.getPassword())){
                String access = jwtProvider.createAccessToken(member.getMemberId(), loginRequest.getUsername());
                String refresh = jwtProvider.createRefreshToken(member.getMemberId(), loginRequest.getUsername());

                memberService.updateRefresh(member, refresh);

                return new ResponseEntity<>(new LoginResponse(loginRequest.getUsername(), access, refresh), HttpStatus.OK);
            } else {

                return new ResponseEntity<>("잘못된 정보입니다.", HttpStatus.OK);
            }
        }
    }


}
