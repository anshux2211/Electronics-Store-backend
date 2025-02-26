package com.lcwd.electronic.store.exception;

import com.lcwd.electronic.store.dtos.ApiResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){

            ApiResponseMessage resp= ApiResponseMessage.builder()
                    .message(ex.getMessage())
                    .success(true)
                    .status(HttpStatus.NOT_FOUND)
                    .build();
            return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex){
        List<ObjectError> errorList=ex.getBindingResult().getAllErrors();
        Map<String,Object> resp=new HashMap<>();
        errorList.stream().forEach(error->{
            String field=((FieldError)error).getField();
            String message=error.getDefaultMessage();
            resp.put(field,message);
        });
        return new ResponseEntity<Map<String,Object>>(resp,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> BadApiRequestExceptionHandler(BadApiRequestException ex){
        ApiResponseMessage resp=ApiResponseMessage.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .success(false)
                .build();

        return new ResponseEntity<ApiResponseMessage>(resp,HttpStatus.BAD_REQUEST);
    }
}
