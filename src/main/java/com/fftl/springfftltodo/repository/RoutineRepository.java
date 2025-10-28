package com.fftl.springfftltodo.repository;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.Entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Integer> {
    List<Routine> findByMember(Member member);
}
