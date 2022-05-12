package com.app.dsmovieticket.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieTicketCartRepository extends JpaRepository<MovieTicketCartEntity, Long> {

    List<MovieTicketCartEntity> findAllByTicketCartId(long id);
    List<MovieTicketCartEntity> findAllByTicketCartIdId(long id);
    Optional<MovieTicketCartEntity> findAllByTicketCartIdIdAndMovieIdId(long id, long movieId);

}