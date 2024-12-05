package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import com.uet.libraryManagement.Repositories.ThesisRepository;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

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
        // Circles will be updated after data is loaded
        resetCircle(allBooksCircle, "all-books-circle");
        resetCircle(remainingBooksCircle, "remaining-books-circle");
        resetCircle(issuedBooksCircle, "issued-books-circle");
        resetCircle(allStudentsCircle, "all-students-circle");
        resetCircle(holdingBooksCircle, "holding-books-circle");
    }

    private void resetCircle(Circle circle, String styleClass) {
        circle.getStyleClass().add(styleClass);
        double circumference = 2 * Math.PI * CIRCLE_RADIUS;
        circle.getStrokeDashArray().clear();
        circle.getStrokeDashArray().addAll(0.0, circumference);
        circle.setRotate(-90);
    }

    private void setupStatisticsChart() {
        // Load data asynchronously
        loadDashboardDataAsync();
    }

    private void loadDashboardDataAsync() {
        Task<DashboardData> dashboardTask = new Task<>() {
            @Override
            protected DashboardData call() throws Exception {
                int remainingNum = BookRepository.getInstance().getNumberOfDocuments() + ThesisRepository.getInstance().getNumberOfDocuments();
                int issuedNum = BorrowRepository.getInstance().getNumberOfDocBorrowed();
                int docNum = remainingNum + issuedNum;
                int userNum = UserRepository.getInstance().getNumberOfUsers();
                int numUserIssuing = UserRepository.getInstance().getNumberOfUsersIssuing();
                return new DashboardData(docNum, remainingNum, issuedNum, userNum, numUserIssuing);
            }
        };

        // Success handler
        Runnable onSuccess = () -> {
            DashboardData data = dashboardTask.getValue();
            updateUI(data);
            updateStatisticsChart(data);
        };

        // Failure handler
        Runnable onFailure = () -> {
            showAlert("Failed to load dashboard data. Please try again.");
        };

        // Run task with TaskManager
        TaskManager.runTask(dashboardTask, onSuccess, onFailure);
    }

    private void updateUI(DashboardData data) {
        // Update labels
        allDocsCount.setText(String.valueOf(data.allDocs()));
        remainingDocsCount.setText(String.valueOf(data.remainingDocs()));
        issuedDocsCount.setText(String.valueOf(data.issuedDocs()));
        allUsersCount.setText(String.valueOf(data.allUsers()));
        holdingDocsCount.setText(String.valueOf(data.holdingUsers()));

        // Update circles
        setupCircle(allBooksCircle, 100, "all-books-circle"); // Always 100% for total
        setupCircle(remainingBooksCircle, (double) (data.remainingDocs() * 100) / data.allDocs(), "remaining-books-circle");
        setupCircle(issuedBooksCircle, (double) (data.issuedDocs() * 100) / data.allDocs(), "issued-books-circle");
        setupCircle(allStudentsCircle, 100, "all-students-circle"); // Always 100% for total
        setupCircle(holdingBooksCircle, (double) (data.holdingUsers() * 100) / data.allUsers(), "holding-books-circle");
    }

    private void updateStatisticsChart(DashboardData data) {
        statisticsChart.setMaxHeight(250.0);
        statisticsChart.setPrefHeight(250.0);
        statisticsChart.setMaxWidth(670.0);
        statisticsChart.setPrefWidth(670.0);
        statisticsChart.getData().clear();
        Axis xAxis = statisticsChart.getXAxis();
        xAxis.setLabel("Categories");

        // If you want to explicitly set labels
        if (xAxis instanceof CategoryAxis) {
            CategoryAxis categoryAxis = (CategoryAxis) xAxis;
            categoryAxis.setCategories(FXCollections.observableArrayList(
                    "All Documents", "Remaining Documents", "Issued Documents",
                    "All Users", "Users issuing"
            ));
        }

        XYChart.Series<String, Number> bookSeries = new XYChart.Series<>();
        bookSeries.setName("Document Information");
        bookSeries.getData().add(new XYChart.Data<>("All Documents", data.allDocs()));
        bookSeries.getData().add(new XYChart.Data<>("Remaining Documents", data.remainingDocs()));
        bookSeries.getData().add(new XYChart.Data<>("Issued Documents", data.issuedDocs()));

        XYChart.Series<String, Number> studentSeries = new XYChart.Series<>();
        studentSeries.setName("User Information");
        studentSeries.getData().add(new XYChart.Data<>("All Users", data.allUsers()));
        studentSeries.getData().add(new XYChart.Data<>("Users issuing", data.holdingUsers()));

        statisticsChart.getData().addAll(bookSeries, studentSeries);
    }

    private void setupCircle(Circle circle, double percentage, String styleClass) {
        circle.getStyleClass().add(styleClass);

        double circumference = 2 * Math.PI * CIRCLE_RADIUS;
        double dashArray = (percentage * circumference) / 100;
        double gapArray = circumference - dashArray;

        ObservableList<Double> strokeDashArray = FXCollections.observableArrayList(dashArray, gapArray);
        circle.getStrokeDashArray().setAll(strokeDashArray);
        circle.setRotate(-90);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Data container for dashboard
        private record DashboardData(int allDocs, int remainingDocs, int issuedDocs, int allUsers, int holdingUsers) {
    }
}