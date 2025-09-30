package com.fftl.springfftltodo.dto;

import com.fftl.springfftltodo.Entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class TodoResponse {
    private int todoId;
    private String text;
    private boolean checked;
    private LocalDate date;

    public static TodoResponse fromEntity(Todo todo) {
        return new TodoResponse(
                todo.getTodoId(),
                todo.getText(),
                todo.isChecked(),
                todo.getDate()
        );
    }
}
