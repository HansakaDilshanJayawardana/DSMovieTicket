package com.app.dsmovieticket.cart;

import com.app.dsmovieticket.movie.MovieEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_cart_movie_tickets")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieTicketCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "ticket_cart_id")
    @JsonIgnore
    private BookingTicketCartEntity ticketCartId;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private MovieEntity movieId;

    @Column(name = "no_of_tickets")
    private int noOfTickets;

}