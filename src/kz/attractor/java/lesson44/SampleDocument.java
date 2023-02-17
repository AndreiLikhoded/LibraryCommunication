package kz.attractor.java.lesson44;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SampleDocument {
    private Integer id;
    private LocalDate date;
    private String text;

    public SampleDocument(Integer id, LocalDate date, String text) {
        this.id = id;
        this.date = date;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
