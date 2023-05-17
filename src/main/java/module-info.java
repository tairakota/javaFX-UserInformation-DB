module com.example.javafxuserinformation_db {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javafxuserinformation_db to javafx.fxml;
    exports com.example.javafxuserinformation_db;
}