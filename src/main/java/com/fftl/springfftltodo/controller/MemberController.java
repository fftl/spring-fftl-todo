package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.Entity.Member;
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
    public ResponseEntity<ApiResponse<String>> getMember(@PathVariable("username") String username){
        Member member = memberService.readUsername(username);
        String getUsername = null;
        String msg = "true";
        HttpStatusCode status = null;

        if(member==null){
            msg = "유저가 존재하지 않습니다";
            status = HttpStatus.NOT_FOUND;
        } else {
            getUsername = member.getUsername();
            status = HttpStatus.OK;
        }

        return new ResponseEntity<>(
                ApiResponse.<String>builder()
                        .status(status.value())
                        .message(msg)
                        .data(getUsername)
                        .build(),
                status);
    }

    /**
     * 멤버 가입
     * @param signUpRequest
     * @return
     */
    @PostMapping()
    public ResponseEntity<?> saveMember(@RequestBody SignUpRequest signUpRequest) {

        //username을 통한 Member확인
        Member findMember = memberService.readUsername(signUpRequest.getUsername());
        if(findMember==null){
            Member member = memberService.create(signUpRequest.getUsername(), signUpRequest.getPassword());
        }
        return new ResponseEntity<>(signUpRequest.getUsername(), HttpStatus.OK);
    }

    /**
     * 멤버 로그인
     * @param loginRequest
     * @return
     */
    @PostMapping("/login")
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
