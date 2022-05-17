package com.app.dsmovieticket.payment.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileRequest {

    @NonNull
    private long cartId;
    @NonNull
    private int key;

}