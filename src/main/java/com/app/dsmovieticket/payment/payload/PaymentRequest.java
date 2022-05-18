package com.app.dsmovieticket.payment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NonNull
    private String paymentType;
    @NonNull
    private long cartId;
    private String cardType;
    private String cardNumber;
    private String csv;
    private String cardHolder;
    private Date expDate;
    private String phoneNumber;
    @NonNull
    private float amount;
    //Theatre Options
    @NonNull
    private String theatreType;

}