module com.example.librarymanagement_demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.librarymanagement_demo to javafx.fxml;
    exports com.example.librarymanagement_demo;
}