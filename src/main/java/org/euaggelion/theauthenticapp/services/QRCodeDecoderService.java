package org.euaggelion.theauthenticapp.services;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@Service
public class QRCodeDecoderService {

    public String decodeQRCode(File file) {
        try {
            // Read the image from the file
            BufferedImage bufferedImage = ImageIO.read(file);

            // Use ZXing to decode the QR code
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);

            return result.getText(); // Return the decoded QR code content
        } catch (NotFoundException e) {
            // QR code not found in the image
            return null;
        } catch (Exception e) {
            // Handle any other errors
            e.printStackTrace();
            return null;
        }
    }
}
