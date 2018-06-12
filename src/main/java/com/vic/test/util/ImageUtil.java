package com.vic.test.util;

import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vic
 * Create time : 2018/4/9 10:28
 */
public class ImageUtil {

    public static void main(String[] args) throws IOException {
//        ImageUtil.mergeY("/Users/vic/Desktop/未标题-1.png", "/Users/vic/Desktop/ic_launcher.png");
        ImageUtil.mirrorImage();
    }

    public static void mergeY(String... filePath) {
        if (filePath.length == 0) {
            return;
        }
        AtomicInteger width = new AtomicInteger(0);
        AtomicInteger height = new AtomicInteger(0);
        List<BufferedImage> images = Lists.newArrayList();

        Arrays.stream(filePath).forEach(s -> {
            File file = new File(s);
            if (file.isFile()) {
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    images.add(bufferedImage);
                    int min = Math.min(width.get(), bufferedImage.getWidth());
                    width.compareAndSet(min, bufferedImage.getWidth());
                    height.addAndGet(bufferedImage.getHeight());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (Objects.isNull(images) || images.isEmpty()) {
            return;
        }

        try {
            BufferedImage target = new BufferedImage(width.get(), height.get(), BufferedImage.TYPE_INT_RGB);

            Graphics2D graphics = target.createGraphics();
            target = graphics.getDeviceConfiguration().createCompatibleImage(width.get(), height.get(), Transparency.TRANSLUCENT);
            graphics = target.createGraphics();

            for (int i = 0, padding = 0; i < images.size(); i++) {
                BufferedImage image = images.get(i);
                graphics.drawImage(image, (target.getWidth() - image.getWidth()) / 2, padding, image.getWidth(), image.getHeight(), null);
                padding += image.getHeight();
            }

            BufferedImage qrCode = QRCodeUtil.createImage("哈哈", null, false);
            graphics.drawImage(qrCode, (target.getWidth() - qrCode.getWidth()) / 2, target.getHeight() / 2, null);

            graphics.dispose();

            ImageIO.write(target, "PNG", new File("/Users/vic/Desktop/target2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void mirrorImage() throws IOException {

        BufferedImage bufferedImage = ImageIO.read(new File("/Users/vic/Desktop/未标题-3.png"));


        BufferedImage target = new BufferedImage(bufferedImage.getWidth() * 2, bufferedImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                int rgb = bufferedImage.getRGB(x, y);
                target.setRGB(x, y, rgb);
                target.setRGB(target.getWidth() - x - 1, y, rgb);
            }
        }


//        ImageIO.write(target, "PNG", new File("/Users/vic/Desktop/未标题-3-1.png"));

        Graphics graphics = target.getGraphics();

        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();

        JFrame jFrame = new JFrame("显示图片");
        jFrame.setSize(500, 300);
//        JPanel jPanel = new JPanel();
//
        Toolkit toolkit = jFrame.getToolkit();
        Image image = toolkit.getImage("/Users/vic/Desktop/未标题-3-1.png");

        jFrame.setIconImage(image);

        JLabel jLabel = new JLabel();
        jLabel.setIcon(new ImageIcon("/Users/vic/Desktop/未标题-3-1.png"));
        jLabel.setSize(500, 200);
        jFrame.add(jLabel);


//        jFrame.setContentPane(jPanel);
        jFrame.setVisible(true);

    }

    public static void mergeY2() throws IOException {
        File file1 = new File("/Users/vic/Desktop/未标题-1.png");
        File file2 = new File("/Users/vic/Desktop/ic_launcher.png");
        BufferedImage a = ImageIO.read(file1);

        BufferedImage b = ImageIO.read(file2);


        int width = Math.max(a.getWidth(), b.getWidth());
        int height = a.getHeight() + b.getHeight() + 10;


        BufferedImage target = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for (int x = 0; x < width; x++) {

            for (int y = 0; y < height; y++) {
                int rgb = 0;
                int ax = a.getWidth();
                int ay = a.getHeight();
                if (x < ax && y < ay) {
                    rgb = a.getRGB(x, y);
                }

                int bx = b.getWidth();
                int by = b.getHeight();
                if (x < bx && y > ay && (y - ay) < by) {
                    rgb = b.getRGB(x, y - ay);
                }
                target.setRGB(x, y, rgb);
            }
        }

        ImageIO.write(target, "PNG", new File("/Users/vic/Desktop/target.jpg"));
    }
}
