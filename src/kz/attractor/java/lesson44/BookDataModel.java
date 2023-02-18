package kz.attractor.java.lesson44;


import kz.attractor.java.libraryCommunication.Book;
import kz.attractor.java.libraryCommunication.Client;
import kz.attractor.java.service.BooksService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDataModel {

    private List<Book> book;

    public BookDataModel() {
        this.book = BooksService.readFile();
    }

    public List<Book> getBooks() {
        return book;
    }

    public void setBook(List<Book> book) {
        this.book = book;
    }
}
