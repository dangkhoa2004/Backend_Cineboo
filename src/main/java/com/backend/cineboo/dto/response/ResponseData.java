package com.backend.cineboo.dto.response;

import org.springframework.http.HttpStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author 04dkh
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> {

    private HttpStatus status;
    private String msg;
    private T data;
}
