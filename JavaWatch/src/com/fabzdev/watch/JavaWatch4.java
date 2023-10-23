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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionListener;
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
public class JavaWatch4 extends JPanel {

    Font smallFont;
    Font mediumFont;
    Font largeFont;
    int hora;
    int min;
    int seg;
    Timer timer;
    
    public JavaWatch4(){
        super();
        timer = new Timer(1000, e -> repaint());
        timer.start();
    }
    
    //detener timer
    public void stopTimer(){
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

    @Override
    protected void paintComponent(Graphics g) {
        //creando objetos para dibujar
        Graphics2D g2d = (Graphics2D) g;
        Rectangle bounds = new Rectangle(getWidth(), getHeight());

        //dibujando fondo negro
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, bounds.width, bounds.height);

        //definiendo color del lapiz a verde
        g2d.setColor(new Color(0, 0x85, 0));

        //definiendo tamaño del reloj
        int size = Math.min(bounds.width, bounds.height);
        Rectangle frame = new Rectangle(bounds.width / 2 - size / 2, bounds.height / 2 - size / 2, size, size);

        //definiendo coordenadas
        int centerX = frame.x + frame.width / 2;
        int centerY = frame.y + frame.height / 2;
        double radius = frame.width / 2;
        double angle;

        //definiendo grosor de linea
        Stroke st = g2d.getStroke();
        Stroke boldSt = new BasicStroke(2);
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
        FontMetrics fm = g2d.getFontMetrics();

        //dibujando contorno del reloj
        g2d.drawOval(frame.x, frame.y, frame.width, frame.height);

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

            //dibujar punto centro
            g2d.fillOval(centerX - 2, centerY - 2, 4, 4);

            //dibujar manecillas
            updatetime();
            g2d.setStroke(st);
            //segundos
            double secRadius = (0.9 * radius);
            angle = Math.toRadians((270 + seg * (360 / 60)) % 360);
            g2d.drawLine(centerX, centerY, centerX + (int) (secRadius * Math.cos(angle)), centerY + (int) (secRadius * Math.sin(angle)));
            //minutos
            double minRadius = (0.7 * radius);
            angle = Math.toRadians((270 + min * (360 / 60)) % 360);
            g2d.drawLine(centerX, centerY, centerX + (int) (minRadius * Math.cos(angle)), centerY + (int) (minRadius * Math.sin(angle)));
            //horas
            double horaRadius = (0.5 * radius);
            angle = Math.toRadians((270 + (hora * (360 / 12)) + (min * (360.0 / 720))) % 360);
            g2d.drawLine(centerX, centerY, centerX + (int) (horaRadius * Math.cos(angle)), centerY + (int) (horaRadius * Math.sin(angle)));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JavaWatch1");
            frame.setMinimumSize(new Dimension(400, 400));
            JavaWatch4 mainPanel = new JavaWatch4();
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
