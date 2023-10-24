/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fabzdev.watch;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author fabio
 */
public class JavaWatch7 extends JPanel {

    //definiendo variables de colores
    Color backgroundColor = new Color(0x4040c0);
    Color horaColor = Color.BLACK;
    Color minColor = Color.BLACK;
    Color segColor = new Color(0xa00000);
    Color faceColor = new Color(0xc0c0c0);
    Color lblColor = Color.BLACK;
    Color gradientA = new Color(224, 224, 224, 255);
    Color gradientB = new Color(32, 32, 32, 144);
    Color shadowColor = new Color(0x0, 0x0, 0x0, 0x30);
    //definiendo variables de fuentes
    Font smallFont;
    Font mediumFont;
    Font largeFont;
    Font smallTitleFont;
    Font mediumTitleFont;
    Font largeTitleFont;
    //definiendo strokes manecillas
    int stHora = 12;
    int stMin = 5;
    int stSeg = 3;

    //definiendo fuentes marca JavaWatch
    //definiendo variables de tiempo
    int hora;
    int min;
    int seg;
    Timer timer;

    public JavaWatch7() {
        super();
        updatetime();
        timer = new Timer(1000, e -> {
            updatetime();
            repaint();
        });
        timer.start();
    }

    //detener timer
    public void stopTimer() {
        timer.stop();
    }

    //definir hora
    private void updatetime() {
        Calendar time = Calendar.getInstance();
        hora = time.get(Calendar.HOUR);
        min = time.get(Calendar.MINUTE);
        seg = time.get(Calendar.SECOND);
    }

    //obtener Fonts
    private Font getSmallFont(Graphics2D g2d) {
        if (smallFont != null) {
            return smallFont;
        }
        smallFont = g2d.getFont().deriveFont(24f);
        return smallFont;
    }

    private Font getMediumFont(Graphics2D g2d) {
        if (mediumFont != null) {
            return mediumFont;
        }
        mediumFont = g2d.getFont().deriveFont(36f);
        return mediumFont;
    }

    private Font getLargeFont(Graphics2D g2d) {
        if (largeFont != null) {
            return largeFont;
        }
        largeFont = g2d.getFont().deriveFont(48f);
        return largeFont;
    }

    private Font getSmallFontTitle(Graphics2D g2d) {
        if (smallTitleFont != null) {
            return smallTitleFont;
        }
        smallTitleFont = g2d.getFont().deriveFont(12f);
        return smallTitleFont;
    }

    private Font getMediumFontTitle(Graphics2D g2d) {
        if (mediumTitleFont != null) {
            return mediumTitleFont;
        }
        mediumTitleFont = g2d.getFont().deriveFont(18f);
        return mediumTitleFont;
    }

    private Font getLargeFontTitle(Graphics2D g2d) {
        if (largeTitleFont != null) {
            return largeTitleFont;
        }
        largeTitleFont = g2d.getFont().deriveFont(24f);
        return largeTitleFont;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //creando objetos para dibujar
        Graphics2D g2d = (Graphics2D) g;
        Rectangle bounds = new Rectangle(getWidth(), getHeight());

        //definiendo tamaño del reloj
        int size = Math.min(bounds.width, bounds.height);
        Rectangle frame = new Rectangle(bounds.width / 2 - size / 2, bounds.height / 2 - size / 2, size, size);

        //definiendo coordenadas
        int centerX = frame.x + frame.width / 2;
        int centerY = frame.y + frame.height / 2;
        double radius = frame.width / 2 - 10;
        double angle;

        //definiendo tamaño de fuente
        Font f;
        if (size < 400) {
            f = getSmallFont(g2d);
        } else if (size < 800) {
            f = getMediumFont(g2d);
        } else {
            f = getLargeFont(g2d);
        }
        g2d.setFont(f);

        //obteniendo FontMetrics
        FontMetrics fm = g2d.getFontMetrics();

        //definiendo grosores de linea
        Stroke st = g2d.getStroke();
        Stroke boldSt = new BasicStroke(2);

        //dibujando background
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, bounds.width, bounds.height);

        //dibujando face-background del reloj
        g2d.setColor(faceColor);
        g2d.fillOval(frame.x + 4, frame.y + 4, frame.width - 8, frame.height - 8);

        //definiendo gradiente
        Paint defaultPaint = g2d.getPaint();
        g2d.setPaint(new RadialGradientPaint(
                (float) (centerX - Math.cos(Math.toRadians(45)) * radius),
                (float) (centerY - Math.sin(Math.toRadians(45)) * radius),
                (float) radius * 3.5f,
                new float[]{0.0f, 0.5f},
                new Color[]{gradientA, gradientB}));
        //dibujando gradiente
        g2d.fillOval(frame.x + 4, frame.y + 4, frame.width - 8, frame.height - 8);
        g2d.setPaint(defaultPaint);

        //definiendo propiedades del contorno
        g2d.setColor(lblColor);
        g2d.setStroke(new BasicStroke(10));

