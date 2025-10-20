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
        Routine routine = new Routine(request.routineName(), request.todos(), member);
        return new RoutineResponse(routine);
    }

    public List<TodoResponse> createTodoes(Member member, int routineId) {
        Routine routine = routineRepository.findById(routineId).orElse(null);
        List<TodoResponse> todoResponses = new ArrayList<>();

        for (String s : routine.getTodoList()) {
            todoResponses.add(TodoResponse.fromEntity(new Todo(s, member)));
        }

        return todoResponses;
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
