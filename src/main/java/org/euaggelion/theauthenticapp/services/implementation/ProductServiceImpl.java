package org.euaggelion.theauthenticapp.services.implementation;

import org.euaggelion.theauthenticapp.configs.ISBNBarcodeDecoder;
import org.euaggelion.theauthenticapp.configs.QRCodeDecoder;
import org.euaggelion.theauthenticapp.models.Product;
import org.euaggelion.theauthenticapp.models.QRCode;
import org.euaggelion.theauthenticapp.repositories.ProductRepository;
import org.euaggelion.theauthenticapp.services.ProductService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ISBNBarcodeDecoder isbnBarcodeDecoder;
    private final QRCodeDecoder qrCodeDecoder;

    public ProductServiceImpl(ProductRepository productRepository, ISBNBarcodeDecoder isbnBarcodeDecoder, QRCodeDecoder qrCodeDecoder) {
        this.productRepository = productRepository;
        this.isbnBarcodeDecoder = isbnBarcodeDecoder;
        this.qrCodeDecoder = qrCodeDecoder;
    }

    @Override
    public Optional<Product> getProductByIsbn(String isbn) {
        return productRepository.findByIsbn(isbn);
    }

    @Override
    public Optional<Product> getProductByQrCode(String qrCodeData) {
        QRCode qrCode = new QRCode();
        qrCode.setCode(qrCodeData); // Assuming `QRCode` has a `setCode` method
        return productRepository.findByQrCode(qrCode);
    }


    @Override
    public String checkProductAuthenticityByIsbnBarcode(File barcodeImageFile) {
        try {
            // Use the ISBNBarcodeDecoder to decode the ISBN from the barcode image
            String decodedIsbn = isbnBarcodeDecoder.decodeISBNBarcode(barcodeImageFile);

            if (decodedIsbn != null) {
                return checkProductAuthenticityByIsbn(decodedIsbn);
            } else {
                return "Unable to decode ISBN barcode.";
            }
        } catch (IOException e) {
            return "Error processing the barcode image.";
        }
    }

    @Override
    public String checkProductAuthenticityByQrCode(File qrCodeImageFile) {
        try {
            // Use the QRCodeDecoder to decode the QR code content from the image
            String decodedQrCode = qrCodeDecoder.decodeQRCode(qrCodeImageFile);

            if (decodedQrCode != null) {
                return checkProductAuthenticityByQrCode(decodedQrCode);
            } else {
                return "Unable to decode QR code.";
            }
        } catch (IOException e) {
            return "Error processing the QR code image.";
        }
    }

    @Override
    public String checkProductAuthenticityByIsbn(String isbn) {
        Optional<Product> product = getProductByIsbn(isbn);
        return product.map(value -> value.isAuthentic() ? "This product is authentic." : "This product is fake!").orElse("Product not found.");
    }

    @Override
    public String checkProductAuthenticityByQrCode(String qrCode) {
        Optional<Product> product = getProductByQrCode(qrCode);
        return product.map(value -> value.isAuthentic() ? "This product is authentic." : "This product is fake!").orElse("Product not found.");
    }
}
