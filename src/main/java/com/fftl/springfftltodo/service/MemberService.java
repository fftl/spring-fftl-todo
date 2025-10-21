package com.fftl.springfftltodo.service;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.config.error.BusinessException;
import com.fftl.springfftltodo.config.error.ErrorCode;
import com.fftl.springfftltodo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public boolean login(Member member, String password){
        return passwordEncoder.matches(password, member.getPassword());
    }

    public Member create(String username, String password){
        return memberRepository.save(new Member(username, passwordEncoder.encode(password)));
    }

    public boolean existUsername(String username){
        return memberRepository.existsByUsername(username);
    }

    public Member readUsername(String username){
        return memberRepository.findByUsername(username).orElseThrow(() -> new BusinessException(ErrorCode.DATA_NOT_FOUND));
    }

    public Member readMemberId(int memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(ErrorCode.DATA_NOT_FOUND));
    }

    public void updateRefresh(Member member, String refresh){
        member.updateRefresh(refresh);
        memberRepository.save(member);
    }
}
