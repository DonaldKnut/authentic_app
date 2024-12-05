package org.euaggelion.theauthenticapp.configs;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Service
public class ISBNBarcodeDecoder {

    // Method to decode ISBN barcode (EAN-13) from image
    public String decodeISBNBarcode(File barcodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(barcodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            // Attempt to decode the barcode image
            Result result = new MultiFormatReader().decode(bitmap);

            // Check if the format is EAN-13, which is commonly used for ISBN-13
            if (result.getBarcodeFormat() == BarcodeFormat.EAN_13) {
                return result.getText(); // This is the decoded ISBN number
            } else {
                return "Not a valid ISBN barcode.";
            }
        } catch (NotFoundException e) {
            System.out.println("No barcode found in the image.");
            return null;
        }
    }
}