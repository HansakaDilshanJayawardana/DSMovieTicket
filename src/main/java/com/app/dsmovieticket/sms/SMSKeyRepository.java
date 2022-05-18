package com.app.dsmovieticket.sms;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SMSKeyRepository extends JpaRepository<SMSKeyEntity , Long> {

    Optional<SMSKeyEntity>findByCartId(long cartId);
    Optional<Object> findByCartIdAndKey(long cartId, int key);

}