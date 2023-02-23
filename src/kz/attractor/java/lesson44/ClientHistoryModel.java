package kz.attractor.java.lesson44;

import kz.attractor.java.libraryCommunication.Client;

public class ClientHistoryModel {
    private Client client;

    public ClientHistoryModel() {
        this.client = new Client("Andrei", "qwe@qwe", "122", 1, "Bazzzanga", "https://images.unsplash.com" +
                "/photo-1518020382113-a7e8fc38eac9?" +
                "ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZnVubnklMjBwaWN0dXJlc3xlbnwwfHwwfHw%3D&w=1000&q=80");
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
