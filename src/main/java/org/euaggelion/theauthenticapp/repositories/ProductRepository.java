package org.euaggelion.theauthenticapp.repositories;

import org.euaggelion.theauthenticapp.models.Product;
import org.euaggelion.theauthenticapp.models.QRCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find by ISBN
    Optional<Product> findByIsbn(String isbn);

    // Find by QR Code
    Optional<Product> findByQrCode(QRCode qrCode);
}