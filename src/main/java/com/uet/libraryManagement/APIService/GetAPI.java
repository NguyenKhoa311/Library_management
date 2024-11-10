package com.uet.libraryManagement.APIService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.google.gson.*;

public class GetAPI {
    public static void main(String[] args) {
        String apiKey = "AIzaSyB5gHzt3vVKJHxU4R-g8MEMibYNtxtIRC4";
        String query = "intitle:Doraemon";

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + apiKey;
            // Create url to connect
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            // Checking connection
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorLine;
                while ((errorLine = errorReader.readLine()) != null) {
                    System.out.println("Error: " + errorLine);
                }
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            } else {
                // Get data from API
                System.out.println("Successfully retrieved the volumes from the Google API.");
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                // create GSON objecet and parse JSON from API
                Gson gson = new GsonBuilder().create();
                VolumeResponse volumeResponse = gson.fromJson(content.toString(), VolumeResponse.class);

                // Duyệt qua từng volume và in thông tin cần thiết
                // iterating through each volume to get information
                for (Volume volume : volumeResponse.items) {
                    VolumeInfo volumeInfo = volume.volumeInfo;
                    String title = volumeInfo.title != null ? volumeInfo.title : "N/A";
                    String authors = volumeInfo.authors != null ? String.join(", ", volumeInfo.authors) : "N/A";
                    String publishedDate = volumeInfo.publishedDate != null ? volumeInfo.publishedDate : "N/A";
                    String publisher = volumeInfo.publisher != null ? volumeInfo.publisher : "N/A";
                    String description = volumeInfo.description != null ? volumeInfo.description : "N/A";
                    String categories = volumeInfo.categories != null ? String.join(", ", volumeInfo.categories) : "N/A";

                    // Print out result sets
                    System.out.println("Title: " + title);
                    System.out.println("Authors: " + authors);
                    System.out.println("Published Date: " + publishedDate);
                    System.out.println("Publisher: " + publisher);
                    System.out.println("Description: " + description);
                    System.out.println("Categories: " + categories);
                    System.out.println();
                }

                // Stop connecting
                in.close();
                conn.disconnect();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
