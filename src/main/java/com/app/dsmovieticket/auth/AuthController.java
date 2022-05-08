package com.app.dsmovieticket.auth;

import com.app.dsmovieticket.auth.exceptions.FieldValidationFailed;
import com.app.dsmovieticket.auth.payload.AuthRequestDto;
import com.app.dsmovieticket.auth.payload.AuthRespondDto;
import com.app.dsmovieticket.auth.payload.RegisterRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    //Sign In
    @PostMapping("/signin")
    public ResponseEntity<AuthRespondDto> login(@RequestBody AuthRequestDto authRequestDto) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDto.getUserName(),
                            authRequestDto.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            //TODO add proper exception
            throw new BadCredentialsException("INVALID_USERNAME_OR_PASSWORD", e);
        }
        return new ResponseEntity<>(userService.loginUser(authRequestDto), HttpStatus.ACCEPTED);
    }

    //Sign Up
    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterRequestDto request){
        //Validations (Controller Level)
        if(request.getUserName().isEmpty()){
            throw new FieldValidationFailed("Username is Required");
        }
        if(request.getPassword().isEmpty()){
            throw new FieldValidationFailed("Password is Required");
        }
        if(request.getFirstName().isEmpty()){
            throw new FieldValidationFailed("First Name is Required");
        }
        if(request.getEmail().isEmpty()){
            throw new FieldValidationFailed("Email is Required");
        }
        if(request.getPhone().isEmpty()){
            throw new FieldValidationFailed("Phone is Required");
        }
        if(request.getPhone().length() != 10){
            throw new FieldValidationFailed("Phone Number Length must be 10");
        }
        if(request.getAddress().isEmpty()){
            throw new FieldValidationFailed("Address is Required");
        }
        return new ResponseEntity<>(userService.registerUser(request) , HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public String testAuth(){
        return "Authentication is OK";
    }

}