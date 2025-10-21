package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.config.error.ErrorCode;
import com.fftl.springfftltodo.config.jwt.JwtProvider;
import com.fftl.springfftltodo.dto.*;
import com.fftl.springfftltodo.service.EmailValidService;
import com.fftl.springfftltodo.service.MailService;
import com.fftl.springfftltodo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final MailService mailService;
    private final EmailValidService emailValidService;

    /**
     * 멤버 검색
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public ApiResponse<Boolean> getMember(@PathVariable("username") String username){
        return ApiResponse.success(memberService.existUsername(username));
    }

    /**
     * 멤버 가입
     * @param signUpRequest
     * @return
     */
    @PostMapping()
    public ApiResponse<?> saveMember(@RequestBody SignUpRequest signUpRequest) {
        if(!memberService.existUsername(signUpRequest.getUsername())){
            Member member = memberService.create(signUpRequest.getUsername(), signUpRequest.getPassword());
            return ApiResponse.success(member.getUsername());
        } else {
            return ApiResponse.fail(ErrorCode.DUPLICATE_RESOURCE.getStatus(), "이미 존재하는 유저입니다.");
        }
    }

    /**
     * 멤버 로그인
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
    public ApiResponse<?> login(@RequestBody LoginRequest loginRequest) {

        //username을 통한 Member확인
        Member member = memberService.readUsername(loginRequest.getUsername());

        if(memberService.login(member, loginRequest.getPassword())){
            String access = jwtProvider.createAccessToken(member.getMemberId(), loginRequest.getUsername());
            String refresh = jwtProvider.createRefreshToken(member.getMemberId(), loginRequest.getUsername());

            memberService.updateRefresh(member, refresh);

            return ApiResponse.success(new LoginResponse(loginRequest.getUsername(), access, refresh));
        } else {

            return ApiResponse.fail(ErrorCode.DATA_NOT_FOUND.getStatus(), "로그인 정보가 잘못되었습니다.");
        }
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(@RequestParam(name = "email") String email){
        System.out.println("이메일 출력 >>"+email);
        mailService.sendMimeMessage(email);
        return new ResponseEntity<>(email, HttpStatus.OK);
    }

    @PostMapping("/email/valid")
    public ResponseEntity<EmailValidResponse> validEmail(@RequestParam(name = "email") String email){
        System.out.println("이메일 출력 >>"+ email);
        EmailValidResponse validate = emailValidService.validate(email);
        return new ResponseEntity<>(validate, HttpStatus.OK);
    }
}
