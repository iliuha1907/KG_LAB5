package com.cg;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class MainWindow extends JFrame {

    private final DrawUtils drawingPanel = new DrawUtils();
    private static final List<List<Integer>> lines = new ArrayList<>();
    private static final List<List<Integer>> polygon = new ArrayList<>();
    private static final List<Integer> rectangle = new ArrayList<>();

    private final int WIDTH = 900;
    private final int HEIGHT = 680;

    private final int BUTTONS_X = 650;
    private final int BUTTONS_WIDTH = 200;

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setLayout(null);

        drawingPanel.setBounds(0, 0, WIDTH-300, HEIGHT);
        add(drawingPanel);

        addDrawLineButton();
        addDrawPolygonButton();
        addDrawRectangleButton();
        addClearButton();

        setResizable(false);
    }

    private void addDrawRectangleButton() {
        JButton drawRectangleButton = new JButton("Draw rectangle");
        drawRectangleButton.setBounds(BUTTONS_X, 100, BUTTONS_WIDTH, 40);
        drawRectangleButton.addActionListener(e -> {
            drawingPanel.drawRectangle(rectangle);
        });
        add(drawRectangleButton);
    }

    private void addDrawPolygonButton() {
        JButton drawPolygonButton = new JButton("Draw polygon");
        drawPolygonButton.setBounds(BUTTONS_X, 200, BUTTONS_WIDTH, 40);
        drawPolygonButton.addActionListener(e -> {
            drawingPanel.drawPolygon(polygon);
        });
        add(drawPolygonButton);
    }

    private void addDrawLineButton() {
        JButton drawLineButton = new JButton("Draw line");
        drawLineButton.setBounds(BUTTONS_X, 300, BUTTONS_WIDTH, 40);
        drawLineButton.addActionListener(e -> {
            drawingPanel.drawLines(lines);
        });
        add(drawLineButton);
    }

    private void addClearButton() {
        JButton clearButton = new JButton("Clear");
        clearButton.setBounds(BUTTONS_X, 400, BUTTONS_WIDTH, 40);
        clearButton.addActionListener(e -> {
            drawingPanel.drawLines(new ArrayList<>());
            drawingPanel.drawRectangle(new ArrayList<>());
            drawingPanel.drawPolygon(new ArrayList<>());
        });
        add(clearButton);
    }


    public static void main(String[] args) throws IOException {
        var lineIO = MainWindow.class.getClassLoader().getResourceAsStream("input_lines.txt");
        new BufferedReader(new InputStreamReader(lineIO)).lines()
                .map(e -> e.split(" "))
                .forEach(MainWindow::pointsAsLine);

        var rectIO = MainWindow.class.getClassLoader().getResourceAsStream("input_rect.txt");
        new BufferedReader(new InputStreamReader(rectIO)).lines()
                .map(e -> e.split(" "))
                .forEach(MainWindow::pointsAsRect);

        createPolygon();

        MainWindow mainWindow = new MainWindow();
        mainWindow.repaint();
    }

    private static void createPolygon() {
        polygon.add(new ArrayList<>());
        polygon.get(0).addAll(Arrays.asList(5, 2, 7, 6));
        polygon.add(new ArrayList<>());
        polygon.get(1).addAll(Arrays.asList(7, 6, 12, -6));
        polygon.add(new ArrayList<>());
        polygon.get(2).addAll(Arrays.asList(12, -6, -10, -7));
        polygon.add(new ArrayList<>());
        polygon.get(3).addAll(Arrays.asList(-10, -7, 5, 2));
    }

    private static void pointsAsLine(String[] line) {
        var ans = Arrays.stream(line).
                map(Integer::parseInt)
                .collect(Collectors.toList());
        lines.add(ans);
    }

    private static void pointsAsRect(String[] line) {
        var ans = Arrays.stream(line).
                map(Integer::parseInt)
                .collect(Collectors.toList());
        rectangle.addAll(ans);
    }
}
