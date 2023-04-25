package com.example.bookstore;

import com.example.bookstore.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Button buttonSave;

    @FXML
    private Button buttonLoad;

    @FXML
    private TableView tableViewBooks;

    @FXML
    private TableColumn<Book, String> tableColumnISBN;

    @FXML
    private TableColumn<Book, String> tableColumnTITLE;

    @FXML
    private TableColumn<Book, String> tableColumnAUTHOR;

    @FXML
    private TableColumn<Book, String> tableColumnDESCRIPTION;

    @FXML
    private TableColumn<Book, String> tableColumnGENRE;

    // Create a TableView and its columns
    TableView<Book> tableView = new TableView<>();

    @FXML
    public void fillTable(){
        // Create a list of books
        List<Book> books = new ArrayList<>();
        books.add(new Book(
           "00001", "Book 1", "Author 1", "Genre 1", "Description 1"
        ));
        books.add(new Book(
                "00001", "Book 1", "Author 1", "Genre 1", "Description 1"
        ));
        books.add(new Book(
                "00001", "Book 1", "Author 1", "Genre 1", "Description 1"
        ));


    }
}