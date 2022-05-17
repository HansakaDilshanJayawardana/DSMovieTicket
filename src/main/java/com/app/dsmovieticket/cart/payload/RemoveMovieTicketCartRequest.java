package com.app.dsmovieticket.cart.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveMovieTicketCartRequest {

    private long movieId;

}