package kz.attractor.java.libraryCommunication;

import kz.attractor.java.service.ReadersService;

import java.util.Arrays;
import java.util.List;

public class Book {
    private String name;

    private String author;

    private String img;

    private boolean busy = false;

    private Integer bookId;

    private transient Client client;


    public Book(String name, String author, String img) {
        this.name = name;
        this.author = author;
        this.img = img;

        bookStatus();
    }

    public void bookStatus() {
        if (busy) {
            List<Client> client = ReadersService.readFile();
            for (Client clients : client) {
                if (clients.getId().equals(bookId)) {
                    this.client = clients;
                }
            }
        }
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}
