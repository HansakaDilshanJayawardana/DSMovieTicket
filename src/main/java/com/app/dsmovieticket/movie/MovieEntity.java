package com.app.dsmovieticket.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_movie")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "movie_name", nullable = false)
    private String movieName;

    @Column(name = "show_time", nullable = false)
    private LocalDateTime expDate;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "casts", nullable = false)
    private String casts;

    @Column(name = "banner", nullable = true)
    private String banner;

    @Column(name = "movie_type", nullable = false)
    private String category;

    @Column(name = "ticket_price", nullable = false)
    @Min(0)
    private float ticketPrice;

    @Column(name = "total_tickets", nullable = false)
    @Min(0)
    private int totTickets;

    //Ticket Add Method
    public void add(int total_tickets){
        this.totTickets += total_tickets;
    }

    //Ticket Subtraction Method
    public void sub(int total_tickets){
        if((this.totTickets - total_tickets) < 0){
            throw new RuntimeException("Invalid No of Tickets");
        }else{
            this.totTickets -= total_tickets;
        }
    }

}
