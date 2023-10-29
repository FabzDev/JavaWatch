/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabzdev.watch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author fabio
 */
public class DrawingDemo2 extends JPanel {

    public DrawingDemo2() {
        super();
        setBackground(Color.BLACK);

    }

    public void drawingTest1(Graphics2D g2d, int size, float radius) {
        Font defaultFont = g2d.getFont();
        FontMetrics defaultFontMetrics = g2d.getFontMetrics();
        g2d.setColor(Color.red);
        g2d.drawString("09:00 PM", 150, 150);
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight());
        float radius = size / 2 * 0.8f;
        Graphics2D g2d = (Graphics2D) g;
        drawingTest1(g2d, size, radius);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("drawingDemo1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new DrawingDemo2());
            frame.setMinimumSize(new Dimension(400, 400));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
