package com.uet.libraryManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class ThesisRepository extends DocumentRepository {
    protected String db_table;
    protected final ConnectJDBC connectJDBC;

    public ThesisRepository() {
        this.connectJDBC = new ConnectJDBC();
        loadDatabase();
    }

    @Override
    protected void loadDatabase() {
        db_table = "theses";
    }
}
