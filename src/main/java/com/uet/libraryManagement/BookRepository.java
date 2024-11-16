package com.uet.libraryManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BookRepository extends DocumentRepository {
    protected String db_table;
    protected final ConnectJDBC connectJDBC;

    public BookRepository() {
        this.connectJDBC = new ConnectJDBC();
        loadDatabase();
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
