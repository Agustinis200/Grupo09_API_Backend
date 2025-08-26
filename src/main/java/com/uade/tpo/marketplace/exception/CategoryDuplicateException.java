package com.uade.tpo.marketplace.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "La categoria que se intenta agregar ya existe")
public class CategoryDuplicateException extends Exception {

}
