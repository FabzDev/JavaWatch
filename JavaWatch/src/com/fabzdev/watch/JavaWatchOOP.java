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
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author fabio
 */
public class JavaWatchOOP extends JPanel {

    //variables OOP
    ClockHands clockHands = new ClockHands();
    ClockCover clockCover = new ClockCover();
    WatchTimer watchTimer = new WatchTimer();
    
    
    //variables no OPP
    //definiendo variables de colores
    Color backgroundColor = new Color(0x4040c0);
    Color horaColor = Color.BLACK;
    Color minColor = Color.BLACK;
    Color segColor = new Color(0xa00000);
    Color faceColor = new Color(0xc0c0c0);
    Color lblColor = Color.BLACK;
    Color gradientA = new Color(224, 224, 224, 120);
    Color gradientB = new Color(32, 32, 32, 50);
    Color shadowColor = new Color(0x0, 0x0, 0x0, 0x30);
    Color backgroundCalColor = new Color(0x101020);
    Color labelCalColor = new Color(0xbdbd00);
    Color dayColor = new Color(0x40, 0x40, 0xff);
    Color nightColor = new Color(0x10, 0x10, 0x70);

    //definiendo strokes manecillas
    int stHora = 12;
    int stMin = 5;
    int stSeg = 3;
    //definiendo variables de tiempo
    int mes;
    int dia;
    int hora;
    int min;
    int seg;
    int ampm;
    int lastMinute;
    Timer timer;
    boolean updatingCalendar;
    int diaNext;
    int mesNext;
    //definiendo BufferedImages
    BufferedImage buffimg;
    BufferedImage dayNightImg;
    //definiendo arreglos de X y Y
    float[] xArr = new float[61];
    float[] yArr = new float[61];
    //definiendo variable clip
    Shape clip;
    //definiendo indicador de tamaño de pantalla
    int sizeIndicator;
    float[] clockFont = {24f, 36f, 48f};
    float[] titleFont = {12f, 18f, 24f};
    Font[] calendarFont = new Font[3];

