package com.demo.shortenUrl.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvalidUrlError {
    private String field;
    private String value;
    private String message;

}
