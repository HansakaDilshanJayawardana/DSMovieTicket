package com.app.dsmovieticket.payment;

import com.app.dsmovieticket.payment.payload.MobileRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm-payment")
    public void OtpVerify(@RequestBody MobileRequest mobileRequest){
        paymentService.confirmMobile(mobileRequest.getCartId() , mobileRequest.getKey());
    }

}