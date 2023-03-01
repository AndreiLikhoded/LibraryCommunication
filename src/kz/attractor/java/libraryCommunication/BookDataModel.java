package kz.attractor.java.libraryCommunication;


import kz.attractor.java.libraryCommunication.Book;
import kz.attractor.java.libraryCommunication.Client;
import kz.attractor.java.service.BooksService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookDataModel {

    private List<Book> books = new ArrayList<>();
    private Book book;

    public BookDataModel() {
        this.books = BooksService.readFile();
    }

    public BookDataModel(int id) {
        this.books = BooksService.readFile();

        for (Book b:books) {
            if (b.getBookId() == id){
                this.book = b;
                return;
            }
        }
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
