package com.uet.libraryManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ThesisRepository extends DocumentRepository {
    protected String db_table;
    protected final ConnectJDBC connectJDBC;
    private static ThesisRepository instance;

    public ThesisRepository() {
        this.connectJDBC = new ConnectJDBC();
        loadDatabase();
    }

    public static synchronized ThesisRepository getInstance() {
        if (instance == null) {
            instance = new ThesisRepository();
        }
        return instance;
    }

    @Override
    protected void loadDatabase() {
        db_table = "theses";
    }

    @Override
    public String getDbTable() {
        return db_table;
    }
}
