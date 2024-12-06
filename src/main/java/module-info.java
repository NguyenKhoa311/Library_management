module com.example.librarymanagement_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.jfoenix;
    requires jdk.compiler;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires org.testfx.junit5;
    requires org.testfx;

    opens com.uet.libraryManagement to javafx.fxml;
    exports com.uet.libraryManagement;
    exports com.uet.libraryManagement.Controllers;
    opens com.uet.libraryManagement.Controllers to javafx.fxml;
    exports com.uet.libraryManagement.APIService;
    opens com.uet.libraryManagement.APIService to javafx.fxml;
    exports com.uet.libraryManagement.Repositories;
    opens com.uet.libraryManagement.Repositories to javafx.fxml;
    exports com.uet.libraryManagement.Managers;
    opens com.uet.libraryManagement.Managers to javafx.fxml;
}