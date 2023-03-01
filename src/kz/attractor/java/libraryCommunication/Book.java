package kz.attractor.java.libraryCommunication;

import kz.attractor.java.service.BooksService;
import kz.attractor.java.service.ReadersService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private String name;

    private String author;

    private String img;

    private boolean busy = false;

    private Integer bookId;

   private String previousUser;

   private String time;

   private boolean onTheShelf;

    private transient Client client;


    public Book(String bookName, String author, String category) {
        this.bookId = BooksService.getNewBookId();
        this.name = name;
        this.author = author;
        this.img = "";
        this.previousUser = "";
        setTime();
        this.onTheShelf = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getPreviousUser() {
        return previousUser;
    }

    public void setPreviousUser(String previousUser) {
        this.previousUser = previousUser;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOnTheShelf() {
        return onTheShelf;
    }

    public void setOnTheShelf(boolean onTheShelf) {
        this.onTheShelf = onTheShelf;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setTime() {
        LocalDate time = LocalDate.now();
        this.time = String.valueOf(time);
    }


}
