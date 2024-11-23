package com.uet.libraryManagement.Repositories;

public class ThesisRepository extends DocumentRepository {
    protected String db_table;
    private static ThesisRepository instance;

    public ThesisRepository() {
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
