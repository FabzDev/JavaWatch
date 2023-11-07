/*
 * Copyright (c) 2020, Raul Cosio
 * Todos los derechos reservados.
 * 
 *  (Todos los acentos han sido omitidos para maximizar compatibilidad en los diferentes sistemas)
 * 
 * La redistribucion y el uso en las formas de codigo fuente y binario, con o sin
 * modificaciones, estan permitidos siempre que se cumplan las siguientes condiciones:
 * 1. Las redistribuciones del codigo fuente deben conservar el aviso de copyright
 *    anterior, esta lista de condiciones y el siguiente descargo de responsabilidad.
 * 2. Las redistribuciones en formato binario deben reproducir el aviso de copyright
 *    anterior, esta lista de condiciones y el siguiente descargo de responsabilidad
 *    en la documentacion y/u otros materiales suministrados con la distribucion.
 * 3. Todo el material publicitario que mencione las funciones o utilice este software
 *    debe mostrar el siguiente reconocimiento:
 *    "Este producto incluye software desarrollado por Raul Cosio, https://java.aid2do.com".
 * 4. Ni el nombre de Raul Cosio ni los nombres de sus colaboradores pueden usarse
 *    para apoyar o promocionar productos derivados de este software sin permiso
 *    previo y por escrito.
 * 
 * ESTE SOFTWARE SE SUMINISTRA POR RAUL COSIO "COMO ESTA" Y CUALQUIER
 * GARANTIA EXPRESA O IMPLICITA, INCLUYENDO, PERO NO LIMITADO A, LAS GARANTIAS
 * IMPLICITAS DE COMERCIALIZACION Y APTITUD PARA UN PROPOSITO PARTICULAR SON
 * RECHAZADAS. EN NINGUN CASO RAUL COSIO SERA RESPONSABLE POR NINGUN
 * DAÑO DIRECTO, INDIRECTO, INCIDENTAL, ESPECIAL, EJEMPLAR O COSECUENCIAL
 * (INCLUYENDO, PERO NO LIMITADO A, LA ADQUISICION O SUSTITUCION DE BIENES O
 * SERVICIOS; LA PERDIDA DE USO, DE DATOS O DE BENEFICIOS; O INTERRUPCION DE LA
 * ACTIVIDAD EMPRESARIAL) O POR CUALQUIER TEORIA DE RESPONSABILIDAD, YA SEA POR
 * CONTRATO, RESPONSABILIDAD ESTRICTA O AGRAVIO (INCLUYENDO NEGLIGENCIA O CUALQUIER
 * OTRA CAUSA) QUE SURJA DE CUALQUIER MANERA DEL USO DE ESTE SOFTWARE, INCLUSO SI SE
 * HA ADVERTIDO DE LA POSIBILIDAD DE TALES DAÑOS.
 * 
 * https://java.aid2do.com
 * 
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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Reloj: Diseño orientado a objetos.
 *
 * @author rcosio
 * @date 10/oct/2019 12:15 pm
 */
public class Watch13 extends JPanel {
    private final Color backgroundColor = new Color(0x4040c0);  // Color de la caratula
    private final Color backgroundDateColor = new Color(0x101020); // Color de fondo calendario
    private final Color hourColor = Color.BLACK;                // Color manecilla hora
    private final Color minuteColor = Color.BLACK;              // Color manecilla minuto
    private final Color secColor = new Color(0xa00000);         // Color manecilla segundo
    private final Color labelColor = Color.BLACK;
    private final Color faceColor = new Color(0xc0c0c0);
    private final Color shadowColor = new Color(0x0, 0x0, 0x0, 0x30);
    private final Color dayColor = new Color(0x40, 0x40, 0xff);
    private final Color nightColor = new Color(0x10, 0x10, 0x70);
    private final Color labelDateColor = new Color(0xbdbd00);
    private final Color gradientA = new Color(224, 224, 224, 128);
    private final Color gradientB = new Color(32, 32, 32, 70);

