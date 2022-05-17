package com.app.dsmovieticket.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddMovieTicketCartRequest {

    private long movieId;
    private int noOfTickets;

}