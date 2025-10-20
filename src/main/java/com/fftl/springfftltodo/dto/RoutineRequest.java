package com.fftl.springfftltodo.dto;

import java.util.ArrayList;

public record RoutineRequest (
        String routineName,
        ArrayList<String> todos
){}
