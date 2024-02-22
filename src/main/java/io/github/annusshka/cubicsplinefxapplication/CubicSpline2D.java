package io.github.annusshka.cubicsplinefxapplication;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.List;

public class CubicSpline2D {
    CubicSpline xs;

    CubicSpline ys;

    List<Double> params;

    public CubicSpline2D(List<Double> x, List<Double> y) {
        params = calculateParams(x, y);
        xs = new CubicSpline(params, x);
        ys = new CubicSpline(params, y);
    }

    public List<Double> getParams() {
        return params;
    }

    public Point2D points(double param) {
        return new Point2D(xs.func(param), ys.func(param));
    }

    private List<Double> calculateParams(List<Double> x, List<Double> y) {
        List<Double> params = new ArrayList<>();

        double currentParam = 0.0;
        params.add(currentParam);
        for (int i = 0; i < x.size() - 1; i++) {
            currentParam = currentParam + Math.hypot(x.get(i + 1) - x.get(i), y.get(i + 1) - y.get(i));
            params.add(currentParam);
        }

        return params;
    }
}
