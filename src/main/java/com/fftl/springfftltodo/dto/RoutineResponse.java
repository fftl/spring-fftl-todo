package com.fftl.springfftltodo.dto;

import com.fftl.springfftltodo.Entity.Routine;
import com.fftl.springfftltodo.Entity.Todo;

import java.util.ArrayList;

public record RoutineResponse (
        String routineName, int routineId
){}
