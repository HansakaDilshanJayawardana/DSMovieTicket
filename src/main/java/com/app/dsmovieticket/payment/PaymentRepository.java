package com.app.dsmovieticket.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentInfoEntity, Long> {

    Optional<PaymentInfoEntity> findByCartId(long cartId);

}