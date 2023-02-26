package kz.attractor.java.libraryCommunication;

import java.util.ArrayList;
import java.util.List;

public class BooksOnHand{
    private List<BookOnHand> books = new ArrayList<>();

    public List<BookOnHand> getBooks() {
        return books;
    }

    public void setBooks(List<BookOnHand> books) {
        this.books = books;
    }
}
