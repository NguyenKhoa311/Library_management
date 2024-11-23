package com.uet.libraryManagement.Repositories;

public class BookRepository extends DocumentRepository {
    protected String db_table;
    private static BookRepository instance;

    public BookRepository() {
        loadDatabase();
    }

    public static synchronized BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    @Override
    protected void loadDatabase() {
        db_table = "books";
    }

    @Override
    public String getDbTable() {
        return db_table;
    }
}