    //definiendo variables calendario
    private Dimension calDaySize;
    private Dimension calMonthSize;
    private String[] months = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};
    private Rectangle calendarFrame;

    //constructor
    public JavaWatchOOP() {
        super();
        fillX(); //llenando arrays trigonometricos
        fillY();
        initFonts(); //llenando arrays fuentes

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelResized();
            }

        });

        watchTimer = new WatchTimer();
    }

    //clase ClockComponent
    private abstract class ClockComponent {

        protected abstract void setBounds(Rectangle bounds);

        protected abstract void paint(Graphics2D g2d);

        protected boolean isDirty() {
            return true;
        }

        protected void update() {
        }
    }

    //clase ClockCover
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
            bounds = new Rectangle(frame.width / 2 - size / 2, frame.height / 2 - size / 2, size, size);
            for (ClockComponent comp : children) {
                comp.setBounds(bounds);
            }
            redrawBackgroundImage();
        }

        private void redrawBackgroundImage() {
            System.out.println("redrawing Background Image");
            if (backgroundImage == null || backgroundImage.getWidth() != frame.width || backgroundImage.getHeight() != frame.height) {
                backgroundImage = new BufferedImage(frame.width, frame.height, BufferedImage.TYPE_INT_RGB);
            }

            Graphics2D g2d = (Graphics2D) backgroundImage.getGraphics();

            //dibujando background
            g2d.setColor(backgroundColor);
            g2d.fillRect(frame.x, frame.y, frame.width, frame.height);

            //definiendo coordenadas
            int centerX = bounds.x + bounds.width / 2;
            int centerY = bounds.y + bounds.height / 2;
            float radius = bounds.width / 2f - 10f;
            double angle;

            //dibujando face-background del reloj
            g2d.setColor(faceColor);
            g2d.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);

            //definiendo sombra caratula
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

            //definiendo y dibujando marco del reloj
            g2d.setColor(lblColor);
            g2d.setStroke(new BasicStroke(10));
            g2d.drawOval(frame.x + 4, frame.y + 4, frame.width - 8, frame.height - 8);

            //obteniendo FontMetrics
            FontMetrics fm = g2d.getFontMetrics();

            //definiendo grosores de linea y tamaño de fuente de la caratula del reloj
            g2d.setFont(g2d.getFont().deriveFont(clockFont[sizeIndicator]));
            Stroke st = g2d.getStroke();
            Stroke boldSt = new BasicStroke(2);

            //dibujando minuteros
            for (int n = 0; n < 60; n++) {
                //definiendo valores geometricos
                angle = Math.toRadians((270 + (360 / 60) * n) % 360);
                double cos = Math.cos(angle);
                double sin = Math.sin(angle);
                float intRadius;

                //para horas
                if (n % 5 == 0) {
                    //definiendo largo de la lina para horas
                    intRadius = 0.9f * radius;
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
                    //definiendo largo de la linea para minutos
                    intRadius = 0.95f * radius;
                    //definiendo el grosor de los minuteros para minutos
                    g2d.setStroke(st);
                }

                //dibujar minuteros/segunderos
                g2d.drawLine((int) (centerX + intRadius * cos), (int) (centerY + intRadius * sin), centerX + (int) (radius * cos), centerY + (int) (radius * sin));
            }

            //dibujar marca Javawatch
            g2d.setFont(g2d.getFont().deriveFont(titleFont[sizeIndicator]));
            Rectangle2D borderTitle = g2d.getFontMetrics().getStringBounds("JavaWatch", g2d);
            g2d.drawString("JavaWatch", (float) (centerX - borderTitle.getWidth() / 2), (float) (centerY + radius * 0.2));

            //cerrar el entorno de dibujo e la imagen
            g2d.dispose();

        }

        private void paint(Graphics2D g2d) {
            Graphics2D innerGraphic = null;
            for (ClockComponent child : children) {
                if (child.isDirty()) {
                    if (innerGraphic == null) {
                        innerGraphic = backgroundImage.createGraphics();
                    }
                    child.paint(innerGraphic);
                }
            }

            if (innerGraphic != null) {
                innerGraphic.dispose();
            }

            g2d.drawImage(backgroundImage, 0, 0, null);
        }

    }

    //clase WatchTimer
    private class WatchTimer {

        private int lastSecond = -1;
        private int lastMinute = -1;
        private Set<ClockComponent> secondListeners = new HashSet<>();
        private Set<ClockComponent> minuteListeners = new HashSet<>();
        private javax.swing.Timer timer;

        //constructor timer
        private WatchTimer() {
            updateTime();
            timer = new javax.swing.Timer(1000, (e) -> {
                updateTime();
                repaint();
            });
            timer.start();
        }

        //detener timer
        private void stopTimer() {
            timer.stop();
        }

        private void setSecondListener(ClockComponent secondListenerComponent) {
            minuteListeners.add(secondListenerComponent);
        }

        private void removeSecondListener(ClockComponent secondListenerComponent) {
            minuteListeners.remove(secondListenerComponent);
        }

        private void setMinuteListener(ClockComponent minuteListenerComponent) {
            minuteListeners.add(minuteListenerComponent);
        }

        private void removeMinuteListener(ClockComponent minuteListenerComponent) {
            minuteListeners.remove(minuteListenerComponent);
        }

        //definir hora
        private void updateTime() {
            Calendar time = Calendar.getInstance();
            mes = time.get(Calendar.MONTH);
            dia = time.get(Calendar.DAY_OF_MONTH);
            hora = time.get(Calendar.HOUR);
            min = time.get(Calendar.MINUTE);
            seg = time.get(Calendar.SECOND);
            ampm = time.get(Calendar.AM_PM);

            // hora = 11;
            // min = 59;
            // ampm = 1;
            if (hora == 11 && min == 59 && ampm == 1) {
                updatingCalendar = true;
                time.add(Calendar.DATE, 1);
                diaNext = time.get(Calendar.DAY_OF_MONTH);
                mesNext = time.get(Calendar.MONTH);
            } else {
                updatingCalendar = false;
            }

            if (seg != lastSecond) {
                for (ClockComponent cc : secondListeners) {
                    cc.update();
                }
                lastSecond = seg;
            }

            if (min != lastMinute) {
                for (ClockComponent cc : minuteListeners) {
                    cc.update();
                }
                lastMinute = min;
            }

        }

    }

    //clase DayNightCover
    private class DayNightCover extends ClockComponent {

        private Rectangle bounds;
        private BufferedImage dayNightImage;
        private Area dnClip;
        private boolean dirty;
        private float dnRadius;

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

        @Override
        protected void paint(Graphics2D g2d) {

        }

        //OLDY CODE
        //crear imagen base del firmamento
        private void createDayNightImage() {
            System.out.println("Creating DayNight Image");

            int size = (int) (bounds.width * 0.65);
            dnRadius = size / 2;
            dayNightImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = dayNightImage.createGraphics();
            int astrosRadius = (int) (size / 7);

            //firmamento con gradiente
            g2d.setColor(dayColor);
            g2d.fillRect(0, 0, size, size);
            g2d.setColor(nightColor);
            g2d.fillRect(0, size / 2, size, size);

            Paint savedPaint = g2d.getPaint();
            GradientPaint gpaint = new GradientPaint(0, size / 2 - size / 16, dayColor, 0, size / 2 + size + 16, nightColor);
            g2d.setPaint(gpaint);
            g2d.fillRect(0, 0, size, size);
            g2d.setPaint(savedPaint);
            gpaint = null;

            //dibujando sol
            drawSun(g2d, size / 2, size / 6, astrosRadius, (int) (astrosRadius / 1.6), 12, 0);

            //dibujando estrellas en el firmamento
            g2d.setColor(Color.YELLOW);
            int angle = 0;
            for (int n = 0; n < 4; n++) {
                if (n == 0) {
                    angle = 15;
                } else {
                    angle += 50;
                }
                int posX = size / 2 + (int) (size / 2 * 0.8 * Math.cos(Math.toRadians(angle)));
                int posY = size / 2 + (int) (size / 2 * 0.8 * Math.sin(Math.toRadians(angle)));
                drawStar(g2d, posX, posY, (int) (size / 20), (int) (size / 40), 5, n * 3 + 5);
            }
            for (int n = 0; n < 2; n++) {
                if (n == 0) {
                    angle = 40;
                } else {
                    angle += 100;
                }
                int posX = size / 2 + (int) (size / 2 * 0.3 * Math.cos(Math.toRadians(angle)));
                int posY = size / 2 + (int) (size / 2 * 0.3 * Math.sin(Math.toRadians(angle)));
                drawStar(g2d, posX, posY, (int) (size / 20), (int) (size / 40), 5, n * 3 + 10);
            }

            //dibujando luna
            float moonRadius = astrosRadius * 0.8f;
            g2d.setColor(Color.WHITE);
            Arc2D lunaArc = new Arc2D.Float(size / 2f - moonRadius - moonRadius * 0.2f, size * 0.70f, moonRadius * 2, moonRadius * 2, 0f, 360.0f, Arc2D.CHORD);
            Area lunaArea = new Area(lunaArc);
            lunaArea.subtract(new Area(new Arc2D.Float(size / 2f - moonRadius - moonRadius * 0.7f - moonRadius * 0.2f, size * 0.70f, moonRadius * 2, moonRadius * 2, 0f, 360.0f, Arc2D.CHORD)));
            g2d.fill(lunaArea);

            g2d.dispose();
            g2d = null;

            //creando abanico del reloj
            //--lado derecho
            //----definiendo variables
            float fanRadius = (dnRadius);
            float border = fanRadius * 0.2f;
            float centerX = bounds.x + bounds.width / 2;
            float centerY = bounds.y + bounds.height / 2;
            int x1 = (int) (centerX + (fanRadius * Math.cos(Math.toRadians(10))) - (border * Math.cos(Math.toRadians(10))));
            int y1 = (int) (centerY - (fanRadius * Math.sin(Math.toRadians(10))) + (border * Math.sin(Math.toRadians(10)))) + 2;
            int xn = (int) (centerX + (fanRadius * Math.cos(Math.toRadians(10))));
            int yn = (int) (centerY - (fanRadius * Math.sin(Math.toRadians(10))));
            double angleFan = (border / fanRadius) + Math.toRadians(10);
            int x3 = (int) (centerX + (fanRadius * Math.cos(angleFan)));
            int y3 = (int) (centerY - (fanRadius * Math.sin(angleFan)));
            int x4 = (int) (centerX + (fanRadius * 1.1 * Math.cos(Math.toRadians(10))));
            int y4 = (int) (centerY - (fanRadius * 1.1 * Math.sin(Math.toRadians(10)))) + 2;
            //----definiendo formas
            //----definiendo arco fan
            Arc2D fanArc = new Arc2D.Float(centerX - fanRadius, centerY - fanRadius, size, size, 10, 180, Arc2D.CHORD);
            Arc2D del1 = new Arc2D.Float(centerX - fanRadius, centerY - fanRadius, fanRadius * 2, fanRadius * 2, 170, 180, Arc2D.CHORD);
            Area fanArea = new Area(fanArc);
            fanArea.subtract(new Area(del1));
            //----generando path
            GeneralPath gp = new GeneralPath();
            gp.moveTo(x1, y1);
            gp.lineTo(x4, y4);
            gp.lineTo(x3 + 10, y3);
            gp.lineTo(x3, y3);
            gp.quadTo(xn, yn, x1, y1);
            fanArea.subtract(new Area(gp));
            fanArea.subtract(new Area(new Arc2D.Float(centerX - fanRadius * 0.2f, centerY - fanRadius * 0.2f, fanRadius * 2 * 0.2f, fanRadius * 2 * 0.2f, 0, 360, Arc2D.CHORD)));

            //--lado izquierdo
            //----definiendo variables
            angleFan = (border / fanRadius) + Math.toRadians(10);
            x1 = (int) (centerX - (fanRadius * Math.cos(Math.toRadians(10))) + (border * Math.cos(Math.toRadians(10))));
            xn = (int) (centerX - (fanRadius * Math.cos(Math.toRadians(10))));
            x3 = (int) (centerX - (fanRadius * Math.cos(angleFan)));
            x4 = (int) (centerX - (fanRadius * 1.1 * Math.cos(Math.toRadians(10))));
            //----definiendo formas
            //----generando path
            gp = new GeneralPath();
            gp.moveTo(x1, y1);
            gp.lineTo(x4, y4);
            gp.lineTo(x3 - 10, y3);
            gp.lineTo(x3, y3);
            gp.quadTo(xn, yn, x1, y1);
            fanArea.subtract(new Area(gp));
            dnClip = fanArea;
        }

        //dibujar sol
        public void drawSun(Graphics2D g2d, int x, int y, int radius, int intRadius, int nPicos, int rotacion) {
            int sunRadius = (int) (intRadius * 0.9);
            g2d.setColor(Color.ORANGE);
            drawStar(g2d, x, y, radius, intRadius, nPicos, rotacion);
            g2d.setColor(Color.YELLOW);
            g2d.fillOval((int) (x - sunRadius), (int) (y - sunRadius), (int) (sunRadius * 2), (int) (sunRadius * 2));
        }

        //dibujar estrella
        public void drawStar(Graphics2D g2d, int x, int y, int radius, int intRadius, int nPicos, int rotacion) {
            int[] xPoints = new int[nPicos * 2];
            int[] yPoints = new int[xPoints.length];

            for (int n = 0; n < (nPicos * 2); n = n + 2) {
                double ang1 = (270 + rotacion + (360.0 / nPicos) * (n / 2)) % 360;
                double ang2 = (270 + rotacion + (360.0 / nPicos) * (n / 2) + ((360.0 / nPicos) / 2)) % 360;
                xPoints[n] = (int) (x + radius * Math.cos(Math.toRadians(ang1)));
                yPoints[n] = (int) (y + (radius * Math.sin(Math.toRadians(ang1))));
                xPoints[n + 1] = (int) (x + (intRadius * Math.cos(Math.toRadians(ang2))));
                yPoints[n + 1] = (int) (y + (intRadius * Math.sin(Math.toRadians(ang2))));
            }
            g2d.fillPolygon(xPoints, yPoints, nPicos * 2);

        }

    }

    //clase CalendarCover
    private class CalendarCover extends ClockComponent {

        private Rectangle bounds;
        private boolean dirty;
        private boolean secondListening;
        private int lastDay = -1;
        private int day_next;
        private int month_next;
        private Dimension caldateSize;
        private Dimension calMonthSize;

        private CalendarCover() {
            watchTimer.setMinuteListener(this);
            secondListening = false;
        }

        protected void setBounds(Rectangle bounds) {
            this.bounds = bounds;
            lastDay = dia;
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
            dirty = true;

        }

        @Override
        protected void update() {
            if (dia != lastDay) {
                dirty = true;
                lastDay = dia;
            }

            if (hora == 11 && min == 59 && ampm == 1) {
                startSecondListening();
                dirty = true;
            } else if (secondListening) {
                stopSecondListening();
            }
        }

        //actualizar los tamaños de fuente del calendario
        private void updateCalendarMetrics(Graphics2D g2d) {
            calDaySize = new Dimension();
            calMonthSize = new Dimension();

            Font saveFont = g2d.getFont();
            g2d.setFont(calendarFont[sizeIndicator]);
            FontMetrics fm = g2d.getFontMetrics();
            calDaySize.height = fm.getHeight();
            calMonthSize.height = fm.getHeight();
            for (int i = 1; i <= 31; i++) {
                String s = i < 10 ? "0" + i : String.valueOf(i);
                if (fm.stringWidth(s) > calDaySize.width) {
                    calDaySize.width = fm.stringWidth(s);
                }
            }
            for (int i = 0; i < months.length; i++) {
                String s = months[i];
                if (fm.stringWidth(s) > calMonthSize.width) {
                    calMonthSize.width = fm.stringWidth(s);
                }
            }
            g2d.setFont(saveFont);
        }

        @Override
        protected void paint(Graphics2D g2d) {
            System.out.println("Dibujando calendario");
            updateCalendarMetrics(g2d);

            //definir tamaños de fuente
            updateCalendarMetrics(g2d);

            //crear rectangulo fondo del calendario
            calendarFrame = new Rectangle(0, 0, calDaySize.width + calMonthSize.width + 8 * 3, calDaySize.height);
            calendarFrame.x = (bounds.x + bounds.width / 2 - calendarFrame.width / 2);
            calendarFrame.y = (bounds.height / 2 + (int) (bounds.height / 2 * 0.3f));

            RoundRectangle2D rr = new RoundRectangle2D.Float(calendarFrame.x, calendarFrame.y, calendarFrame.width, calendarFrame.height, (int) (calendarFrame.height / 2), (int) (calendarFrame.height / 2));
            g2d.setColor(backgroundCalColor);
            g2d.fill(rr);

            //redefiniendo figura roundRect y obteniendo font metrics
            rr.setRoundRect(calendarFrame.x + 2, calendarFrame.y + 2, calendarFrame.width - 4, calendarFrame.height - 4, ((calendarFrame.height - 4) / 2), ((calendarFrame.height - 4) / 2));

            //crear clip para fecha calendario
            Shape savedClip = g2d.getClip();
            g2d.setClip(new Area(rr));

            //dibujando strings dia y mes
            g2d.setColor(labelCalColor);
            g2d.setFont(calendarFont[sizeIndicator]);
            FontMetrics fm = g2d.getFontMetrics();

            //dia
            int x = calendarFrame.x + 8;
            int y = calendarFrame.y + fm.getAscent();
            int y2;

            if (hora == 11 && min == 59 && ampm == 1) {
                y2 = (int) (y + rr.getHeight() - rr.getHeight() / 60 * seg);
                y = y - (int) (rr.getHeight() / 60 * seg);
                g2d.drawString(diaNext < 10 ? "0" + diaNext : String.valueOf(diaNext), x, y2);
            }
            g2d.drawString(dia < 10 ? "0" + dia : String.valueOf(dia), x, y);

            //mes
            x = calendarFrame.x + 8 + calDaySize.width + 8;

            y = calendarFrame.y + fm.getAscent();
            if (hora == 11 && min == 59 && ampm == 1 && mes != mesNext) {
                y2 = (int) (y + rr.getHeight() - rr.getHeight() / 60 * seg);
                y = y - (int) (rr.getHeight() / 60 * seg);
                g2d.drawString(months[mesNext], x, y2);;
            }
            g2d.drawString(months[mes], x, y);

            g2d.setClip(savedClip);

        }

    }

    //clase ClockHands
    private class ClockHands {

        private Rectangle bounds;

        private ClockHands() {
        }

        private void setBounds(Rectangle bounds) {
            this.bounds = bounds;
        }

        private void paint(Graphics2D g2d) {
            
            int radius = bounds.width/2;
            int centerX = bounds.x + bounds.width/2;
            int centerY = bounds.y + bounds.height/2;
            
            
            //definiendo valor de x y y para manecillas
            int xSeg = (int) ((0.9 * radius) * cos(seg));
            int ySeg = (int) ((0.9 * radius) * sin(seg));
            int xMin = (int) ((0.7 * radius) * cos(min));
            int yMin = (int) ((0.7 * radius) * sin(min));
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

    }

    

    //OLD CODE *************************************************************************************

    //panelResized
    public void panelResized() {
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
        clockHands.setBounds(bounds);

    }

    //getBounds
    public Rectangle getBounds() {
        return new Rectangle(getWidth(), getHeight());
    }

    //get screen size
    private void getScreenSize(Rectangle frame) {
        //dibujar marca Javawatch
        if (frame.width < 400) {
            sizeIndicator = 0;
        } else if (frame.width < 800) {
            sizeIndicator = 1;
        } else {
            sizeIndicator = 2;
        }
    }

    //llenarArrays x y y
    public void fillX() {
        for (int i = 0; i < 60; i++) {
            xArr[i] = (float) (Math.cos(Math.toRadians((270 + i * (360 / 60)) % 360)));
        }
    }

    public void fillY() {
        for (int i = 0; i < 60; i++) {
            yArr[i] = (float) (Math.sin(Math.toRadians((270 + i * (360 / 60)) % 360)));
        }
    }

    //obtener seno y coseno
    public float cos(int min) {
        return xArr[min];
    }

    public float sin(int min) {
        return yArr[min];
    }

    //llenar arreglo de fuentes para cada tamaño de pantalla
    private void initFonts() {
        BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();

        calendarFont[0] = g2d.getFont().deriveFont(Font.BOLD, 28f);
        calendarFont[1] = g2d.getFont().deriveFont(Font.BOLD, 42f);
        calendarFont[2] = g2d.getFont().deriveFont(Font.BOLD, 84f);
        g2d.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (clockCover != null) {
            clockCover.paint(g2d);
        }
        if (clockHands != null) {
            clockHands.paint(g2d);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("JavaWatch1");
            frame.setMinimumSize(new Dimension(400, 400));
            JavaWatchOOP mainPanel = new JavaWatchOOP();
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    mainPanel.new WatchTimer().stopTimer();
                }
            });
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
