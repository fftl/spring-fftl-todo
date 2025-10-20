package com.fftl.springfftltodo.controller;

import com.fftl.springfftltodo.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/Routine")
@RequiredArgsConstructor
@RestController
public class RoutineController {

    private final RoutineService routineService;

}
