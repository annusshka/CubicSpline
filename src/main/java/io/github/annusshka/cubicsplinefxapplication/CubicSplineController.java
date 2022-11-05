package io.github.annusshka.cubicsplinefxapplication;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class CubicSplineController {
    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<Point2D>();

    ArrayList<Double> pointsX = new ArrayList<>();

    ArrayList<Double> pointsY = new ArrayList<>();

    List<Point2D> pointsP = new ArrayList<Point2D>();

    int index = -1;

    int clickCount = 0;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
                case SECONDARY -> handleSecondaryClick(canvas.getGraphicsContext2D(), event);
            }
        });
    }

    private void splineInterpolation(ArrayList<Double> x, ArrayList<Double> y, int num) {
        CubicSpline2D cubicSpline2D = new CubicSpline2D(x, y);
        List<Double> params = new ArrayList<>();

        params = cubicSpline2D.getParams(x, y);

        params = linspace(params.get(0), params.get(params.size() - 1), num);

        for (double param : params) {
            pointsP.add(cubicSpline2D.points(param));
        }
    }

    public static List<Double> linspace(double start, double stop, int n) {
        List<Double> result = new ArrayList<Double>();

        double step = (stop-start) / (n - 1);

        for(int i = 0; i < n - 1; i++) {
            result.add(start + (i * step));
        }
        result.add(stop);

        return result;
    }

    private void handleSecondaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        if (clickCount == 0) {
            for (Point2D point : points) {
                if (point.getX() >= clickPoint.getX() - 3 && point.getX() <= clickPoint.getX() + 3 &&
                        point.getY() >= clickPoint.getY() - 3 && point.getY() <= clickPoint.getY() + 3) {
                    clickCount++;
                    index = points.indexOf(point);
                }
            }
        } else if (clickCount == 1 && index >= 0) {
            points.set(index, clickPoint);
            pointsX.set(index, clickPoint.getX());
            pointsY.set(index, clickPoint.getY());
            drawSpline(graphicsContext);
            clickCount = 0;
            index = -1;
        }
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());

        points.add(clickPoint);
        pointsX.add(clickPoint.getX());
        pointsY.add(clickPoint.getY());

        drawSpline(graphicsContext);
    }

    private void drawSpline(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        final int POINT_RADIUS = 3;
        for (Point2D point : points) {
            graphicsContext.fillOval(point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        if (points.size() > 1 && pointsX.size() == pointsY.size()) {

            splineInterpolation(pointsX, pointsY, 500 + 20 * points.size());

            graphicsContext.beginPath();
            for (int i = 0; i < pointsP.size() - 1; i++) {
                graphicsContext.lineTo(pointsP.get(i + 1).getX(), pointsP.get(i + 1).getY());
            }
            graphicsContext.stroke();

            pointsP.clear();
        }
    }
}