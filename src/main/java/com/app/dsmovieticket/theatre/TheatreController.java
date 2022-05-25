package com.app.dsmovieticket.theatre;

import com.app.dsmovieticket.utility.Theatre;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/theatre")
public class TheatreController {

    @GetMapping("/list")
    public ResponseEntity<ArrayList<String>> getList(){
        return new ResponseEntity<>(Theatre.theatre, HttpStatus.OK);
    }

}