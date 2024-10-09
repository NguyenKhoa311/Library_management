module com.example.librarymanagement_demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.uet.libraryManagement to javafx.fxml;
    exports com.uet.libraryManagement;
}