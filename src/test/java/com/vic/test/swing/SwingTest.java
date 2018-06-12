package com.vic.test.swing;

import junit.framework.TestCase;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by vic
 * Create time : 2018/4/9 18:36
 */
public class SwingTest extends TestCase {


    public void test() throws IOException {
//
//        JFrame jFrame = new JFrame("测试窗口");
//        jFrame.setSize(250,250);
//        jFrame.setLocationRelativeTo(null);
//        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        jFrame.setVisible(true);


        URL url = new URL("http://localhost:8080/#/User");

        JEditorPane ed = new JEditorPane();
        ed.setSize(2000,2000);

        //create a new image
        BufferedImage image = new BufferedImage(ed.getWidth(), ed.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        //paint the editor onto the image
        SwingUtilities.paintComponent(image.createGraphics(),
                ed,
                new JPanel(),
                0, 0, image.getWidth(), image.getHeight());
        //save the image to file
        ImageIO.write((RenderedImage)image, "png", new File("/Users/vic/Desktop/html.png"));

    }
}
