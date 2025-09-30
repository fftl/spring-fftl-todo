package com.fftl.springfftltodo.service;

import com.fftl.springfftltodo.Entity.Member;
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

    public Member readUsername(String username){
        Member member = memberRepository.findByUsername(username);
        if(member != null) return member;

        return null; //TODO 에러 처리 필요
    }

    public Member readMemberId(int memberId){
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member != null) return member;

        return null; //TODO 에러 처리 필요
    }

    public void updateRefresh(Member member, String refresh){
        member.updateRefresh(refresh);
        memberRepository.save(member);
    }
}
