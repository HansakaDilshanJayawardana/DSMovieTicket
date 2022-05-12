package com.app.dsmovieticket.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingTicketCartRepository extends JpaRepository<ShoppingTicketCartEntity, Long> {

    Optional<ShoppingTicketCartEntity> findByUserIdAndPurchase(long authUserId, boolean b);
    Optional<ShoppingTicketCartEntity> findByIdAndPurchase(Long ticketCartId, boolean b);
    List<ShoppingTicketCartEntity> findAllByUserId(long authUserId);

}