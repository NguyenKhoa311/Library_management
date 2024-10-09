package com.uet.libraryManagement;

public class Book extends Document {
    // attribute
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
