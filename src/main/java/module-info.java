module com.example.bookstore {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.bookstore to javafx.fxml;
    exports com.example.bookstore;
    exports com.example.bookstore.model;
    opens com.example.bookstore.model to javafx.base, javafx.fxml;
}