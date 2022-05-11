package com.app.dsmovieticket.movie.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class MovieCreateRequest {

    private String movieName;
    private LocalDateTime showTime;
    private String description;
    private String casts;
    private String banner;
    private String movieType;
    private float ticketPrice;
    private int totTickets;

}