    private float fsintable[] = new float[60 + 1];
    private float fcostable[] = new float[60 + 1];
    
    private int curFontIndex;    // font size index.
    private Font[] fontTitle;    // "Java Watch"
    private Font[] fontHours;    // Font para horas
    private Font[] calendarFont; // Font para el calendario
    
    private int month;  // fecha y hora actual
    private int day;    // fecha y hora actual
    private int hour;   // fecha y hora actual
    private int minute; // fecha y hora actual
    private int second; // fecha y hora actual
    private int ampm;   // fecha y hora actual (am:0, pm:1)
    
    private final int hourStroke = 12;
    private final int minuteStroke = 5;
    private final int secondStroke = 3;

    private WatchTimer watchTimer;
    private ClockCover clockCover;
    private ClockHands clockHands;
    
    private static final String[] monthNames = new String[] {"ENE","FEB","MAR","ABR","MAY","JUN","JUL","AGO","SEP","OCT","NOV","DIC"};

    private class WatchTimer {
        private int lastSecond = -1; // Para garantizar que la primera vez siempre sea diferente a second
        private int lastMinute = -1; // Para garantizar que la primera vez siempre sea diferente a minute
        private Set<ClockComponent> secondListeners = new HashSet<>();
        private Set<ClockComponent> minuteListeners = new HashSet<>();
        private javax.swing.Timer timer;
        
        private WatchTimer() {
            updateTime();
            timer = new javax.swing.Timer(1000, e -> {
                updateTime();
                repaint();
            });
            timer.start();
        }
        
        private void stop() {
            timer.stop();
        }
        
        private void setMinuteListener(ClockComponent c) {
            minuteListeners.add(c);
        }
    
        private void removeMinuteListener(ClockComponent c) {
            minuteListeners.remove(c);
        }

        private void setSecondListener(ClockComponent c) {
            secondListeners.add(c);
        }

        private void removeSecondListener(ClockComponent c) {
            secondListeners.remove(c);
        }
        
        private void updateTime() {
            Calendar cal = Calendar.getInstance();
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DATE);
                hour = cal.get(Calendar.HOUR);
                minute = cal.get(Calendar.MINUTE);
                second = cal.get(Calendar.SECOND);
                ampm = cal.get(Calendar.AM_PM);

