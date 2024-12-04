package com.uet.libraryManagement.APIService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import com.google.gson.*;

public class BookAPI {
    private static final String API_KEY = "AIzaSyB5gHzt3vVKJHxU4R-g8MEMibYNtxtIRC4";

    public static List<Volume> searchVolumes(String query, String docType) {
        List<Volume> volumes = new ArrayList<>();

        try {
            if ("Thesis".equalsIgnoreCase(docType)) {
                query += " thesis";
            }

            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + API_KEY + "&maxResults=25";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                Gson gson = new GsonBuilder().create();
                VolumeResponse volumeResponse = gson.fromJson(content.toString(), VolumeResponse.class);

                if (volumeResponse != null && volumeResponse.items != null) {
                    volumes.addAll(volumeResponse.items);
                } else {
                    System.out.println("No volumes found for the query: " + query);
                }
                in.close();
            } else {
                System.out.println("Error: HTTP response code " + responseCode);
            }
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return volumes;
    }
}
