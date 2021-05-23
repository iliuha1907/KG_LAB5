package com.cg;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class DrawUtils extends JPanel {
    private int numCellsX = 20;
    private int numCellsY;
    private int cellSize = 10;
    private List<List<Integer>> lines = new ArrayList<>();
    private List<List<Integer>> polygon= new ArrayList<>();
    private List<Integer> rect = new ArrayList<>();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawCoordinates(g);
        drawCoordinateRectangles(numCellsX, g);
        for (var line : lines) {
            drawLine(line.get(0), line.get(1), line.get(2), line.get(3), cellSize, g, Color.MAGENTA);

            if (!rect.isEmpty()){
                var l = LiangBarsky.calc(line, rect);
                if(!l.isEmpty()){
                    drawLine(l.get(0), l.get(1), l.get(2), l.get(3), cellSize, g, Color.cyan);
                }
            }
        }

        for (var line : polygon) {
            drawLine(line.get(0), line.get(1), line.get(2), line.get(3), cellSize, g, Color.PINK);

            if (!rect.isEmpty()){
                var l = LiangBarsky.calc(line, rect);
                if(!l.isEmpty()){
                    drawLine(l.get(0), l.get(1), l.get(2), l.get(3), cellSize, g, Color.RED);
                }
            }
        }

        if (!rect.isEmpty()){
            drawRectangles(rect, cellSize, g);
        }
    }

    private void drawCoordinates(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        Rectangle bounds = getBounds();
        Line2D yLine = new Line2D.Double((int)(bounds.width / 2), 0, (int)(bounds.width / 2), bounds.height);
        Line2D xLine = new Line2D.Double(0, (int)(bounds.height / 2), bounds.width, (int)(bounds.height / 2));
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.draw(xLine);
        g2.draw(yLine);
    }

    private void drawCoordinateRectangles(int n, Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        Rectangle bounds = getBounds();
        cellSize = (Math.min(bounds.width, bounds.height)) / n;
        int nX = bounds.width / cellSize;
        int nY = bounds.height / cellSize;
        numCellsY = nY * 2;
        g2.setStroke(new BasicStroke(1));
        for (int i = -nX / 2; i < nX / 2; i++){
            Line2D line = new Line2D.Double((int)(bounds.width / 2) + cellSize * i,0,
                    (int)(bounds.width / 2) + cellSize * i,bounds.height);
            g2.draw(line);
        }
        for (int i = -nY / 2; i < nY / 2; i++){
            Line2D line = new Line2D.Double(0,(int)(bounds.height / 2) + cellSize * i,bounds.width,
                    (int)(bounds.height / 2) +cellSize * i);
            g2.draw(line);
        }
    }

    private void drawLine(int x1, int y1, int x2, int y2, int cellSize, Graphics g, Color c){
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(6));
        Rectangle bounds = getBounds();
        g2.setColor(c);
        g2.drawLine((bounds.width / 2) + x1 * cellSize, (bounds.height / 2) - cellSize * y1,
                (bounds.width / 2) + x2 * cellSize, (bounds.height / 2) - cellSize * y2);
    }

    private void drawRectangles(List<Integer> rect, int cellSize, Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        g2.setStroke(new BasicStroke(5));
        Rectangle bounds = getBounds();
        g2.setColor(Color.yellow);

        int x1 = rect.get(0);
        int x2 = rect.get(2);
        int y1 = rect.get(1);
        int y2 = rect.get(3);

        g2.drawLine((bounds.width / 2) + x1 * cellSize, (bounds.height / 2) - cellSize * y1,
                (bounds.width / 2) + x2 * cellSize, (bounds.height / 2) - cellSize * y1);
        g2.drawLine((bounds.width / 2) + x1 * cellSize, (bounds.height / 2) - cellSize * y2,
                (bounds.width / 2) + x2 * cellSize, (bounds.height / 2) - cellSize * y2);
        g2.drawLine((bounds.width / 2) + x1 * cellSize, (bounds.height / 2) - cellSize * y1,
                (bounds.width / 2) + x1 * cellSize, (bounds.height / 2) - cellSize * y2);
        g2.drawLine((bounds.width / 2) + x2 * cellSize, (bounds.height / 2) - cellSize * y1,
                (bounds.width / 2) + x2 * cellSize, (bounds.height / 2) - cellSize * y2);
    }

    public void drawRectangle(List<Integer> rect){
        this.rect = rect;
        if(!rect.isEmpty()){
            Pair<Integer, Integer> deltas = getMaxDxDy(rect, false);
            if (numCellsX / 2 < deltas.getKey() || numCellsX / 2 + 6 > deltas.getKey())
                numCellsX = deltas.getKey() * 2 + 6;
            if (numCellsY / 2 < deltas.getValue())
                numCellsX = deltas.getValue() * 2 + 2;
        }
        repaint();
    }

    public void drawLines(List<List<Integer>> lines){
        this.lines = lines;
        Pair<Integer, Integer> deltas = getMaxDxDy(lines);
        if (numCellsX / 2 < deltas.getKey() || numCellsX / 2 + 6 > deltas.getKey())
            numCellsX = deltas.getKey() * 2 + 6;
        if (numCellsY / 2 < deltas.getValue())
            numCellsX = deltas.getValue() * 2 + 2;
        repaint();
    }

    public void drawPolygon(List<List<Integer>> polygon){
        this.polygon = polygon;
        Pair<Integer, Integer> deltas = getMaxDxDy(polygon);
        if (numCellsX / 2 < deltas.getKey() || numCellsX / 2 + 6 > deltas.getKey())
            numCellsX = deltas.getKey() * 2 + 6;
        if (numCellsY / 2 < deltas.getValue())
            numCellsX = deltas.getValue() * 2 + 2;
        repaint();
    }


    private Pair<Integer, Integer> getMaxDxDy(List<Integer> line, boolean w){
        int dx = 0;
        int dy = 0;

        if (Math.abs(line.get(0)) > dx)
            dx = Math.abs(line.get(0));
        if (Math.abs(line.get(2)) > dx)
            dx = Math.abs(line.get(2));
        if (Math.abs(line.get(1)) > dy)
            dy = Math.abs(line.get(1));
        if (Math.abs(line.get(3)) > dy)
            dy = Math.abs(line.get(3));

        return new Pair<>(dx, dy);
    }


    private Pair<Integer, Integer> getMaxDxDy(List<List<Integer>> lines){
        int dx = 0;
        int dy = 0;
        for (List<Integer> line : lines){
            if (Math.abs(line.get(0)) > dx)
                dx = Math.abs(line.get(0));
            if (Math.abs(line.get(2)) > dx)
                dx = Math.abs(line.get(2));
            if (Math.abs(line.get(1)) > dy)
                dy = Math.abs(line.get(1));
            if (Math.abs(line.get(3)) > dy)
                dy = Math.abs(line.get(3));
        }
        return new Pair<>(dx, dy);
    }
}
