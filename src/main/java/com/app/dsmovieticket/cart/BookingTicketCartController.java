package com.app.dsmovieticket.cart;

import com.app.dsmovieticket.cart.payload.AddMovieTicketCartRequest;
import com.app.dsmovieticket.cart.payload.MovieTicketCartRespond;
import com.app.dsmovieticket.cart.payload.RemoveMovieTicketCartRequest;
import com.app.dsmovieticket.common.FieldValidationException;
import com.app.dsmovieticket.payment.PaymentService;
import com.app.dsmovieticket.payment.PaymentType;
import com.app.dsmovieticket.payment.payload.PaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/cart")
public class BookingTicketCartController {

    private final BookingTicketCartService bookingTicketCartService;
    private final PaymentService paymentService;

    @PostMapping("/add-movie-ticket-cart")
    public ResponseEntity<MovieTicketCartEntity> addToMovieTicketCart(@RequestBody AddMovieTicketCartRequest movieTicketCartRequest, @AuthenticationPrincipal UserDetails user) {
        if (movieTicketCartRequest.getMovieId() <= 0) {
            throw new FieldValidationException("Give Proper Movie ID");
        }
        if (movieTicketCartRequest.getNoOfTickets() < 0) {
            throw new FieldValidationException("Give Proper No of Tickets");
        }

        return new ResponseEntity<>(bookingTicketCartService.addMovieTicketToCart(movieTicketCartRequest, user.getUsername()), HttpStatus.CREATED);
    }

    @GetMapping("/movie-ticket-cart-list")
    public ResponseEntity<MovieTicketCartRespond> viewMovieTicketCart(@AuthenticationPrincipal UserDetails user) {
        return new ResponseEntity<>(bookingTicketCartService.viewBookingTicketCart(user.getUsername()), HttpStatus.OK);
    }

    @DeleteMapping("/delete-cart-movie")
    public ResponseEntity<Object> deleteFromMovieTicketCart(@RequestBody RemoveMovieTicketCartRequest ticketCartRequest, @AuthenticationPrincipal UserDetails user) {
        if (ticketCartRequest.getMovieId() <= 0) {
            throw new FieldValidationException("Movie ID is Required");
        }

        bookingTicketCartService.removeMovieTicketFromCart(ticketCartRequest, user.getUsername());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchase(@RequestBody PaymentRequest paymentRequest, @AuthenticationPrincipal UserDetails user) {
        if (paymentRequest.getPaymentType().isEmpty()) {
            throw new FieldValidationException("Payment Type is Required");
        }

        if (paymentRequest.getPaymentType().equals(PaymentType.CARD)) {
            if (paymentRequest.getCardNumber().length() != 16) {
                throw new FieldValidationException("Invalid Card Number");
            }
            if (paymentRequest.getCsv().length() != 3) {
                throw new FieldValidationException("Invalid CSV");
            }
        }

        if (paymentRequest.getCartId() <= 0) {
            throw new FieldValidationException("Card ID is Required");
        }

        if (paymentRequest.getAmount() <= 0) {
            throw new FieldValidationException("Invalid Amount");
        }

        return new ResponseEntity<>(paymentService.pay(paymentRequest,user.getUsername()), HttpStatus.OK);
    }

    @GetMapping("/past-bookings")
    public List<BookingTicketCartEntity> viewPurchaseHistory(@AuthenticationPrincipal UserDetails user) {
        return bookingTicketCartService.viewPurchaseHistory(user.getUsername());
    }

}