package org.euaggelion.theauthenticapp.services;

import org.euaggelion.theauthenticapp.models.Product;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Optional;

@Service
public interface ProductService {
    Optional<Product> getProductByIsbn(String isbn);
    Optional<Product> getProductByQrCode(String qrCode);


    String checkProductAuthenticityByIsbnBarcode(File barcodeImageFile);

    String checkProductAuthenticityByQrCode(File qrCodeImageFile);

    String checkProductAuthenticityByIsbn(String isbn);

    String checkProductAuthenticityByQrCode(String qrCode);
}