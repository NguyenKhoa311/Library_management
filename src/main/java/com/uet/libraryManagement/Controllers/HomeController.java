package com.uet.libraryManagement.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    public Label version_label;
    @FXML
    public Label appname_label;
    @FXML
    public Label authors_label;
    @FXML
    public Label time_label;

    private static String lasttime;

    private void Timenow() {

        // Định dạng thời gian để hiển thị dd:MM:yyyy HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss");

        // Tạo một Timeline để cập nhật label mỗi giây
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            String timenow = sdf.format(new Date());
            lasttime = timenow;
            // Sử dụng Platform.runLater để cập nhật label trên JavaFX Application Thread
            Platform.runLater(() -> time_label.setText(timenow));
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);  // Lặp vô hạn
        timeline.play();  // Bắt đầu timeline
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        time_label.setText(lasttime);
        Timenow();
    }
}
