package org.euaggelion.theauthenticapp.controller;

import lombok.RequiredArgsConstructor;
import org.euaggelion.theauthenticapp.models.Product;
import org.euaggelion.theauthenticapp.models.User;
import org.euaggelion.theauthenticapp.services.QRCodeDecoderService;
import org.euaggelion.theauthenticapp.services.ProductService;
import org.euaggelion.theauthenticapp.services.ScanHistoryService;
import org.euaggelion.theauthenticapp.services.ISBNBarcodeDecoderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final QRCodeDecoderService qrCodeDecoder;
    private final ProductService productService;
    private final ScanHistoryService scanHistoryService;
    private final ISBNBarcodeDecoderService isbnBarcodeDecoder;

    @PostMapping("/search/qr")
    public ResponseEntity<String> checkProductByQrCode(
            @RequestParam("qrCodeImage") MultipartFile qrCodeImageFile,
            @AuthenticationPrincipal User user) throws IOException {

        File file = convertMultipartFileToFile(qrCodeImageFile);

        String qrCodeContent = qrCodeDecoder.decodeQRCode(file);

        if (qrCodeContent == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid QR code image");
        }
        Optional<Product> productOptional = productService.getProductByQrCode(qrCodeContent);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            scanHistoryService.logScan(user, product);  // Log the scan
            return ResponseEntity.ok(qrCodeContent);  // Return the product info or QR code content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @PostMapping("/search/isbn/barcode")
    public ResponseEntity<String> checkProductByIsbnBarcode(
            @RequestParam("barcodeImage") MultipartFile barcodeImageFile,
            @AuthenticationPrincipal User user) throws IOException {

        File file = convertMultipartFileToFile(barcodeImageFile);

        String isbn = isbnBarcodeDecoder.decodeISBNBarcode(file);

        if (isbn == null || isbn.equals("Not a valid ISBN barcode.")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ISBN barcode image");
        }

        Optional<Product> productOptional = productService.getProductByIsbn(isbn);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            scanHistoryService.logScan(user, product);  // Log the scan
            return ResponseEntity.ok(isbn);  // Return the product info or ISBN
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    private File convertMultipartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(convFile);
        return convFile;
    }
}
