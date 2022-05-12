package com.app.dsmovieticket.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartMovieTicketRepository extends JpaRepository<CartMovieTicketEntity, Long> {

    List<CartMovieTicketEntity> findAllByTicketCartId(long id);
    List<CartMovieTicketEntity> findAllByTicketCartIdId(long id);
    Optional<CartMovieTicketEntity> findAllByTicketCartIdIdAndMovieIdId(long id, long movieId);

}