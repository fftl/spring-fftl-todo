package com.fftl.springfftltodo.service;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.Entity.Routine;
import com.fftl.springfftltodo.Entity.Todo;
import com.fftl.springfftltodo.dto.RoutineRequest;
import com.fftl.springfftltodo.dto.RoutineResponse;
import com.fftl.springfftltodo.dto.RoutineTodosResponse;
import com.fftl.springfftltodo.dto.TodoResponse;
import com.fftl.springfftltodo.repository.RoutineRepository;
import com.fftl.springfftltodo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final TodoRepository todoRepository;

    public RoutineResponse create(Member member, RoutineRequest request) {
        System.out.println(request);
        Routine routine = routineRepository.save(new Routine(request.routineName(), request.todos(), member));

        System.out.println("루틴 이름입니다 >>" +routine.getRoutineName());
        return new RoutineResponse(routine.getRoutineName(), routine.getRoutineId());
    }

    public List<TodoResponse> createTodoes(Member member, int routineId) {
        Routine routine = routineRepository.findById(routineId).orElse(null);
        List<TodoResponse> todoResponses = new ArrayList<>();

        for (String s : routine.getTodoList()) {
            todoResponses.add(TodoResponse.fromEntity(new Todo(s, member)));
        }

        return todoResponses;
    }

    public List<RoutineResponse> readRoutine(Member member){
        List<Routine> routines = routineRepository.findByMember(member);
        List<RoutineResponse> routineResponses = new ArrayList<>();
        for(Routine r : routines){
            routineResponses.add(new RoutineResponse(r.getRoutineName(), r.getRoutineId()));
        }
        return routineResponses;
    }

    public boolean delete(int routineId){
        Routine routine = routineRepository.findById(routineId).orElse(null);

        if(routine != null){
            routineRepository.delete(routine);
            return true;
        }

        return false;
    }
}
