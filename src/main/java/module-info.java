module io.github.annusshka.cubicsplinefxapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens io.github.annusshka.cubicsplinefxapplication to javafx.fxml;
    exports io.github.annusshka.cubicsplinefxapplication;
}