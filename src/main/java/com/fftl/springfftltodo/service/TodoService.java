package com.fftl.springfftltodo.service;

import com.fftl.springfftltodo.Entity.Member;
import com.fftl.springfftltodo.Entity.Todo;
import com.fftl.springfftltodo.dto.TodoResponse;
import com.fftl.springfftltodo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public Todo create(Member member, String s){
        return todoRepository.save(new Todo(s, member));
    }

    public List<TodoResponse> readAll(Member member){
        List<Todo> todos = todoRepository.findAllByMember(member);
        return todos.stream()
                .map(TodoResponse::fromEntity)
                .toList();
    }

    public List<TodoResponse> readAllDay(Member member, LocalDate date){
        List<Todo> todos = todoRepository.findAllByMemberAndDate(member, date);
        return todos.stream()
                .map(TodoResponse::fromEntity)
                .toList();
    }

    public boolean checkTodo(int todoId){
        Todo todo = todoRepository.findById(todoId).orElse(null);
        if(todo != null){
            todo.checkTodo();
            todoRepository.save(todo);
            return true;
        } else {
            return false;
        }
    }

    public boolean delete(int todoId){
        Todo todo = todoRepository.findById(todoId).orElse(null);
        if(todo != null){
            todoRepository.delete(todo);
            return true;
        } else {
            return false;
        }
    }
}