            if (second != lastSecond) {
                for (ClockComponent c : secondListeners)
                    c.update();
                lastSecond = second;
            }
            if (minute != lastMinute) {
                for (ClockComponent c : minuteListeners)
                    c.update();
                lastMinute = minute;
            }
        }
    }
    
    private class ClockHands {
        private Rectangle bounds;
        
        private ClockHands() {
        }
        
        private void setBounds(Rectangle bounds) {
            this.bounds = bounds;
        }
        
        private void paint(Graphics2D g2d) {
            int x0,x1,x2;
            int y0,y1,y2;
            
            int centerX = bounds.x + bounds.width/2;
            int centerY = bounds.y + bounds.height/2;

            float radius = bounds.width/2f - 10f;
            double hourRadius = radius * 0.5f;
            float minRadius = radius * 0.7f;
            double secRadius = radius * 0.9f;

            double angle = (270 + (double)hour*(360.0/12) + (double)minute*(360.0/(12*60))) % 360;
            x0 = (int) (Math.cos(Math.toRadians(angle)) * hourRadius);
            y0 = (int) (Math.sin(Math.toRadians(angle)) * hourRadius);
            x1 = (int) (cos(minute) * minRadius);
            y1 = (int) (sin(minute) * minRadius);
            x2 = (int) (cos(second) * secRadius);
            y2 = (int) (sin(second) * secRadius);

            // Sombra de manecillas
            g2d.setStroke(new BasicStroke(hourStroke));
            g2d.setColor(shadowColor);
            g2d.fillOval(centerX-10+2, centerY-10+2, 20, 20);
            g2d.drawLine(centerX+2, centerY+2, centerX + x0+2, centerY + y0+2);
            g2d.setStroke(new BasicStroke(minuteStroke));
            g2d.drawLine((int)(centerX - x1*0.15f)+2, (int)(centerY - y1*0.15f)+2, centerX + x1+2, centerY + y1+2);
            g2d.setStroke(new BasicStroke(secondStroke));
            g2d.drawLine((int)(centerX - x2*0.2f)+3, (int)(centerY - y2*0.2f)+3, centerX + x2 + 3, centerY + y2 + 3);

            // Manecillas
            g2d.setStroke(new BasicStroke(hourStroke));
            g2d.setColor(hourColor);
            g2d.fillOval(centerX-10, centerY-10, 20, 20);
            g2d.drawLine(centerX, centerY, centerX + x0, centerY + y0);
            g2d.setStroke(new BasicStroke(minuteStroke));
            g2d.setColor(minuteColor);
            g2d.drawLine((int)(centerX - x1*0.15), (int)(centerY - y1*0.15), centerX + x1, centerY + y1);
            g2d.setStroke(new BasicStroke(secondStroke));
            g2d.setColor(secColor);
            g2d.drawLine((int)(centerX - x2*0.2), (int)(centerY - y2*0.2), centerX + x2, centerY + y2);

            // Remache
            g2d.setColor(backgroundColor);
            g2d.fillOval(centerX-3, centerY-3, 6, 6);
        }
    }
    
    private abstract class ClockComponent {
        protected abstract void setBounds(Rectangle bounds);
        protected abstract void paint(Graphics2D g2d);
        protected boolean isDirty() {
            return true;
        }
        protected void update() {
        }
    }
    
    private class ClockCover {
        private final String title = "Java Watch";
        private Rectangle frame;
        private Rectangle bounds;
        private BufferedImage backgroundImage;
        private List<ClockComponent> children;
        
        private ClockCover() {
            children = new LinkedList<>();
            DayNightCover dayNight = new DayNightCover();
            children.add(dayNight);
            CalendarCover calendarCover = new CalendarCover();
            children.add(calendarCover);
        }
        
        private void setBounds(Rectangle frame) {
            this.frame = frame;

            int size = Math.min(frame.width, frame.height);
            bounds = new Rectangle(frame.width/2 - size/2, frame.height/2 - size/2, size, size);
            for (ClockComponent child : children) {
                child.setBounds(bounds);
            }
            redrawBackgroundImage();
        }
        
        private void redrawBackgroundImage() {
            if ((backgroundImage == null) || (backgroundImage.getWidth() != frame.width) || (backgroundImage.getHeight() != frame.height))
                backgroundImage = new BufferedImage(frame.width, frame.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) backgroundImage.getGraphics();
            g2d.setColor(backgroundColor);
            g2d.fillRect(frame.x, frame.y, frame.width, frame.height);

            int centerX = bounds.x + bounds.width/2;
            int centerY = bounds.y + bounds.height/2;
            float radius = bounds.width/2f - 10f;

            // Color de caratula
            g2d.setColor(faceColor);
            g2d.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);

            // Sombra en caratula
            Paint savePaint = g2d.getPaint();
            RadialGradientPaint rp = new RadialGradientPaint(
                    (float)(centerX - Math.cos(Math.toRadians(45))*radius), 
                    (float)(centerY - Math.sin(Math.toRadians(45))*radius), (float)radius*3.5f,
                    new float[] {0.0f, 0.5f},
                    new Color[] {gradientA, gradientB});
            g2d.setPaint(rp);
            g2d.fillOval(bounds.x + 4, bounds.y + 4, bounds.width-8, bounds.height-8);
            g2d.setPaint(savePaint);

            // Marco del reloj
            g2d.setColor(labelColor);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawOval(bounds.x+4, bounds.y+4, bounds.width-8, bounds.height-8);

            Font f;
            Font fTitle;
            if (bounds.width < 450) {
                curFontIndex = 0;
                f = fontHours[0];
                fTitle = fontTitle[0];
            } else if (bounds.width < 800) {
                curFontIndex = 1;
                f = fontHours[1];
                fTitle = fontTitle[1];
            } else {
                curFontIndex = 2;
                f = fontHours[2];
                fTitle = fontTitle[2];
            }
            g2d.setFont(f);
            FontMetrics fm = g2d.getFontMetrics();

            Stroke simpleStroke = new BasicStroke(1);
            Stroke boldStroke = new BasicStroke(2);

            // Lineas segundos/horas
            int x0;
            int y0;
            float innerRadius;
            for (int n=0; n<60; n++) {
                float cos = cos(n);
                float sin = sin(n);
                x0 = (int) (cos * radius);
                y0 = (int) (sin * radius);
                if ((n % 5) == 0) {
                    innerRadius = radius * 0.9f;
                    int h = n / 5;
                    if (h == 0)
                        h = 12;
                    Rectangle2D r = fm.getStringBounds(String.valueOf(h), g2d);
                    int x_ = (int)(-(r.getWidth()/2) + radius*cos*0.8);
                    int y_ = (int)(r.getHeight()/2 - fm.getDescent() + radius*sin*0.8);
                    g2d.drawString(String.valueOf(h), centerX + x_, centerY + y_);
                    g2d.setStroke(boldStroke);
                } else {
                    innerRadius = radius * 0.95f;
                    g2d.setStroke(simpleStroke);
                }
                g2d.drawLine(centerX + (int)(cos * innerRadius), centerY + (int)(sin * innerRadius), centerX + x0, centerY + y0);
            }

            // Titulo
            g2d.setFont(fTitle);
            Rectangle2D r = g2d.getFontMetrics().getStringBounds(title, g2d);
            g2d.drawString(title, (int)(centerX - r.getWidth()/2), (int)(centerY + radius*0.2)); // Java Watch

            g2d.dispose();
        }
        
        private void paint(Graphics2D g2d) {
            Graphics2D innerGraphics = null;
            for (ClockComponent child : children) {
                if (child.isDirty()) {
                    if (innerGraphics == null)
                        innerGraphics = backgroundImage.createGraphics();
                    child.paint(innerGraphics);
                }
            }
            if (innerGraphics != null)
                innerGraphics.dispose();
            
            g2d.drawImage(backgroundImage, 0, 0, null);
        }
    }
    
    private class DayNightCover extends ClockComponent {
        private Rectangle bounds;
        private BufferedImage dayNightImage;
        private Area dnClip;            // Region para hacer clip a imagen dia/noche
        private boolean dirty;
        private float dnRadius;         // Radio de imagen dia/noche

        private DayNightCover() {
            watchTimer.setMinuteListener(this);
        }

        @Override
        protected void update() {
            dirty = true;
        }

        @Override
        protected boolean isDirty() {
            return dirty;
        }
        
        @Override
        protected void setBounds(Rectangle bounds) {
            this.bounds = bounds;
            createDayNightImage();
            dirty = true;
        }
        
        private void createDayNightImage() {
                        System.out.println("DayNightCover.createDayNightImage");

            dnRadius = bounds.width/2f * 0.65f; // day/night radius
            int width = (int)(dnRadius * 2);
            dayNightImage = new BufferedImage(width, width, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d =  dayNightImage.createGraphics();

            int centerX = width/2;
            int centerY = width/2;

            // Firmamento
            g2d.setColor(dayColor);
            g2d.fillRect(0, 0, width, width);
            g2d.setColor(nightColor);
            g2d.fillRect(0, centerY, width, width);
            Paint savePaint = g2d.getPaint();
            GradientPaint grad = new GradientPaint(0, width/2 - width/16, dayColor, 0, width/2 + width/16, nightColor);
            g2d.setPaint(grad);
            g2d.fillRect(0, 0, width, width);
            g2d.setPaint(savePaint);
            grad = null;

            // Sol
            float sunRadius = dnRadius*0.15f;
            g2d.setColor(Color.orange);
            fillStar(g2d, (int)(centerX), (int)(centerY-dnRadius+sunRadius*2), (int)(sunRadius*2f), (int)(sunRadius*1.2f), 12, 0);
            g2d.setColor(Color.yellow);
            g2d.fillOval((int)(centerX-sunRadius), (int)(centerY-dnRadius+sunRadius), (int)(sunRadius*2), (int)(sunRadius*2));

            // Luna
            float moonRadius = dnRadius*0.2f;
            Arc2D arc = new Arc2D.Float(centerX-moonRadius, centerY+dnRadius-moonRadius*3, moonRadius*2, moonRadius*2, 0, 360, Arc2D.CHORD);
            Area moon = new Area(arc);
            arc = new Arc2D.Float(centerX-moonRadius-moonRadius*0.7f, centerY+dnRadius-moonRadius*3, moonRadius*2, moonRadius*2, 0, 360, Arc2D.CHORD);
            moon.subtract(new Area(arc));
            g2d.setColor(Color.white);
            g2d.fill(moon);

            // Estrellas
            double angle;
            g2d.setColor(Color.yellow);
            for (int n=0; n<4; n++) {
                angle = (180.0-(180.0/8) - n*(180.0/4)) % 360;
                float x = (float)(Math.cos(Math.toRadians(angle)) * dnRadius*0.75);
                float y = (float)(Math.sin(Math.toRadians(angle)) * dnRadius*0.75);
                fillStar(g2d, (int)(centerX+x), (int)(centerY+y), (int)(dnRadius*0.1f), (int)(dnRadius*0.05f), 5, (int)(270-180.0/8-n*180.0/4));
            }
            for (int n=0; n<2; n++) {
                angle = (180.0-(180.0/4) - n*(180.0/2)) % 360;
                float x = (float)(Math.cos(Math.toRadians(angle)) * dnRadius*0.35);
                float y = (float)(Math.sin(Math.toRadians(angle)) * dnRadius*0.35);
                fillStar(g2d, (int)(centerX+x), (int)(centerY+y), (int)(dnRadius*0.1f), (int)(dnRadius*0.05f), 5, (int)(270-180.0/4-n*180.0/2));
            }
            g2d.dispose();
            g2d = null;

            // Crear clip para imagen dia/noche
            centerX = bounds.x + bounds.width/2; // Centrado en las coordenadas del reloj
            centerY = bounds.y + bounds.width/2;

            // Dia/noche
            arc = new Arc2D.Float(centerX-dnRadius, centerY-dnRadius, dnRadius*2, dnRadius*2, 10, 180, Arc2D.CHORD);
            dnClip = new Area(arc);
            arc = new Arc2D.Float(centerX-dnRadius*1.1f, centerY-dnRadius*1.1f, dnRadius*2.2f, dnRadius*2.2f, 180-10, 180, Arc2D.CHORD);
            dnClip.subtract(new Area(arc));
            arc = new Arc2D.Float(centerX-dnRadius*0.2f, centerY-dnRadius*0.2f, dnRadius*0.4f, dnRadius*0.4f, 0, 180, Arc2D.CHORD);
            dnClip.subtract(new Area(arc));

            // Redondear extremos
            float smoothLen = 0.15f;
            int testX = (int)(centerX + dnRadius*(1-smoothLen)*Math.cos(Math.toRadians(10)));
            int testY = (int)(centerY - dnRadius*(1-smoothLen)*Math.sin(Math.toRadians(10)));

            int testX2 = (int)(centerX + dnRadius*Math.cos(Math.toRadians(10)));
            int testY2 = (int)(centerY - dnRadius*Math.sin(Math.toRadians(10)));

            int testX3 = (int)(centerX + dnRadius*Math.cos(Math.toRadians(10)+smoothLen));
            int testY3 = (int)(centerY - dnRadius*Math.sin(Math.toRadians(10)+smoothLen));

            int testX4 = (int)(centerX + dnRadius*1.05f*Math.cos(Math.toRadians(10)+smoothLen));
            int testY4 = (int)(centerY - dnRadius*1.05f*Math.sin(Math.toRadians(10)+smoothLen));

            GeneralPath gp = new GeneralPath();
            gp.moveTo(testX, testY);
            gp.quadTo(testX2, testY2, testX3, testY3);
            gp.lineTo(testX4, testY4);
            testX2 = (int)(centerX + dnRadius*1.05f*Math.cos(Math.toRadians(10-1)));
            testY2 = (int)(centerY - dnRadius*1.05f*Math.sin(Math.toRadians(10-1)));
            gp.lineTo(testX2, testY2);
            gp.closePath();
            dnClip.subtract(new Area(gp));

            testX = (int)(centerX - dnRadius*(1-smoothLen)*Math.cos(Math.toRadians(10)));
            testX2 = (int)(centerX - dnRadius*Math.cos(Math.toRadians(10)));
            testX3 = (int)(centerX - dnRadius*Math.cos(Math.toRadians(10)+smoothLen));
            testX4 = (int)(centerX - dnRadius*1.05f*Math.cos(Math.toRadians(10)+smoothLen));

            gp = new GeneralPath();
            gp.moveTo(testX, testY);
            gp.quadTo(testX2, testY2, testX3, testY3);
            gp.lineTo(testX4, testY4);
            testX2 = (int)(centerX - dnRadius*1.05f*Math.cos(Math.toRadians(10-1)));
            gp.lineTo(testX2, testY2);
            gp.closePath();
            dnClip.subtract(new Area(gp));
        }
        
        @Override
        protected void paint(Graphics2D g2d) {
            System.out.println("DayNightCover.paint");
            Graphics2D g2dback = g2d;
            
            Shape saveClip = g2dback.getClip();
            g2dback.setClip(dnClip);

            int centerX = bounds.x + bounds.width/2;
            int centerY = bounds.y + bounds.height/2;
            float radius = bounds.width/2f - 10f;
            double angle;
            
            int hour24 = hour + 12*ampm;
            AffineTransform saveTrans = g2dback.getTransform();
            AffineTransform trans = new AffineTransform();
            trans.translate(centerX, centerY);
            angle = (double)(180.0 + hour24*(360.0/24) + (double)(minute*(360.0/(24*60)))) % 360;
            trans.rotate(Math.toRadians(angle));
            g2dback.setTransform(trans);
            g2dback.drawImage(dayNightImage, -dayNightImage.getWidth()/2, -dayNightImage.getHeight()/2, null);
            g2dback.setTransform(saveTrans);

            // sombreado en dia/noche
            Paint savePaint = g2dback.getPaint();
            RadialGradientPaint rp = new RadialGradientPaint(
                    (float)(centerX - Math.cos(Math.toRadians(45))*radius), 
                    (float)(centerY - Math.sin(Math.toRadians(45))*radius), (float)radius*3.5f,
                    new float[] {0.0f, 0.2f},
                    new Color[] {new Color(224, 224, 224, 128), new Color(32, 32, 32, 70)});
            g2dback.setPaint(rp);
            g2dback.fillOval(bounds.x + 4, bounds.y + 4, bounds.width-8, bounds.height-8);
            g2dback.setPaint(savePaint);
            g2dback.setClip(saveClip);
            
            dirty = false;
        }
    }
    
    private class CalendarCover extends ClockComponent {
        private Rectangle bounds;
        private boolean dirty;
        private boolean secondListening;
        private int lastDay = -1;
        private int day_next;
        private int month_next;
        private Dimension calDateSize;
        private Dimension calMonthSize;

        public CalendarCover() {
            watchTimer.setMinuteListener(this);
            secondListening = false;
        }
        
        @Override
        protected void setBounds(Rectangle bounds) {
            this.bounds = bounds;
            lastDay = day;
            dirty = true;
        }

        @Override
        protected boolean isDirty() {
            return dirty;
        }
        
        private void startSecondListening() {
            if (!secondListening) {
                watchTimer.setSecondListener(this);
                secondListening = true;
                
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, 1);
                day_next = cal.get(Calendar.DATE);
                month_next = cal.get(Calendar.MONTH);
            }
        }
        
        private void stopSecondListening() {
            watchTimer.removeSecondListener(this);
            secondListening = false;
            dirty = true; // Ultima actualizacion de cambio de dia.
        }

        @Override
        protected void update() {
            if (day != lastDay) {
                dirty = true;
                lastDay = day;
            }
            
            if ((ampm == 1) && (hour == 11) && (minute == 59)) {
                startSecondListening();
                dirty = true;
            } else if (secondListening) {
                stopSecondListening();
            }
        }
        
        private void updateCalendarMetrics(Graphics2D g2d) {
            calDateSize = new Dimension();
            calMonthSize = new Dimension();

            Font saveFont = g2d.getFont();
            g2d.setFont(calendarFont[curFontIndex]);
            FontMetrics fm = g2d.getFontMetrics();
            calDateSize.height = fm.getHeight();
            calMonthSize.height = fm.getHeight();
            for (int n=1; n<=31; n++) {
                String s = (n < 10) ? "0" + n : String.valueOf(n);
                int width = fm.stringWidth(s);
                if (width > calDateSize.width)
                    calDateSize.width = width;
            }
            for (int n=0; n<monthNames.length; n++) {
                String s = monthNames[n];
                int width = fm.stringWidth(s);
                if (width > calMonthSize.width)
                    calMonthSize.width = width;
            }
            g2d.setFont(saveFont);
        }
        
        @Override
        protected void paint(Graphics2D g2d) {
            System.out.println("CalendarCover.paint");
            
            updateCalendarMetrics(g2d);
            int centerY = bounds.y + bounds.height/2;
            
            float radius = bounds.width/2f - 10f;
            int yPos = centerY + (int)(radius * 0.3f);

            // background
            Rectangle calendarRect = new Rectangle(0, 0, calDateSize.width + calMonthSize.width + 8*3,
                                                         calDateSize.height);
            calendarRect.x = bounds.x + (bounds.width - calendarRect.width) / 2;
            calendarRect.y = yPos;
            RoundRectangle2D roundRect = new RoundRectangle2D.Float(calendarRect.x, calendarRect.y, 
                                                                    calendarRect.width, calendarRect.height,
                                                                    calendarRect.height/2, calendarRect.height/2);
            g2d.setColor(backgroundDateColor);
            g2d.fill(roundRect);
            roundRect.setRoundRect(calendarRect.x+2, calendarRect.y+2, 
                                   calendarRect.width-4, calendarRect.height-4,
                                   (calendarRect.height-4)/2, (calendarRect.height-4)/2);
            Shape saveClip = g2d.getClip();
            g2d.setClip(new Area(roundRect));

            // Dia
            g2d.setColor(labelDateColor);
            g2d.setFont(calendarFont[curFontIndex]);
            FontMetrics fm = g2d.getFontMetrics();
            int x = calendarRect.x + 8;
            int y = calendarRect.y + fm.getAscent();
            if (secondListening) {
                y -= second*((double)calendarRect.height/60);
            }
            g2d.drawString(day < 10 ? "0" + day : String.valueOf(day), x, y);
            if (secondListening) {
                g2d.drawString(day_next < 10 ? "0" + day_next : String.valueOf(day_next), x, y + calendarRect.height);
            }

            // Mes
            x = calendarRect.x + 8 + calDateSize.width + 8;
            y = calendarRect.y + fm.getAscent();
            if (secondListening && (month != month_next)) {
                y -= (int)( (second)*((double)calendarRect.height/60) );
            }
            g2d.drawString(monthNames[month], x, y);
            if (secondListening && (month != month_next)) {
                g2d.drawString(monthNames[month_next], x, y + calendarRect.height);
            }

            g2d.setClip(saveClip);
            
            dirty = false;
        }
    }
    
    public Watch13() {
        super();
        initSinCosTable();
        initFonts();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelResized();
            }
        });
        watchTimer = new WatchTimer();
    }
    
    private void stopTimer() {
        watchTimer.stop();
    }
    
    private void panelResized() {
        Rectangle bounds = getBounds();
        
        int size = Math.min(bounds.width, bounds.height) - 1;
        Rectangle frame = new Rectangle(bounds.width/2 - size/2, bounds.height/2 - size/2, size, size);
        
        if (clockCover == null) {
            clockCover = new ClockCover();
        }
        clockCover.setBounds(bounds);
        
        if (clockHands == null) {
            clockHands = new ClockHands();
        }
        clockHands.setBounds(frame);
    }
    
    private void initSinCosTable() {
        for (int n=0; n<fsintable.length; n++) {
            double angle = (270 + 360.0/60 * (double)n) % 360;
            double d = Math.sin(Math.toRadians(angle));
            fsintable[n] = (float) d;
            d = Math.cos(Math.toRadians(angle));
            fcostable[n] = (float) d;
        }
    }
    
    private void initFonts() {
        BufferedImage bi = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        Font stdFont = g2d.getFont();
        
        calendarFont = new Font[3];
        calendarFont[0] = stdFont.deriveFont(Font.BOLD, 28f);
        calendarFont[1] = stdFont.deriveFont(Font.BOLD, 42f);
        calendarFont[2] = stdFont.deriveFont(Font.BOLD, 84f);
        
        fontTitle = new Font[3];
        fontTitle[0] = g2d.getFont().deriveFont(12f);
        fontTitle[1] = g2d.getFont().deriveFont(18f);
        fontTitle[2] = g2d.getFont().deriveFont(24f);
        
        fontHours = new Font[3];
        fontHours[0] = g2d.getFont().deriveFont(24f);
        fontHours[1] = g2d.getFont().deriveFont(36f);
        fontHours[2] = g2d.getFont().deriveFont(72f);
        
        g2d.dispose();
    }
    
    private float sin(int angle60) {
        return fsintable[angle60];
    }
    
    private float cos(int angle60) {
        return fcostable[angle60];
    }
    