        //dibujando contorno del reloj
        g2d.drawOval(frame.x + 4, frame.y + 4, frame.width - 8, frame.height - 8);

        //dibujando minuteros
        for (int n = 0; n < 60; n++) {
            //definiendo valores geometricos
            angle = Math.toRadians((270 + (360 / 60) * n) % 360);
            double cos = Math.cos(angle);
            double sin = Math.sin(angle);
            double intRadius;

            //para horas
            if (n % 5 == 0) {
                //definiendo largo de la lina para horas
                intRadius = 0.9 * radius;
                //definiendo el grosor de los minuteros para horas
                g2d.setStroke(boldSt);

                //definiendo la hora
                int h = n / 5;
                if (n == 0) {
                    h = 12;
                }

                //definiendo posición de las horas
                Rectangle2D sb = fm.getStringBounds(String.valueOf(h), g2d);
                int x = (int) (-sb.getWidth() / 2 + (radius * cos * 0.8));
                int y = (int) ((fm.getAscent() - fm.getDescent()) / 2 + (radius * sin * 0.8));

                //dibujando texto horas
                g2d.drawString(String.valueOf(h), centerX + x, centerY + y);

            } else {
                //definiendo largo de la lina para minutos
                intRadius = 0.95 * radius;
                //definiendo el grosor de los minuteros para minutos
                g2d.setStroke(st);
            }

            //dibujar el minutero
            g2d.drawLine((int) (centerX + intRadius * cos), (int) (centerY + intRadius * sin), centerX + (int) (radius * cos), centerY + (int) (radius * sin));
        }

        //dibujar marca Javawatch
        if (size < 400) {
            g2d.setFont(getSmallFontTitle(g2d));
        } else if (size < 800) {
            g2d.setFont(getMediumFontTitle(g2d));
        } else {
            g2d.setFont(getLargeFontTitle(g2d));
        }
        
        Rectangle2D borderTitle = g2d.getFontMetrics().getStringBounds("JavaWatch", g2d);
        g2d.drawString("JavaWatch", (float)(centerX - borderTitle.getWidth() / 2), (float)(centerY + radius*0.3));

        //definiendo valor de x y y para manecillas
        int xSeg = (int) ((0.9 * radius) * Math.cos(Math.toRadians((270 + seg * (360 / 60)) % 360)));
        int ySeg = (int) ((0.9 * radius) * Math.sin(Math.toRadians((270 + seg * (360 / 60)) % 360)));
        int xMin = (int) ((0.7 * radius) * Math.cos(Math.toRadians((270 + min * (360 / 60)) % 360)));
        int yMin = (int) ((0.7 * radius) * Math.sin(Math.toRadians((270 + min * (360 / 60)) % 360)));
        int xHora = (int) ((0.5 * radius) * Math.cos(Math.toRadians((270 + hora * (360 / 12)) % 360)));
        int yHora = (int) ((0.5 * radius) * Math.sin(Math.toRadians((270 + hora * (360 / 12)) % 360)));

        //dibujar manecillas
        //--sombras
        //----horas
        g2d.setStroke(new BasicStroke(stHora));
        g2d.setColor(shadowColor);
        g2d.drawLine(centerX + 2, centerY + 2, centerX + xHora + 2, centerY + yHora + 2);
        //----minutos
        g2d.setStroke(new BasicStroke(stMin));
        g2d.setColor(shadowColor);
        g2d.drawLine(centerX - (int) (xMin * 0.15) + 2, centerY - (int) (yMin * 0.15) + 2, centerX + xMin + 2, centerY + yMin + 2);
        //----segundos
        g2d.setStroke(new BasicStroke(stSeg));
        g2d.setColor(shadowColor);
        g2d.drawLine(centerX - (int) (xSeg * 0.2) + 2, centerY - (int) (ySeg * 0.2) + 2, centerX + xSeg + 2, centerY + ySeg + 2);

        //--manecillas
        //----horas
        g2d.setStroke(new BasicStroke(stHora));
        g2d.setColor(horaColor);
        g2d.fillOval(centerX - 10, centerY - 10, 20, 20);
        g2d.drawLine(centerX, centerY, centerX + xHora, centerY + yHora);
        //----minutos
        g2d.setStroke(new BasicStroke(stMin));
        g2d.setColor(minColor);
        g2d.drawLine(centerX - (int) (xMin * 0.15), centerY - (int) (yMin * 0.15), centerX + xMin, centerY + yMin);
        //----segundos
        g2d.setStroke(new BasicStroke(stSeg));
        g2d.setColor(segColor);
        g2d.drawLine(centerX - (int) (xSeg * 0.2), centerY - (int) (ySeg * 0.2), centerX + xSeg, centerY + ySeg);
        //remache
        g2d.setColor(backgroundColor);
        g2d.fillOval(centerX - 3, centerY - 3, 6, 6);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JavaWatch1");
            frame.setMinimumSize(new Dimension(400, 400));
            JavaWatch7 mainPanel = new JavaWatch7();
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainPanel.stopTimer();
                }
            });
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
