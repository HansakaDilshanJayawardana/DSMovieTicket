package com.app.dsmovieticket.cart;

import com.app.dsmovieticket.common.Status;
import com.app.dsmovieticket.utility.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tbl_ticket_cart")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingTicketCartEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id" , nullable = false)
    private long userId;

    @Column(name = "is_purchase"  ,nullable = false)
    private boolean purchase = false;

    @Column(name = "status")
    private String status = Status.TicketCart.PENDING;

}