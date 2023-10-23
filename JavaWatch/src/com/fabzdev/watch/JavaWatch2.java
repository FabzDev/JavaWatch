/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabzdev.watch;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author fabio
 */
public class JavaWatch2 extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle bounds = new Rectangle(getWidth(), getHeight());
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, bounds.width, bounds.height);

        g2d.setColor(new Color(0, 0x85, 0));

        int size = Math.min(bounds.width, bounds.height);
        Rectangle frame = new Rectangle(bounds.width / 2 - size / 2, bounds.height / 2 - size / 2, size, size);
        g2d.drawOval(frame.x, frame.y, frame.width, frame.height);

        int centerX = frame.x + frame.width / 2;
        int centerY = frame.y + frame.height / 2;
        double radius = frame.width / 2;
        
        Stroke st = g2d.getStroke();
        Stroke boldSt = new BasicStroke(2);
        
        for (int n = 0; n < 60; n++) {
            double angle = Math.toRadians((270 + (360 / 60) * n) % 360);
            double intRadius;
            if (n % 5 == 0) {
                intRadius = 0.9 * radius;
                g2d.setStroke(boldSt);
            } else {
                intRadius = 0.95 * radius;
                g2d.setStroke(st);
            }
            g2d.drawLine((int) (centerX + intRadius * Math.cos(angle)), (int) (centerY + intRadius * Math.sin(angle)), centerX + (int) (radius * Math.cos(angle)), centerY + (int) (radius * Math.sin(angle))); //aguja
        }

//        g2d.drawLine(centerX, 0, centerX, size); // guia vertical
//        g2d.drawLine(centerX, centerY, frame.x + frame.width, centerY); //aguja
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JavaWatch1");
            frame.setMinimumSize(new Dimension(400, 400));
            JavaWatch2 mainPanel = new JavaWatch2();
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
