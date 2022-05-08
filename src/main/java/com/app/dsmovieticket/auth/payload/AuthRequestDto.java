package com.app.dsmovieticket.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDto {

    private String userName;
    private String password;

}
