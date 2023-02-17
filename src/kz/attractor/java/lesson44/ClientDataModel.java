package kz.attractor.java.lesson44;

import kz.attractor.java.libraryCommunication.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientDataModel {
    private Client client;

    public ClientDataModel() {
       this.client = new Client(1, "Andrei", "Bazzzanga", "qwe@qwe", "123",
               "https://www.shutterstock.com/image-photo/skeptic-surprised-cat-thinking-dont-260nw-1905929728.jpg");

    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
