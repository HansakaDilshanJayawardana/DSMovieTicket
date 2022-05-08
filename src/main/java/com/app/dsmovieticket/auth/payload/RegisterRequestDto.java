package com.app.dsmovieticket.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class RegisterRequestDto {

    private String userName;
    private String password;
    private String firstName;
    private String email;
    private String phone;
    private String address;

}
