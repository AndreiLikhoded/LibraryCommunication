package kz.attractor.java.libraryCommunication;

public class BookOnHand {
    private String name;

    private String author;

    private String img;

    private Integer bookId;

    private String clientEmail;

    private boolean onHand;

    public BookOnHand(String name, String author, String img, Integer bookId, String clientEmail, boolean onHand) {
        this.name = name;
        this.author = author;
        this.img = img;
        this.bookId = bookId;
        this.clientEmail = clientEmail;
        this.onHand = onHand;
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

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public boolean isOnHand() {
        return onHand;
    }

    public void setOnHand(boolean onHand) {
        this.onHand = onHand;
    }
}
