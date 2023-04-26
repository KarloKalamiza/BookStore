package com.example.bookstore;

import com.example.bookstore.model.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Button buttonSave;
    @FXML
    private Button buttonLoad;
    @FXML
    private TableView<Book> tableViewBooks;
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
    private final File dataFile = new File("data.txt");
    private final Object lock = new Object(); // shared lock object
    private boolean isSaved = false; // shared flag
    ObservableList<Book> books = FXCollections.observableArrayList(
            new Book("0001", "The Bible", "Various authors", "SF", "The holy scripture of Christianity, the Bible is the world's best-selling book of all time."),
            new Book("0002", "Don Quixote", "Miguel de Cervantes", "historic", "A Spanish novel published in 1605, considered one of the greatest works of fiction ever written."),
            new Book("0003", "The Lord of the Rings", "J.R.R. Tolkien", "SF", "A fantasy novel published in 1954, considered a classic of the genre and one of the most influential books of the 20th century."),
            new Book("0004", "Harry Potter and the Philosopher's Stone", "J.K. Rowling", "SF", "The first novel in the Harry Potter series, which has become a cultural phenomenon and a beloved classic of children's literature."),
            new Book("0005", "To Kill a Mockingbird", "Harper Lee", "SF", "A Pulitzer Prize-winning novel published in 1960, which has become a classic of American literature and a touchstone for discussions of race and justice."),
            new Book("0006", "The Catcher in the Rye", "J.D. Salinger", "drama", " A controversial novel published in 1951, which has become a touchstone for discussions of teenage angst and disillusionment."),
            new Book("0007", "1984", "George Orwell", "drama", "A dystopian novel published in 1949, which has become a classic of political and social commentary."),
            new Book("0008", "Pride and Prejudice", "Jane Austen", "romantic", " A romantic novel published in 1813, which has become a classic of English literature and a touchstone for discussions of gender roles and social class."),
            new Book("0009", "The Diary of a Young Girl", "Anne Frank", "diary", "A diary written by a young Jewish girl during the Nazi occupation of the Netherlands, which has become a classic of Holocaust literature and a testament to the resilience of the human spirit.")
    );
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String isbn = "Isbn";
        String title = "Title";
        String author = "Author";
        String genre = "Genre";
        String desc = "Description";
        tableColumnISBN.setCellValueFactory(new PropertyValueFactory<>(isbn));
        tableColumnTITLE.setCellValueFactory(new PropertyValueFactory<>(title));
        tableColumnAUTHOR.setCellValueFactory(new PropertyValueFactory<>(author));
        tableColumnGENRE.setCellValueFactory(new PropertyValueFactory<>(genre));
        tableColumnDESCRIPTION.setCellValueFactory(new PropertyValueFactory<>(desc));

        tableViewBooks.setItems(books);
    }
    public void saveBooks(){
        new Thread(new SaveTask()).start();
    }
    public void loadBooks(){
        new Thread(new LoadTask()).start();
    }
    private void saveDataToFile() throws IOException {
        List<Book> data = tableViewBooks.getItems();
        try(PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
            for (Book book: data){
                writer.printf("%s,%s,%s,%s,%s\n", book.getIsbn(), book.getTitle(), book.getAuthor(),
                        book.getGenre(), book.getDescription());
            }

            tableViewBooks.getItems().clear();
        }
    }
    private static void showAlert(
            String title, String header, String content
    ) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void loadDataFromFile() throws IOException {
        List<Book> data = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                Book book = new Book(fields[0], fields[1], fields[2], fields[3], fields[4]);
                data.add(book);
            }
        }
        tableViewBooks.setItems(FXCollections.observableArrayList(data));
    }
    private class SaveTask extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            synchronized(lock) {
                saveDataToFile();
                isSaved = true;
                lock.notifyAll();
            }

            return null;
        }
    }
    private class LoadTask extends Task<Void>{
        @Override
        protected Void call() throws Exception {
            synchronized (lock) {
                while (!isSaved){
                    lock.wait();
                }
                loadDataFromFile();
            }
            return null;
        }
        @Override
        protected void scheduled() {
            synchronized (lock) {
                if (!isSaved){
                    showAlert("WARNING", "BOOKS CANNOT LOAD!", "LOADTASK CALLED BEFORE SAVETASK!");
                }
            }
        }
    }
}