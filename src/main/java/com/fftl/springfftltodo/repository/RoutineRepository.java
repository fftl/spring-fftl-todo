package com.fftl.springfftltodo.repository;

import com.fftl.springfftltodo.Entity.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoutineRepository extends JpaRepository<Routine, Integer> {
}
