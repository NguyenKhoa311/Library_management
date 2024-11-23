package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import com.uet.libraryManagement.Repositories.ThesisRepository;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;

public class AdminDashboardController {
    @FXML
    private Label allDocsCount;
    @FXML
    private Label remainingDocsCount;
    @FXML
    private Label issuedDocsCount;
    @FXML
    private Label allUsersCount;
    @FXML
    private Label holdingDocsCount;

    @FXML
    private Circle allBooksCircle;
    @FXML
    private Circle remainingBooksCircle;
    @FXML
    private Circle issuedBooksCircle;
    @FXML
    private Circle allStudentsCircle;
    @FXML
    private Circle holdingBooksCircle;

    @FXML
    private BarChart<String, Number> statisticsChart;

    private static final double CIRCLE_RADIUS = 30.0;

    @FXML
    public void initialize() {
        setupStatisticsChart();
        setupCircleProgress();
    }

    private void setupCircleProgress() {
        // Get values
        int allBooks = Integer.parseInt(allDocsCount.getText());
        int remainingBooks = Integer.parseInt(remainingDocsCount.getText());
        int issuedBooks = Integer.parseInt(issuedDocsCount.getText());
        int allStudents = Integer.parseInt(allUsersCount.getText());
        int holdingStudents = Integer.parseInt(holdingDocsCount.getText());

        // Setup circles
        setupCircle(allBooksCircle, 100, "all-books-circle"); // Always 100% for total
        setupCircle(remainingBooksCircle, (remainingBooks * 100) / allBooks, "remaining-books-circle");
        setupCircle(issuedBooksCircle, (issuedBooks * 100) / allBooks, "issued-books-circle");
        setupCircle(allStudentsCircle, 100, "all-students-circle"); // Always 100% for total
        setupCircle(holdingBooksCircle, (holdingStudents * 100) / allStudents, "holding-books-circle");
    }

    private void setupCircle(Circle circle, double percentage, String styleClass) {
        circle.getStyleClass().add(styleClass);

        // Calculate the stroke dash array
        double circumference = 2 * Math.PI * CIRCLE_RADIUS;
        double dashArray = (percentage * circumference) / 100;
        double gapArray = circumference - dashArray;

        // Set the stroke properties
        circle.setStrokeLineCap(StrokeLineCap.ROUND);

        // Create observable list for dash array
        ObservableList<Double> strokeDashArray = FXCollections.observableArrayList(
                dashArray, gapArray
        );
        circle.getStrokeDashArray().setAll(strokeDashArray);

        // Set initial rotation to start from the top
        circle.setRotate(-90);
    }

    private void setupStatisticsChart() {
        // Thiết lập kích thước tối đa cho biểu đồ
        statisticsChart.setMaxHeight(250.0);
        statisticsChart.setPrefHeight(250.0);
        statisticsChart.setMaxWidth(670.0); // 700 - padding
        statisticsChart.setPrefWidth(670.0);
        XYChart.Series<String, Number> bookSeries = new XYChart.Series<>();
        bookSeries.setName("Document Information");
        int docNum = BookRepository.getInstance().getNumberOfDocuments() + ThesisRepository.getInstance().getNumberOfDocuments();
        int issuedNum = BorrowRepository.getInstance().getNumberOfDocBorrowed();
        int remainingNum = docNum - issuedNum;
        int userNum = UserRepository.getInstance().getNumberOfUsers();
        int numUserIssuing = UserRepository.getInstance().getNumberOfUsersIssuing();
        bookSeries.getData().add(new XYChart.Data<>("All Documents", docNum));
        bookSeries.getData().add(new XYChart.Data<>("Remaining Documents", remainingNum));
        bookSeries.getData().add(new XYChart.Data<>("Issued Documents", issuedNum));

        XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
        studentSeries.setName("User Information");

        studentSeries.getData().add(new XYChart.Data<>("All Users", userNum));
        studentSeries.getData().add(new XYChart.Data<>("Users issuing", numUserIssuing));

        statisticsChart.getData().addAll(bookSeries, studentSeries);
        loadDashboardData(
                String.valueOf(docNum),
                String.valueOf(remainingNum),
                String.valueOf(issuedNum),
                String.valueOf(userNum),
                String.valueOf(numUserIssuing)
        );
    }

    private void loadDashboardData(String allDocs, String remainingDocs, String issuedDocs, String allUsers, String issuingUsers) {
        allDocsCount.setText(allDocs);
        remainingDocsCount.setText(remainingDocs);
        issuedDocsCount.setText(issuedDocs);
        allUsersCount.setText(allUsers);
        holdingDocsCount.setText(issuingUsers);
    }
}