module com.example.librarymanagement_demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires com.jfoenix;


    opens com.uet.libraryManagement to javafx.fxml;
    exports com.uet.libraryManagement;
    exports com.uet.libraryManagement.Controllers;
    opens com.uet.libraryManagement.Controllers to javafx.fxml;
    exports com.uet.libraryManagement.APIService;
    opens com.uet.libraryManagement.APIService to javafx.fxml;
}