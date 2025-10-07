package com.fftl.springfftltodo.repository;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    List<Todo> findAllByMember(Member member);
    List<Todo> findAllByMemberAndDate(Member member, LocalDate date);
}
