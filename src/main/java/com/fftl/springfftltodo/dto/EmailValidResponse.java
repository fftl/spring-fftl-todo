package com.fftl.springfftltodo.dto;

public record EmailValidResponse(
    boolean formatValid,
    boolean domainValid,
    boolean mxFound,
    boolean disposable,
    String suggestion
){}