//    private static final boolean DEBUGFLAG = false;
//    private static boolean initFlag = false;
    
    private void fillStar(Graphics2D g2d, int x, int y, int radius, int innerRadius, int nSpikes, int rotation) {
        int xPoints[] = new int[nSpikes*2];
        int yPoints[] = new int[xPoints.length];
        
        for (int n=0; n<xPoints.length; n+=2) {
            double angle1 = (270 + rotation + (360.0/(xPoints.length/2))*(n/2)) % 360;
            double angle2 = (270 + rotation + (360.0/(xPoints.length/2))*(n/2) + (360.0/xPoints.length) ) % 360;
            xPoints[n] = x + (int) (Math.cos(Math.toRadians(angle1)) * radius);
            xPoints[n+1] = x + (int) (Math.cos(Math.toRadians(angle2)) * innerRadius);
            yPoints[n] = y + (int) (Math.sin(Math.toRadians(angle1)) * radius);
            yPoints[n+1] = y + (int) (Math.sin(Math.toRadians(angle2)) * innerRadius);
        }
        
        g2d.fillPolygon(xPoints, yPoints, xPoints.length);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        
        if (clockCover != null)
            clockCover.paint(g2d);
        if (clockHands != null)
            clockHands.paint(g2d);
    }
    
    public static void main(String[] args) throws Exception {
        Watch13 mainPanel = new Watch13();
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setTitle("Watch 13 - Raul Cosio");
            frame.setMinimumSize(new Dimension(400,400));
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainPanel.stopTimer();                }
            });
            frame.setContentPane(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
