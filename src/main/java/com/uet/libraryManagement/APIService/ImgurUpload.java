package com.uet.libraryManagement.APIService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;


public class ImgurUpload {

    private static final String CLIENT_ID = "c8327df77b699e2";  // Thay thế với Client ID của bạn
    private static final String UPLOAD_URL = "https://api.imgur.com/3/upload";

    public static String uploadImage(File imageFile) throws IOException {
        HttpURLConnection connection = null;
        try {
            // Đọc file ảnh vào byte array
            Path path = imageFile.toPath();
            byte[] imageBytes = Files.readAllBytes(path);

            // Mở kết nối với API Imgur
            URL url = new URL(UPLOAD_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=----boundary");

            // Dữ liệu form
            String boundary = "----boundary";
            String LINE_FEED = "\r\n";

            String boundaryStart = "--" + boundary;
            String boundaryEnd = "--" + boundary + "--";

            String formData = boundaryStart + LINE_FEED +
                    "Content-Disposition: form-data; name=\"image\"; filename=\"" + imageFile.getName() + "\"" + LINE_FEED +
                    "Content-Type: image/jpeg" + LINE_FEED + LINE_FEED;

            // Gửi yêu cầu POST
            connection.getOutputStream().write(formData.getBytes());
            connection.getOutputStream().write(imageBytes);
            connection.getOutputStream().write(LINE_FEED.getBytes());
            connection.getOutputStream().write(boundaryEnd.getBytes());

            // Đọc phản hồi từ Imgur
            InputStream responseStream = connection.getInputStream();
            StringBuilder response = new StringBuilder();
            int i;
            while ((i = responseStream.read()) != -1) {
                response.append((char) i);
            }

            // Phân tích JSON để lấy URL ảnh
            String responseString = response.toString();
            if (responseString.contains("link")) {
                String imgUrl = responseString.split("\"link\":\"")[1].split("\"")[0];
                return imgUrl;  // Trả về URL ảnh đã tải lên Imgur
            } else {
                throw new IOException("Failed to upload image to Imgur: " + responseString);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
