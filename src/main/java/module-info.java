module se233.advprogrammingproject1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;
    requires java.sql;
    requires jdk.unsupported.desktop;

    opens se233.advprogrammingproject1.controllers to javafx.fxml;

    exports se233.advprogrammingproject1;
    exports se233.advprogrammingproject1.controllers;
    exports se233.advprogrammingproject1.cropping;
    exports se233.advprogrammingproject1.functions;
}