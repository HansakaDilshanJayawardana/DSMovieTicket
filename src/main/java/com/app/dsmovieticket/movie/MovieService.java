package com.app.dsmovieticket.movie;

import com.app.dsmovieticket.auth.UserService;
import com.app.dsmovieticket.common.FieldValidationException;
import com.app.dsmovieticket.movie.payload.MovieCreateRequest;
import com.app.dsmovieticket.utility.MovieTypes;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserService userService;

    //Get Movie Type List Method
    public ArrayList<String> movieTypeList() {
        ArrayList<String> movieTypes = new ArrayList<>();
        movieTypes.add(MovieTypes.SINHALA);
        movieTypes.add(MovieTypes.ENGLISH);
        movieTypes.add(MovieTypes.HINDI);
        movieTypes.add(MovieTypes.TAMIL);
        movieTypes.add(MovieTypes.TELUGU);

        return movieTypes;
    }

    //Add Movie Method
    public MovieEntity addMovie(MovieCreateRequest movieCreateRequest, String username) {
        MovieEntity passMovie = MovieEntity.builder()
                .movieName(movieCreateRequest.getMovieName())
                .showTime(movieCreateRequest.getShowTime())
                .description(movieCreateRequest.getDescription())
                .casts(movieCreateRequest.getCasts())
                .banner(movieCreateRequest.getBanner())
                .category(movieCreateRequest.getMovieType())
                .ticketPrice(movieCreateRequest.getTicketPrice())
                .totTickets(movieCreateRequest.getTotTickets())
                .build();

        passMovie.setCreatedBy(userService.getAuthUserId(username));
        passMovie.setCreatedAt(new Date());

        return movieRepository.save(passMovie);
    }

    //Get All Movies Method
    public List getAllMovies() {
        return movieRepository.findAll();
    }

    //Get Movie by Name Method
    public List getMovieByName(String searchKey) {
        return movieRepository.findByMovieNameContainingIgnoreCase(searchKey);
    }

    //View Movie by ID Method
    public MovieEntity viewMovieById(long id) {
        return movieRepository.getById(id);
    }

    //Update Movie Method
    public MovieEntity updateMovie(MovieCreateRequest movieCreateRequest, long id, String username) {
        MovieEntity passMovie = movieRepository.findById(id).orElseThrow(()->new RuntimeException("Movie not found"));

        if (passMovie.getCreatedBy() != userService.getAuthUserId(username)) {
            throw new FieldValidationException("Current User have No Authorize to Update");
        }

        passMovie.setMovieName(movieCreateRequest.getMovieName());
        passMovie.setShowTime(movieCreateRequest.getShowTime());
        passMovie.setDescription(movieCreateRequest.getDescription());
        passMovie.setCasts(movieCreateRequest.getCasts());
        passMovie.setBanner(movieCreateRequest.getBanner());
        passMovie.setCategory(movieCreateRequest.getMovieType());
        passMovie.setTicketPrice(movieCreateRequest.getTicketPrice());
        passMovie.setTotTickets(movieCreateRequest.getTotTickets());

        passMovie.setModifiedBy(userService.getAuthUserId(username));
        passMovie.setModifiedAt(new Date());

        return movieRepository.save(passMovie);
    }

    //Delete Movie Method
    public void deleteMovie(long id, String username) {
        MovieEntity passMovie = movieRepository.findById(id).orElseThrow(()->new RuntimeException("Movie not Found"));

        if (passMovie.getCreatedBy() != userService.getAuthUserId(username)) {
            throw new FieldValidationException("Illegal Action");
        }

        movieRepository.delete(passMovie);
    }

}
