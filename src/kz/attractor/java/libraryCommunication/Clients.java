package kz.attractor.java.libraryCommunication;

import java.util.ArrayList;
import java.util.List;

public class Clients{
    private List<Client> clients = new ArrayList<>();

    public Clients() {
        this.clients = clients;
    }

    public void ClientsList() {

       Client client1 = new Client("Andrei", "qwe@qwe", "123", 1, "bazanga", "img");
       Client client2 = new Client("Boris", "ewq@ewq", "123", 2, "britva", "img");

        clients.add(client1);
        clients.add(client2);

    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
