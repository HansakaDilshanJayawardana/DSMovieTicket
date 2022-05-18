package com.app.dsmovieticket.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_sms_key")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SMSKeyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cart_id" , nullable = false)
    private long cartId;

    @Column(name = "phone" , nullable = false)
    private String phone;

    @Column(name = "key" , nullable = false)
    private int key;

}