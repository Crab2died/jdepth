package com.github.usefultool.security;

import org.junit.Test;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * RSA key pair generate tool
 * @author Crab2Died
 */
public class RSAKeyPairGeneratorTool {

    private static final int KEY_SIZE = 1 << 10;

    private static final String RSA_KEY = "RSA";

    private static KeyPair keyPair;

    static
    {
        KeyPairGenerator keyPairGen;
        try {
            keyPairGen = KeyPairGenerator.getInstance(RSA_KEY);
            keyPairGen.initialize(KEY_SIZE);
            keyPair = keyPairGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /* export as string */
    public static String privateKeyExport() {
        return encryptBASE64(keyPair.getPrivate().getEncoded());
    }

    /* export as a file */
    public static void privateKeyExport(String filePath) throws IOException {

        try(FileOutputStream fos = new FileOutputStream(filePath)){
            fos.write(keyPair.getPrivate().getEncoded());
        }
    }

    /* export as string */
    public static String publicKeyExport() {
        return encryptBASE64(keyPair.getPublic().getEncoded());
    }

    /* export as a file */
    public static void publicKeyExport(String filePath) throws IOException {

        try(FileOutputStream fos = new FileOutputStream(filePath)){
            fos.write(keyPair.getPublic().getEncoded());
        }
    }

    /*
     * BASE64 encode tool
     */
    private static String encryptBASE64(byte[] key) {

        return new BASE64Encoder().encodeBuffer(key);
    }

    @Test
    public void generateKeyPair() throws IOException {

        System.out.println(privateKeyExport());
        System.out.println(publicKeyExport());

        /* export public/private key as files */
        privateKeyExport("private.secret");
        publicKeyExport("public.secret");

    }

}
