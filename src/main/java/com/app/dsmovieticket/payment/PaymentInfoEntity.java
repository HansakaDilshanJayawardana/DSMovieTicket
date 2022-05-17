package com.app.dsmovieticket.payment;

import com.app.dsmovieticket.utility.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_payment_records")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInfoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "payment_type" , nullable = false)
    private String paymentType;

    @Column(name = "payed_amount" , nullable = false)
    private float amount;

    @Column(name = "cart_amount" , nullable = false)
    private float cartAmount;

    @Column(name = "status")
    private String paymentStatus = PaymentStatus.PENDING;

    @Column(name = "theatre_type")
    private String theatreType;

    @Column(name = "cart_id" , nullable = false)
    private long cartId;

}