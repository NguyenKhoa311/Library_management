package com.uet.libraryManagement.Managers;

import javafx.concurrent.Task;
import javafx.application.Platform;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {
    private static final ExecutorService executorService = Executors.newFixedThreadPool(8); // Tùy chỉnh số luồng theo nhu cầu

    public static <T> void runTask(Task<T> task, Runnable onSuccess, Runnable onFailure) {
        // Xử lý khi Task thành công
        task.setOnSucceeded(event -> {
            if (onSuccess != null) Platform.runLater(onSuccess);
        });

        // Xử lý khi Task thất bại
        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            if (onFailure != null) {
                Platform.runLater(() -> {
                    onFailure.run();
                });
            }
            if (ex != null) ex.printStackTrace(); // Log lỗi để debug
        });

        // Thực thi Task
        executorService.submit(task);
    }

    public static void shutdown() {
        executorService.shutdown();
    }
}
