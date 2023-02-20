package kz.attractor.java.lesson44;

import kz.attractor.java.libraryCommunication.Book;

public class BookInfoModel {
    private Book book;

    public BookInfoModel() {
        this.book = new Book("Idiot", "F.M.Dostoevskiy",
                "https://klike.net/uploads/posts/2021-02/1613966494_1.jpg", "Andrei", "Alexei");
    }

    public Book getBook() {
        return book;
    }
}
