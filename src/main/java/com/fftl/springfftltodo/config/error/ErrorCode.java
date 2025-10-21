package com.fftl.springfftltodo.config.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DATA_NOT_FOUND(404, "Data not found"),
    ENTITY_NOT_FOUND(404, "Entity not found"),
    DUPLICATE_RESOURCE(409, "Duplicate resource"),
    INTERNAL_ERROR(500, "Internal error");
    private final int status;
    private final String message;
}

