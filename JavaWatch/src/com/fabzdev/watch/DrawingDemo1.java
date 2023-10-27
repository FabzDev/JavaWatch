/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabzdev.watch;

import java.awt.Color;
import java.awt.Dimension;
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
public class DrawingDemo1 extends JPanel {

    public DrawingDemo1() {
        super();
        setBackground(Color.BLACK);

    }

    public void drawingTest1(Graphics2D g2d, int size, float radius) {
        Arc2D fan = new Arc2D.Float(getWidth() / 2 - radius, getHeight() / 2 - radius, (int) radius * 2, (int) radius * 2, 0, 180, Arc2D.CHORD);
        g2d.setColor(Color.red);
        g2d.fill(fan);

//        g2d.drawRect(getWidth()/2 - size/2, getHeight()/2 - size/2, size, size);
//        g2d.drawOval((int)(getWidth()/2-radius), (int)(getHeight()/2-radius), (int)(radius*2), (int)(radius*2));
//        GeneralPath gp = new GeneralPath();
//        gp.moveTo(200, 200);
//        gp.lineTo(200, 300);
//        gp.lineTo(100, 300);
//        gp.quadTo(200, 250, 200, 200);
//        gp.closePath();
    }

    public void drawingTest2(Graphics2D g2d, int size, float radius) {
        Arc2D fanArc = new Arc2D.Float(getWidth() / 2 - radius, getHeight() / 2 - radius, (int) radius * 2, (int) radius * 2, 10, 180, Arc2D.CHORD);
        g2d.setColor(Color.red);
        Area fanArea = new Area(fanArc);
//        g2d.setColor(new Color(255,255,0,150));
        fanArea.subtract(new Area(new Arc2D.Float(getWidth() / 2 - (int) (radius * 1.1f), getHeight() / 2 - (int) (radius * 1.1f), (int) radius * 2 * 1.1f, (int) radius * 2 * 1.1f, 170, 180, Arc2D.CHORD)));
        g2d.fill(fanArea);
    }

    public void drawingTest3(Graphics2D g2d, int size, float radius) {
        Arc2D fanArc = new Arc2D.Float(getWidth() / 2 - radius, getHeight() / 2 - radius, (int) radius * 2, (int) radius * 2, 10, 180, Arc2D.CHORD);
        g2d.setColor(Color.red);
        Area fanArea = new Area(fanArc);
        fanArea.subtract(new Area(new Arc2D.Float(getWidth() / 2 - (int) (radius * 1.1f), getHeight() / 2 - (int) (radius * 1.1f), (int) radius * 2 * 1.1f, (int) radius * 2 * 1.1f, 170, 180, Arc2D.CHORD)));

        float border = radius * 0.2f;
        int x1 = (int) (getWidth() / 2 + (radius * Math.cos(Math.toRadians(10))) - (border * Math.cos(Math.toRadians(10))));
        int y1 = (int) ((getHeight() / 2) - (radius * Math.sin(Math.toRadians(10))) + (border * Math.sin(Math.toRadians(10))));
        int xn = (int) (getWidth() / 2 + (radius * Math.cos(Math.toRadians(10))));
        int yn = (int) ((getHeight() / 2) - (radius * Math.sin(Math.toRadians(10))));
        double angle = (border / radius) + Math.toRadians(10);
        int x3 = (int) (getWidth() / 2 + (radius * Math.cos(angle)));
        int y3 = (int) (getHeight() / 2 - (radius * Math.sin(angle)));
        int x4 = (int) (getWidth() / 2 + (radius * 1.1 * Math.cos(Math.toRadians(10))));
        int y4 = (int) (getHeight() / 2 - (radius * 1.1 * Math.sin(Math.toRadians(10))));
        
//        g2d.fillOval(x3-5, y3-5, 10, 10);

        GeneralPath gp = new GeneralPath();
        gp.moveTo(x1, y1);
        gp.lineTo(x4, y4);
        gp.lineTo(x3+10, y3);
        gp.lineTo(x3, y3);
        gp.quadTo(xn, yn, x1, y1);

        fanArea.subtract(new Area(gp));
        g2d.fill(fanArea);
        g2d.setColor(new Color(255, 255, 0, 150));
        g2d.fill(gp);
        

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int size = Math.min(getWidth(), getHeight());
        float radius = size / 2 * 0.8f;
        Graphics2D g2d = (Graphics2D) g;
        drawingTest3(g2d, size, radius);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("drawingDemo1");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new DrawingDemo1());
            frame.setMinimumSize(new Dimension(400, 400));
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
