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

    private List<Book> book = new ArrayList<>();

    public BookDataModel() {
        book.addAll(List.of(
                new Book("Idiot", "F.M.Dostoevskiy", ".data/images/idiot.jpg"),
                new Book("Voina i mir", "L.V.Tolstoy", ".data/images/idiot.jpg")
                ));
    }

    public List<Book> getBooks() {
        return book;
    }

    //    private Map<String, List<Book>> bookList;
//
//
//    public BookDataModel() {
//        bookList = (Map<String, List<Book>>) BooksService.readFile();
//    }
//
//    public Map<String, List<Book>> getBookList() {
//        return bookList;
//    }
//
//    public void setBookList(Map<String, List<Book>> bookList) {
//        this.bookList = bookList;
//    }
}
