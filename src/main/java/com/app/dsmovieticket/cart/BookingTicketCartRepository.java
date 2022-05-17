package com.app.dsmovieticket.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingTicketCartRepository extends JpaRepository<BookingTicketCartEntity, Long> {

    Optional<BookingTicketCartEntity> findByUserIdAndPurchase(long authUserId, boolean b);
    Optional<BookingTicketCartEntity> findByIdAndPurchase(long ticketCartId, boolean b);
    List<BookingTicketCartEntity> findAllByUserId(long authUserId);

}