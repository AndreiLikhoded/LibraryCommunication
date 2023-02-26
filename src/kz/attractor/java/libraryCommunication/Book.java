package kz.attractor.java.libraryCommunication;

import kz.attractor.java.service.ReadersService;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String name;

    private String author;

    private String img;

    private boolean busy = false;

    private Integer bookId;

    private String clientEmail;

    private String clientName1;
    private String clientName2;

    private transient Client client;

    private List<String> dataBook = new ArrayList<>();


    public Book(String name, String author, String img, String clientName1, String clientName2, String clientEmail) {
        this.name = name;
        this.author = author;
        this.img = img;
        this.clientName1 = clientName1;
        this.clientName2 = clientName2;
        this.clientEmail = clientEmail;

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

    public String getClientName1() {
        return clientName1;
    }

    public void setClientName1(String clientName1) {
        this.clientName1 = clientName1;
    }

    public String getClientName2() {
        return clientName2;
    }

    public void setClientName2(String clientName2) {
        this.clientName2 = clientName2;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public List<String> getDataBook() {
        return dataBook;
    }

    public void setDataBook(List<String> dataBook) {
        this.dataBook = dataBook;
    }
}
