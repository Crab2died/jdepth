package com.github.usefultool;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class DrawBackColor {

    public static void main(String[] args) {
        System.out.println(transferAlpha2File("C:\\Users\\Crab2Died\\Desktop\\1111.png", "C:\\Users\\Crab2Died\\Desktop\\aa.png"));
    }

    public static boolean transferAlpha2File(String imgSrc, String imgTarget) {
        File file = new File(imgSrc);
        InputStream is = null;
        boolean result;
        try {
            is = new FileInputStream(file);
            // 如果是MultipartFile类型，那么自身也有转换成流的方法：is = file.getInputStream();
            BufferedImage bi = ImageIO.read(is);
            Image image = bi;
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);
                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }
                    bufferedImage.setRGB(j2, j1, rgb);
                }
            }
            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            result = ImageIO.write(bufferedImage, "png", new File(imgTarget));// 直接输出文件
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
        return result;
    }

    public static byte[] transferAlpha2Byte(String imgSrc) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        File file = new File(imgSrc);
        InputStream is = null;
        byte[] result = null;
        try {
            is = new FileInputStream(file);
            // 如果是MultipartFile类型，那么自身也有转换成流的方法：is = file.getInputStream();
            BufferedImage bi = ImageIO.read(is);
            Image image = bi;
            ImageIcon imageIcon = new ImageIcon(image);
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(),
                    BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
            g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
            int alpha = 0;
            for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
                for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
                    int rgb = bufferedImage.getRGB(j2, j1);

                    int R = (rgb & 0xff0000) >> 16;
                    int G = (rgb & 0xff00) >> 8;
                    int B = (rgb & 0xff);
                    if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
                        rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
                    }

                    bufferedImage.setRGB(j2, j1, rgb);

                }
            }

            g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);//转换成byte数组
            result = byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
            }
        }
        return result;
    }
}