package io.github.annusshka.cubicsplinefxapplication;

import java.util.ArrayList;
import java.util.List;

public class CubicSpline {
    public List<Double> x;

    public List<Double> y;

    List<Double> h = new ArrayList<>();

    double[] c;

    int n;

    public CubicSpline(List<Double> x, List<Double> y) {
        this.x = x;
        this.y = y;
        n = setN();
        h = setH();
        c = setC();
    }

    public void setXY(List<Double> x, List<Double> y) throws Exception {
        if (x == null || y == null) {
            throw new Exception("List is null");
        }
        if (x.size() != y.size()) {
            throw new Exception("The lists aren't equals");
        }

        this.x = x;
        this.y = y;
    }

    public List<Double> getX() {
        return x;
    }

    public List<Double> getY() {
        return y;
    }

    private int setN() {
        return x.size();
    }

    private double[] setC() {
        return getC(getMatrixA(), getResultB());
    }

    private List<Double> setH() {
        for (int i = 0; i < n - 1; i++) {
            h.add(x.get(i + 1) - x.get(i));
        }

        return h;
    }

    private List<Double> getH() {
        return h;
    }

    public double func(double param) {
        if (Double.isNaN(param)) {
            return param;
        }
        if (param <= x.get(0)) {
            return y.get(0);
        }
        if (param >= x.get(n - 1)) {
            return y.get(n - 1);
        }

        int i = 0;
        for (; param >= x.get(i + 1); ++i) {
            if (param == x.get(i)) {
                return y.get(i);
            }
        }

        double t = (param - x.get(i)) / h.get(i);
        return y.get(i) * Math.pow(1 - t, 2) * (1 + 2 * t) + y.get(i + 1) * Math.pow(t, 2) * (3 - 2 * t) +
                c[i] * h.get(i) * t * Math.pow(1 - t, 2) - c[i + 1] * h.get(i) * Math.pow(t, 2) * (1 - t);
    }

    //MatrixA * x = resultB
    private double[][] getMatrixA() {
        double[][] result = new double[n][n];

        result[0][1] = 0;
        result[1][0] = 0;
        for (int i = 0; i < n; i++) {
            result[i][i] = 2.0;
            if (i != 0 && i != n - 1) {
                result[i][i + 1] = h.get(i - 1) / (h.get(i - 1) + h.get(i));
                result[i + 1][i] = h.get(i) / (h.get(i - 1) + h.get(i));
            }
        }

        return result;
    }

    private double[] getResultB() {
        double[] result = new double[n];
        result[0] = 0.0;

        for (int i = 0; i < n - 2; i++) {
            result[i + 1] = 3.0 * (((h.get(i) / (h.get(i) + h.get(i + 1))) * (y.get(i + 2) - y.get(i + 1)) / h.get(i + 1) +
                    (h.get(i + 1) / (h.get(i) + h.get(i + 1))) * (y.get(i + 1) - y.get(i)) / h.get(i)));
        }
        result[n - 1] = 0;

        return result;
    }

    private double[] getC(double[][] a, double[] b) {
        double[] c = new double[n];
        double[] u = new double[n];
        double[] v = new double[n];

        //Прямой ход прогонки
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                v[i] = 0;
                u[i] = 0;
            } else if (i == n - 1) {
                v[i] = 0;
                u[i] = (b[i] - a[i][i - 1] * u[i - 1]) / (a[i][i] + a[i][i - 1] * v[i - 1]);
            } else {
                v[i] = -1 * a[i][i + 1] / (a[i][i] + a[i][i - 1] * v[i - 1]);
                u[i] = (b[i] - a[i][i - 1] * u[i - 1]) / (a[i][i] + a[i][i - 1] * v[i - 1]);
            }
        }

        //Обратный ход прогонки
        c[n - 1] = u[n - 1];
        for (int i = n - 1; i > 0; i--) {
            c[i - 1] = v[i - 1] * c[i] + u[i - 1];
        }

        return c;
    }
}
