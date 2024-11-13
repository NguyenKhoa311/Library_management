package com.uet.libraryManagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Book extends Document {
    // attribute

    public static List<String> allGenres = new ArrayList<>(Arrays.asList("làm giàu", "làm người", "nấu ăn"));
    private String isbn;
    private String genre;

    // constructors
    public Book() {

    }

   public Book(String id, String title, String author, String publisher, String description, int year, String isbn, String genre) {
        super(id, title, author, publisher, description, year);
        this.isbn = isbn;
        this.genre = genre;
   }

    // getters, setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
