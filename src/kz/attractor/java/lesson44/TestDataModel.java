package kz.attractor.java.lesson44;

import java.time.LocalDate;

public class TestDataModel {
    private SampleDocument document;

    public TestDataModel() {
        this.document = new SampleDocument(1, LocalDate.now(), "test text");
    }


    public SampleDocument getDocument() {
        return document;
    }

    public void setDocument(SampleDocument document) {
        this.document = document;
    }
}
