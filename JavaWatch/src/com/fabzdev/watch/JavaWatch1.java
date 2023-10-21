/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabzdev.watch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author fabio
 */
public class JavaWatch1 extends JPanel {


    @Override
    protected void paintComponent(Graphics g) {
        Rectangle rect = new Rectangle(getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fill(rect);
        
        g2d.setColor(new Color(0, 0x85, 0));
        int size = Math.min(getWidth(), getHeight());
        Rectangle frame = new Rectangle(1+getWidth()/2 - size/2, 1+getHeight()/2 - size/2, size-2, size-2);
//        g2d.draw(frame);
        g2d.drawOval(frame.x,frame.y,frame.width,frame.height);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JavaWatch1");
            frame.setMinimumSize(new Dimension(400, 400));
            JavaWatch1 mainPanel = new JavaWatch1();
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
