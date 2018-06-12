package com.vic.test.swing;

import javax.swing.*;
import java.awt.*;

/**
 * Created by vic
 * Create time : 2018/4/9 18:40
 */
public class MyFrame {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("测试窗口");
        jFrame.setSize(250, 250);
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(0, 0, 0));

        JButton jButton = new JButton("测试");

        jButton.addActionListener(e -> {
            System.out.println(e);
        });
        jPanel.add(jButton);

        jFrame.setContentPane(jPanel);
        jFrame.setVisible(true);
    }
}
