package com.app.dsmovieticket.movie;

import com.app.dsmovieticket.common.FieldValidationException;
import com.app.dsmovieticket.movie.payload.MovieCreateRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/movie")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/categories")
    public ResponseEntity<ArrayList<String>> categories() {
        return new ResponseEntity<>(movieService.movieTypeList(), HttpStatus.OK);
    }

    @GetMapping("/movie-list")
    public ResponseEntity<List> getMovieList(@RequestParam(name = "name", required = false , defaultValue = "") String searchKey) {
        if (searchKey.isEmpty()) {
            return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(movieService.getMovieByName(searchKey), HttpStatus.OK);
        }
    }

    @PostMapping("/add-movie")
    public ResponseEntity<MovieEntity> addNewMovie(@RequestBody MovieCreateRequest movieCreateRequest , @AuthenticationPrincipal UserDetails user) {
        if (movieCreateRequest.getMovieName().isEmpty()) {
            throw new FieldValidationException("Movie Name is Required");
        }
        if (movieCreateRequest.getTicketPrice() < 0f) {
            throw new FieldValidationException("Invalid Ticket Price");
        }
        if (!movieService.movieTypeList().contains(movieCreateRequest.getMovieType().toUpperCase())) {
            throw new FieldValidationException("Movie Category does not match any existing Category");
        }

        return new ResponseEntity<>(movieService.addMovie(movieCreateRequest, user.getUsername()), HttpStatus.CREATED);
    }
    
    @PutMapping("/edit-movie")
    public ResponseEntity<MovieEntity> updateMovie(@RequestBody MovieCreateRequest movieCreateRequest, @RequestParam(name = "id") long id, @AuthenticationPrincipal UserDetails user) {
        if (movieCreateRequest.getMovieName().isEmpty()) {
            throw new FieldValidationException("Movie Name is Required");
        }
        if (movieCreateRequest.getTicketPrice() < 0f) {
            throw new FieldValidationException("Invalid Ticket Price ");
        }
        if (!movieService.movieTypeList().contains(movieCreateRequest.getMovieType().toUpperCase())) {
            throw new FieldValidationException("Movie Category does not match any existing Category");
        }

        return new ResponseEntity<>(movieService.updateMovie(movieCreateRequest, id, user.getUsername()), HttpStatus.OK);
    }

    @DeleteMapping("/delete-movie")
    public ResponseEntity<Object> deleteMovie(@RequestParam(name = "id") long id, @AuthenticationPrincipal UserDetails user) {
        movieService.deleteMovie(id, user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
