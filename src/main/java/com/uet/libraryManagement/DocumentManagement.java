package com.uet.libraryManagement;

import java.util.ArrayList;

public class DocumentManagement {
    // books list
    private final ArrayList<Document> documents = new ArrayList<>();

    public void addDoc(Document document) {
        documents.add(document);
    }

    public void removeDoc(Document document) {
        documents.remove(document);
    }

    public void findDoc(Document document) {

    }
}
