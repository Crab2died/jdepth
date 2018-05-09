package com.github.usefultool.security;

import sun.misc.BASE64Decoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA encryption/decryption tool
 */
public class RSATool {

    private static final String RSA_KEY = "RSA";

    public static RSAPublicKey getPublicKey(byte[] publicKeyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance(RSA_KEY);
        return (RSAPublicKey) kf.generatePublic(spec);
    }

    public static RSAPublicKey getPublicKey(String publicKeyFilePath)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        return getPublicKey(loadKeyFile(publicKeyFilePath));
    }


    public static RSAPrivateKey getPrivateKey(byte[] privateKeyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance(RSA_KEY);
        return (RSAPrivateKey) kf.generatePrivate(spec);
    }

    public static RSAPrivateKey getPrivateKey(String privateKeyFilePath)
            throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        return getPrivateKey(loadKeyFile(privateKeyFilePath));
    }

    private static byte[] decryptBASE64(String key) throws IOException {

        return new BASE64Decoder().decodeBuffer(key);
    }

    private static byte[] loadKeyFile(String filePath) throws IOException {

        try (InputStream in = new FileInputStream(filePath);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1 << 10];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
            return out.toByteArray();
        }
    }


    public static void main(String... args) {

        String publicKeyStr =
                        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCVla4Vp0j6tLhEmF+NEBUyyexIpdFTXsX1uiH8\n" +
                        "fnMtX2+RBBk84rhHBUpa0cB2h7X4WfbPKwiRVxXBZL59hVCFyvPa/YuvN5ihe+IOPnPA68xEvbvi\n" +
                        "iRBFaSMdghcelClq4LlqyHrMIcSeRJgK8VSeLr8Q6vQEBHtVR3AFcfEW0wIDAQAB";

        String privateKeyStr =
                        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJWVrhWnSPq0uESYX40QFTLJ7Eil\n" +
                        "0VNexfW6Ifx+cy1fb5EEGTziuEcFSlrRwHaHtfhZ9s8rCJFXFcFkvn2FUIXK89r9i683mKF74g4+\n" +
                        "c8DrzES9u+KJEEVpIx2CFx6UKWrguWrIeswhxJ5EmArxVJ4uvxDq9AQEe1VHcAVx8RbTAgMBAAEC\n" +
                        "gYBCOQ/m+jZu948Unzi+JL6dy5NV3kM725tcXFkrhXfMQJIN63iY4e6fUpJGftHsJ8zB9GI1+WFD\n" +
                        "pwA0rDtBKsIopnftI10vCqwVZB0J0RXt0e/oVgL34JU/XsDwh1CMruI4fzrpMSW6OB+TcitIZ9ys\n" +
                        "wlpKlabrhCYcwc2fM8D/AQJBAPxbxosCrdL0PvJ4XQRQdWxceYrG3hywCi0gqyvKMVA+vMgRNkPw\n" +
                        "RT6Wb/m/9hLlEIFai0purhG5dulehXu9oxMCQQCXvkDINPNrEr8b//REAS0KdZ1fFHJrPJFipyq1\n" +
                        "BKnuQHvKYbh/HZB9D57hWI/Nktvg0smjQqyMcf3roGA9EXVBAkEAx9IPuwCrDK/TrAC8fm8LG3t5\n" +
                        "zfY753FJDYShpPHb7FNQ/gYK9u6ola9fCN4EBeAVGqfMRWqwMF6vlof+oAV/uwJAJgTA7mh15InZ\n" +
                        "4AYNCUzSICC/wSa6VCvksTb4+Emm2c7GNTquowqhPPRA9mzd+r+k33twkbsOyFF3MSYJdsN9AQJB\n" +
                        "APRBZ2XLUq2+25xGsQn1S45IauutF26UklJId4gopXFf8pzY5lTKODKnFqMYCy16uiHBOp4Q4fe0\n" +
                        "w8dnsKqbY4E=";

        String input = "!!!hello world!!!";

        RSAPublicKey publicKey;
        RSAPrivateKey privateKey;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            publicKey = getPublicKey("public.secret");
            privateKey = getPrivateKey("private.secret");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            // Encrypted
            System.out.println("Encrypted =>  " + new String(cipherText));
            // Decrypted
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] plainText = cipher.doFinal(cipherText);
            System.out.println("Decrypted => " + new String(plainText));

        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException
                | InvalidKeySpecException | BadPaddingException
                | IllegalBlockSizeException | NoSuchPaddingException e) {

            e.printStackTrace();
        }
    }
}
