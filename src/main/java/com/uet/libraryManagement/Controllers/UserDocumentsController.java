package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Managers.SessionManager;
import javafx.fxml.FXML;

public class UserDocumentsController extends DocumentsController {
    @FXML
    private void takeDoc() {
        Document selectedDocument = docsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            showAlert("No document selected. Please select a document to borrow.");
            return;
        }

        if (selectedDocument.getQuantity() == 0) {
            showAlert("This document is currently out of stock.");
            return;
        }

        int userId = SessionManager.getInstance().getUser().getId();
        int docId = selectedDocument.getId();
        String docType = (selectedDocument instanceof Book) ? "book" : "thesis";

        // Check if the user has already borrowed the document
        if (BorrowRepository.getInstance().hasUserAlreadyBorrowedDocument(userId, docId, docType)) {
            showAlert("You have already borrowed this document.");
        } else {
            // borrow document
            BorrowRepository.getInstance().borrowDocument(userId, docId, docType);

            // Decrease the document quantity by 1
            selectedDocument.setQuantity(selectedDocument.getQuantity() - 1);

            // Update the quantity in the database
            BorrowRepository.getInstance().updateDocumentQuantity(docId, docType, selectedDocument.getQuantity());
            loadDocuments(docType.equals("book") ? "Books" : "Theses");
            showAlert("Document borrowed successfully.");
        }
    }
}
