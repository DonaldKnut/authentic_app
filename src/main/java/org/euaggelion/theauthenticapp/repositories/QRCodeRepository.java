package org.euaggelion.theauthenticapp.repositories;

import org.euaggelion.theauthenticapp.models.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRCodeRepository extends JpaRepository<QRCode, Long> {
    Optional<QRCode> findByCode(String code);
}
