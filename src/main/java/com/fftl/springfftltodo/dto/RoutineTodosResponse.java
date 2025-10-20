package com.fftl.springfftltodo.dto;

import java.util.ArrayList;

public record RoutineTodosResponse(
        ArrayList<TodoResponse> todos
){}
