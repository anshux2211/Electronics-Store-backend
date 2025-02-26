package com.lcwd.electronic.store.dtos;


import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseMessage {
    private String message;
    private Boolean success;
    private HttpStatus status;
}
