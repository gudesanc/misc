package org.gds.misc.code;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * https://www.baeldung.com/java-generating-barcodes-qr-codes
 */
public class QRCode {


    public static void main(String[] args) throws Exception {
        String url="https://sites.google.com/icviasantisavarino.edu.it/openday/home";
        BufferedImage img = generateQRCodeImage(url);
        File outputfile = new File("/tmp/qrcode.png");
        ImageIO.write(img, "png", outputfile);
    }



    public static BufferedImage generateQRCodeImage(String barcodeText) throws Exception {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix =
                barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 250, 250);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

}
