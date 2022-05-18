package com.app.dsmovieticket.cart;

import com.app.dsmovieticket.auth.UserService;
import com.app.dsmovieticket.cart.payload.AddMovieTicketCartRequest;
import com.app.dsmovieticket.cart.payload.MovieTicketCartRespond;
import com.app.dsmovieticket.cart.payload.RemoveMovieTicketCartRequest;
import com.app.dsmovieticket.common.NotFoundException;
import com.app.dsmovieticket.common.Status;
import com.app.dsmovieticket.movie.MovieEntity;
import com.app.dsmovieticket.movie.MovieRepository;
import com.app.dsmovieticket.movie.MovieService;
import com.app.dsmovieticket.payment.PaymentType;
import com.app.dsmovieticket.sms.SMSKeyEntity;
import com.app.dsmovieticket.sms.SMSKeyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingTicketCartService {

    private final MovieService movieService;
    private final UserService userService;
    private final MovieTicketCartRepository movieTicketCartRepository;
    private final BookingTicketCartRepository bookingTicketCartRepository;
    private final MovieRepository movieRepository;
    private final SMSKeyRepository smsKeyRepository;
//    private final SmsGateway smsHandler;

    public MovieTicketCartRespond viewBookingTicketCart(String username) {
        BookingTicketCartEntity shoppingTicketCart = bookingTicketCartRepository.findByUserIdAndPurchase(userService.getAuthUserId(username), false)
                .orElse(new BookingTicketCartEntity());

        if (shoppingTicketCart.getUserId() <= 0) {
            return MovieTicketCartRespond.builder()
                    .movieTicketCartId(shoppingTicketCart.getId())
                    .ticketEntities(new ArrayList<>())
                    .build();
        } else {
            List<MovieTicketCartEntity> ticketEntities = movieTicketCartRepository.findAllByTicketCartIdId(shoppingTicketCart.getId());
            return MovieTicketCartRespond.builder()
                    .movieTicketCartId(shoppingTicketCart.getId())
                    .ticketEntities(ticketEntities)
                    .build();
        }
    }

    @Transactional
    public MovieTicketCartEntity addMovieTicketToCart(AddMovieTicketCartRequest movieTicketRequest, String username) {
        BookingTicketCartEntity ticketCart = bookingTicketCartRepository.findByIdAndPurchase(userService.getAuthUserId(username), false)
                .orElse(new BookingTicketCartEntity());

        if (ticketCart.getUserId() <= 0) {
            ticketCart.setUserId(userService.getAuthUserId(username));
            ticketCart.setCreatedAt(new Date());
            ticketCart.setCreatedBy(userService.getAuthUserId(username));

            ticketCart = bookingTicketCartRepository.save(ticketCart);

            MovieEntity movie = movieService.viewMovieById(movieTicketRequest.getMovieId());
            movie.sub(movieTicketRequest.getNoOfTickets());

            movie = movieRepository.save(movie);

            MovieTicketCartEntity movieTicketCartToBeAdd = MovieTicketCartEntity.builder()
                    .ticketCartId(ticketCart)
                    .movieId(movie)
                    .noOfTickets(movieTicketRequest.getNoOfTickets())
                    .build();

            movieTicketCartToBeAdd = movieTicketCartRepository.save(movieTicketCartToBeAdd);
            return movieTicketCartToBeAdd;
        } else {
            if (ticketCart.getStatus().equals(Status.TicketCart.PENDINGFORCONF)) {
                throw new RuntimeException("Please Confirm Your Payment");
            }

            ticketCart.setModifiedAt(new Date());
            ticketCart.setModifiedBy(userService.getAuthUserId(username));

            ticketCart = bookingTicketCartRepository.save(ticketCart);

            List<MovieTicketCartEntity> moviesInCart = movieTicketCartRepository.findAllByTicketCartIdId(ticketCart.getId());

            MovieTicketCartEntity movieEntity = moviesInCart.stream()
                    .filter(element -> element.getMovieId().getId() == movieTicketRequest.getMovieId())
                    .findAny()
                    .orElse(null);

            if (movieEntity == null) {
                MovieEntity movie = movieRepository.findById(movieTicketRequest.getMovieId())
                        .orElseThrow(() -> new NotFoundException("Movie not Found"));
                movie.sub(movieTicketRequest.getNoOfTickets());

                movie = movieRepository.save(movie);

                MovieTicketCartEntity movieTicketCartToBeAdd = MovieTicketCartEntity.builder()
                        .ticketCartId(ticketCart)
                        .movieId(movie)
                        .noOfTickets(movieTicketRequest.getNoOfTickets())
                        .build();

                return movieTicketCartRepository.save(movieTicketCartToBeAdd);
            } else {
                MovieEntity movie = movieEntity.getMovieId();
                movie.add(movieEntity.getNoOfTickets());
                movie.sub(movieTicketRequest.getNoOfTickets());

                movie = movieRepository.save(movie);

                movieEntity.setMovieId(movie);
                movieEntity.setNoOfTickets(movieTicketRequest.getNoOfTickets());

                return movieTicketCartRepository.save(movieEntity);
            }
        }
    }

    @Transactional
    public void removeMovieTicketFromCart(RemoveMovieTicketCartRequest movieTicketRequest, String username) {
        BookingTicketCartEntity ticketCart = bookingTicketCartRepository.findByUserIdAndPurchase(userService.getAuthUserId(username), false)
                .orElseThrow(()->new NotFoundException("Movie not Found"));

        if (ticketCart.getStatus().equals(Status.TicketCart.PENDINGFORCONF)) {
            throw new RuntimeException("Please Confirm Your Payment");
        }

        MovieTicketCartEntity movieTicketCart = movieTicketCartRepository.findAllByTicketCartIdIdAndMovieIdId(ticketCart.getId(), movieTicketRequest.getMovieId())
                .orElseThrow(()->new NotFoundException("Movie Ticket Cart not Found"));

        MovieEntity movie = movieRepository.findById(movieTicketRequest.getMovieId()).get();
        movie.add(movieTicketCart.getNoOfTickets());
        movieRepository.save(movie);

        if (movieTicketCartRepository.findAllByTicketCartIdId(movieTicketCart.getId()).stream().count() == 0) {
            bookingTicketCartRepository.delete(ticketCart);
        }
    }

    //TODO: Have to Complete SMS Connection in UpdatePayment
    public void updatePayment(long movieTicketCartId, float amount, String type, String phone) {
        BookingTicketCartEntity ticketCart = bookingTicketCartRepository.findById(movieTicketCartId)
                .orElseThrow(()->new NotFoundException("Requested Ticket Cart not Found"));

        if (ticketCart.getStatus().equals(Status.TicketCart.PENDINGFORCONF)) {
            throw new RuntimeException("Please Confirm Your Payment");
        }

        double movieTicketCartPrice = movieTicketCartRepository.findAllByTicketCartIdId(movieTicketCartId)
                .stream()
                .mapToDouble(element -> element.getMovieId().getTicketPrice() * element.getNoOfTickets())
                .sum();

        if ((float) movieTicketCartPrice == amount) {
            if (type.equals(PaymentType.CARD)) {
                ticketCart.setStatus(Status.TicketCart.PAYMENTDONE);
                ticketCart.setPurchase(true);
            } else {
                ticketCart.setStatus(Status.TicketCart.PENDINGFORCONF);

                SMSKeyEntity smsKey = SMSKeyEntity.builder()
                        .cartId(ticketCart.getId())
                        .phone(phone)
//                        .key(smsHandler.generateSmsKey())
                        .build();
                smsKey = smsKeyRepository.save(smsKey);
//                smsHandler.sendConfMessage(phone, smsKey.getKey());
            }
            ticketCart.setModifiedAt(new Date());
            bookingTicketCartRepository.save(ticketCart);
        } else {
            throw new RuntimeException("Ticket Cart Values does not Match");
        }
    }

    public List<BookingTicketCartEntity> viewPurchaseHistory(String username) {
        return bookingTicketCartRepository.findAllByUserId(userService.getAuthUserId(username));
    }
    
}