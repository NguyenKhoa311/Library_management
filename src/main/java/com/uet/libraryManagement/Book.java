package com.uet.libraryManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book extends Document {
    // attribute

    public static List<String> allGenres = new ArrayList<>(Arrays.asList("làm giàu", "làm người", "nấu ăn"));
    private String genre;

    // constructors
    public Book() {

    }

    public Book(String title, String author, String publisher, String description, String year, String genre, String url, String isbn10, String isbn13) {
        super(title, author, publisher, description, year, url, isbn10, isbn13);
        this.genre = genre;
    }

   public Book(int id, String title, String author, String publisher, String description, String year, String genre, String url, String isbn10, String isbn13) {
        super(id, title, author, publisher, description, year, url, isbn10, isbn13);
        this.genre = genre;
   }

    // getters, setters
    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
