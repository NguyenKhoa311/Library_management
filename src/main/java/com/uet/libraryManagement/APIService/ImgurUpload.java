//package com.uet.libraryManagement.APIService;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class ImgurUpload {
//    private static final String CLIENT_ID = "c8327df77b699e2";
//    private static final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/image";
//
//    public static String uploadImage(File imageFile) throws IOException {
//        URL url = new URL(IMGUR_UPLOAD_URL);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Authorization", "Client-ID " + CLIENT_ID);
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        connection.setDoOutput(true);
//
//        // Đọc file ảnh và encode base64
//        String base64Image = encodeFileToBase64(imageFile);
//        String data = "image=" + base64Image;
//
//        // Gửi dữ liệu
//        try (OutputStream os = connection.getOutputStream()) {
//            os.write(data.getBytes());
//            os.flush();
//        }
//
//        // Đọc phản hồi
//        int responseCode = connection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    response.append(line);
//                }
//                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
//                if (jsonObject.get("success").getAsBoolean()) {
//                    return jsonObject.getAsJsonObject("data").get("link").getAsString();
//                } else {
//                    throw new IOException("Imgur upload failed: " + jsonObject);
//                }
//            }
//        } else {
//            throw new IOException("HTTP Error: " + responseCode);
//        }
//    }
//
//    private static String encodeFileToBase64(File file) throws IOException {
//        try (FileInputStream fis = new FileInputStream(file);
//             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//            while ((bytesRead = fis.read(buffer)) != -1) {
//                out.write(buffer, 0, bytesRead);
//            }
//            return java.util.Base64.getEncoder().encodeToString(out.toByteArray());
//        }
//    }
//}
package com.uet.libraryManagement.APIService;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

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
