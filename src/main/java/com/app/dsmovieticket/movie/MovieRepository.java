package com.app.dsmovieticket.movie;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity , Long> {

    List findAllByMovieNameContaining(String searchKey);
    List findByMovieNameContainingIgnoreCase(String searchKey);

}