package ua.dkulieshov;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class QrGenerator {

  public static InputStream generateQRCodeImage(String barcodeText) throws Exception {
    BufferedImage bufferedImage = buildBufferedImage(barcodeText);
    return getInputStream(bufferedImage);
  }

  private static BufferedImage buildBufferedImage(String barcodeText) throws WriterException {
    QRCodeWriter barcodeWriter = new QRCodeWriter();
    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200);

    BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
    return bufferedImage;
  }

  private static InputStream getInputStream(BufferedImage bufferedImage) throws IOException {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    ImageIO.write(bufferedImage, "png", os);
    return new ByteArrayInputStream(os.toByteArray());
  }
}
