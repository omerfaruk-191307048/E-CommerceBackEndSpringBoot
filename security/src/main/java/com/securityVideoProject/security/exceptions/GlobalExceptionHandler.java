package com.securityVideoProject.security.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//Firlatilan hatalarin programin akisini bozmamasi ve istemciye istenmeyen sonuclar dondurmemesi icin
//bu olusturdugumuz sinif ile projemizdeki tum controller siniflarindan firlatilan istisnalari dinlemesini saglayacagiz
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
