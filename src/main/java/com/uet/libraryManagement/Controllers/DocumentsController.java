package com.uet.libraryManagement.Controllers;

import com.jfoenix.controls.JFXTreeView;
import com.sun.source.tree.Tree;
import com.uet.libraryManagement.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.net.URL;
import java.util.ResourceBundle;

public class DocumentsController implements Initializable {

    @FXML
    public Label genre;
    @FXML
    private TreeView<String> book_types_tree;

    public void selectItem() {

        TreeItem<String> item =  book_types_tree.getSelectionModel().getSelectedItem();
        if (item != null) {
            genre.setText(item.getValue());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> root = new TreeItem<>("Documents");

        TreeItem<String> branch_sach = new TreeItem<>("Sách");
        TreeItem<String> branch_bao = new TreeItem<>("Báo");
        TreeItem<String> branch_truyen = new TreeItem<>("Truyện");

        for (String type : Book.allGenres) {
            String genre = "Sách dạy " + type;
            TreeItem<String> item = new TreeItem<>(genre);
            branch_sach.getChildren().add(item);
        }
//        TreeItem<String> leaf_sach_LamGiau = new TreeItem<>("Sách dạy làm giàu");
//        TreeItem<String> leaf_sach_LamNguoi = new TreeItem<>("Sách dạy làm người");
//        TreeItem<String> leaf_sach_NauAn = new TreeItem<>("Sách dạy nấu ăn");

        TreeItem<String> leaf_bao_KinhTe = new TreeItem<>("Báo kinh tế");
        TreeItem<String> leaf_bao_VanHoa = new TreeItem<>("Báo văn hóa");
        TreeItem<String> leaf_bao_XaHoi = new TreeItem<>("Báo xã hội");

        branch_bao.getChildren().addAll(leaf_bao_KinhTe, leaf_bao_VanHoa, leaf_bao_XaHoi);

        TreeItem<String> leaf_truyen_tranh = new TreeItem<>("Truyện tranh");
        TreeItem<String> leaf_truyen_sech = new TreeItem<>("Truyện sếch");

        branch_truyen.getChildren().addAll(leaf_truyen_tranh,leaf_truyen_sech);

        root.getChildren().addAll(branch_sach, branch_bao, branch_truyen);

        //Co hien thi root hay khong.
        //book_types_tree.setShowRoot(false);
        book_types_tree.setRoot(root);
    }
}
