package com.uet.libraryManagement;

public class BookRepository extends DocumentRepository {
    protected String db_table;
    protected final ConnectJDBC connectJDBC;
    private static BookRepository instance;

    public BookRepository() {
        this.connectJDBC = new ConnectJDBC();
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
