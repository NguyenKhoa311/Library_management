module com.example.librarymanagement_demo {
    requires javafx.fxml;
    requires com.google.gson;
    requires transitive com.jfoenix;
    requires javafx.controls;
    requires jdk.compiler;


    opens com.uet.libraryManagement to javafx.fxml;
    exports com.uet.libraryManagement;
    exports com.uet.libraryManagement.Controllers;
    opens com.uet.libraryManagement.Controllers to javafx.fxml;
    exports com.uet.libraryManagement.APIService;
    opens com.uet.libraryManagement.APIService to javafx.fxml;
}