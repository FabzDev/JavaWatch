/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabzdev.watch;
    import javax.swing.*;
import java.awt.*;
/**
 *
 * @author fabio
 */

public class test extends JFrame {
    public test() {
        setTitle("Polygon Example");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new MyPanel());
    }

    public static void main(String[] args) {
        new test().setVisible(true);
    }
}

class MyPanel extends JPanel {
    int[] xPoints = {192, 211, 266, 193, 119, 174};
    int[] yPoints = {96, 170, 223, 202, 223, 170};

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillPolygon(xPoints, yPoints, xPoints.length);
    }
}

