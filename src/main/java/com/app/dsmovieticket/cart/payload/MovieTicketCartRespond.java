package com.app.dsmovieticket.cart.payload;

import com.app.dsmovieticket.cart.MovieTicketCartEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieTicketCartRespond {

    private long movieTicketCartId;
    private List<MovieTicketCartEntity> ticketEntities = new ArrayList<>();

    //Get Movie Ticket Cart Price Method
    public double getMovieTicketCartPrice() {
        if (!this.ticketEntities.isEmpty()) {
            return this.ticketEntities.stream().mapToDouble(value -> value.getMovieId().getTicketPrice() * value.getNoOfTickets()).sum();
        } else {
            return 0;
        }
    }

}