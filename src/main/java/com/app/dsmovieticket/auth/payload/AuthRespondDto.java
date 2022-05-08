package com.app.dsmovieticket.auth.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthRespondDto {

    private String jwtToken;
    private String userName;
    private String role;

}
