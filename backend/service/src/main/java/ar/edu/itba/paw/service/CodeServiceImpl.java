package ar.edu.itba.paw.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@PropertySource("classpath:config.properties")
@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private Environment env;

    public String getCode(String plainText) {
        try {
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(env.getProperty("encPassword").toCharArray(), env.getProperty("encSalt").getBytes(), 65536, 256);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            String cipherText = Base64.getUrlEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));

            ByteArrayOutputStream b = new ByteArrayOutputStream();
            b.write(ivParameterSpec.getIV());
            b.write(cipherText.getBytes(StandardCharsets.UTF_8));
            byte[] cipherArr = b.toByteArray();

            byte[] encodeBase64 = Base64.getUrlEncoder().encode(cipherArr);
            return new String(encodeBase64, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Code generation exception: " + e.getMessage());
        }
    }

    public byte[] createQr(String text) {
        try {
            QRCodeWriter barcodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = barcodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("QR exception: " + e.getMessage());
        }
    }
}
