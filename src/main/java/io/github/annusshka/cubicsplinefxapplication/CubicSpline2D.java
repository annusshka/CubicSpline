package io.github.annusshka.cubicsplinefxapplication;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class CubicSpline2D {
    CubicSpline xs;

    CubicSpline xy;

    List<Double> params;

    public CubicSpline2D(List<Double> x, List<Double> y) {
        params = calculateParams(x, y);
        xs = new CubicSpline(params, x);
        xy = new CubicSpline(params, y);
    }

    public List<Double> getParams(List<Double> x, List<Double> y) {
        return params;
    }

    public Point2D points(double param) {
        return new Point2D(xs.func(param), xy.func(param));
    }

    private List<Double> calculateParams(List<Double> x, List<Double> y) {
        List<Double> params = new ArrayList<>();
        params.add(0.0);

        double n = 0.0;
        for (int i = 0; i < x.size() - 1; i++) {
            n = n + Math.hypot(x.get(i + 1) - x.get(i), y.get(i + 1) - y.get(i));
            params.add(n);
        }

        return params;
    }
}
