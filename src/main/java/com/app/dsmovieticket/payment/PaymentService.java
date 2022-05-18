package com.app.dsmovieticket.payment;

import com.app.dsmovieticket.auth.UserService;
import com.app.dsmovieticket.cart.BookingTicketCartEntity;
import com.app.dsmovieticket.cart.BookingTicketCartRepository;
import com.app.dsmovieticket.cart.BookingTicketCartService;
import com.app.dsmovieticket.common.NotFoundException;
import com.app.dsmovieticket.common.Status;
import com.app.dsmovieticket.email.EmailController;
import com.app.dsmovieticket.payment.payload.PaymentRequest;
import com.app.dsmovieticket.sms.SMSKeyRepository;
import com.app.dsmovieticket.utility.*;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
@AllArgsConstructor
@Log4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserService userService;
    private final BookingTicketCartService bookingTicketCartService;
    private final SMSKeyRepository smsKeyRepository;
    private final BookingTicketCartRepository bookingTicketCartRepository;
    private final CardValidationHandler cardValidationHandler;
//    private final DeliveryHandler deliveryHandler;
//    private final DeliveryRepository deliveryRepository;
//    private final SmsHandler smsHandler;
    private final EmailController emailController;

    @Transactional
    public PaymentInfoEntity pay(PaymentRequest paymentRequest, String username) {
        BookingTicketCartEntity cart = bookingTicketCartRepository.findByIdAndPurchase(paymentRequest.getCartId(), false)
                .orElseThrow(() -> new RuntimeException("Cart Payment Already Done"));

        if (paymentRequest.getPaymentType().equals(PaymentType.CARD)) {
            cardValidationHandler.deductAmount(paymentRequest.getCardType(), paymentRequest.getCardNumber(), paymentRequest.getCsv(), paymentRequest.getCardHolder(), paymentRequest.getAmount());

            PaymentInfoEntity payEntity = PaymentInfoEntity.builder()
                    .paymentType(paymentRequest.getPaymentType())
                    .amount(paymentRequest.getAmount())
                    .cartId(paymentRequest.getCartId())
                    .paymentStatus(PaymentStatus.DONE)
                    .build();

            payEntity.setCreatedAt(new Date());
            payEntity.setCreatedBy(userService.getAuthUserId(username));

            bookingTicketCartService.updatePayment(paymentRequest.getCartId(), paymentRequest.getAmount(), paymentRequest.getPaymentType(), paymentRequest.getPhoneNumber());

            //TODO SMS Message
            // TODO uncomment these
            emailController.sendMail(
                    userService.getUserEmailByName(username) ,
                    "DSMovieTicket - Confirmation",
                    "Your Ticket has been Booked!!!"
            );

            return paymentRepository.save(payEntity);
        } else if (paymentRequest.getPaymentType().equals(PaymentType.MOBILE)) {
            // send mobile information for mobile pay center
            // TODO send mobile center

            PaymentInfoEntity payEntity = PaymentInfoEntity.builder()
                    .paymentType(paymentRequest.getPaymentType())
                    .amount(paymentRequest.getAmount())
                    .cartId(paymentRequest.getCartId())
                    .paymentStatus(PaymentStatus.PENDINGFORCONFIRM)
                    .build();

            payEntity.setCreatedAt(new Date());
            payEntity.setCreatedBy(userService.getAuthUserId(username));

            bookingTicketCartService.updatePayment(paymentRequest.getCartId(), paymentRequest.getAmount(), paymentRequest.getPaymentType(), paymentRequest.getPhoneNumber());

            // TODO uncomment these
            emailController.sendMail(
                    userService.getUserEmailByName(username) ,
                    "DSMovieTicket - Payment Alert",
                    "Please Complete Your Payment!!!"
            );

            return paymentRepository.save(payEntity);
        }
        return null;
    }

    @Transactional
    public void confirmMobile(long cartId, int key) {
        BookingTicketCartEntity cart = bookingTicketCartRepository.findById(cartId).orElseThrow(() -> new NotFoundException("Cart not Found"));
        smsKeyRepository.findByCartIdAndKey(cartId, key).orElseThrow(() -> new NotFoundException("Payment not Found Key"));
        cart.setPurchase(true);
        cart.setStatus(Status.TicketCart.PAYMENTDONE);

        bookingTicketCartRepository.save(cart);

        PaymentInfoEntity paymentEntity = paymentRepository.findByCartId(cartId).orElseThrow(() -> new NotFoundException("Payment Entry not Found"));
        paymentEntity.setPaymentStatus(PaymentStatus.DONE);

        // TODO uncomment these
        emailController.sendMail(
                userService.getUserById(cart.getUserId()).getEmail() ,
                "DSMovieTicket - Confirmation",
                "Your Ticket has been Booked!!!"
        );

//        smsHandler.sendSms(deliveryInfo.getPhone() , "Your order has been placed and our delivery team contact you as soon as possible");
        paymentRepository.save(paymentEntity);
    }

}