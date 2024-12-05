package org.euaggelion.theauthenticapp.services;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@Service
public class ISBNBarcodeDecoderService {

    public String decodeISBNBarcode(File file) {
        try {
            // Read the image from the file
            BufferedImage bufferedImage = ImageIO.read(file);

            // Use ZXing to decode the barcode
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);

            // Validate and return the ISBN content
            String isbn = result.getText();
            if (isValidISBN(isbn)) {
                return isbn;
            } else {
                return "Not a valid ISBN barcode.";
            }
        } catch (NotFoundException e) {
            // Barcode not found in the image
            return null;
        } catch (Exception e) {
            // Handle any other errors
            e.printStackTrace();
            return null;
        }
    }

    private boolean isValidISBN(String isbn) {
        // Simple validation for ISBN-10 or ISBN-13 formats
        return isbn.matches("\\d{10}") || isbn.matches("\\d{13}");
    }
}
