package org.euaggelion.theauthenticapp.configs;

//import com.google.zxing.BinaryBitmap;
//import com.google.zxing.MultiFormatReader;
//import com.google.zxing.NotFoundException;
//import com.google.zxing.common.HybridBinarizer;
//import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
//import com.google.zxing.LuminanceSource;
//import com.google.zxing.Result;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Service
public class QRCodeDecoder {

    public String decodeQRCode(File qrCodeImage) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(qrCodeImage);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText(); // This is the decoded QR code text
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }
}