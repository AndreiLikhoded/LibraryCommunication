package kz.attractor.java.lesson44;

import kz.attractor.java.libraryCommunication.Client;
import kz.attractor.java.service.ReadersService;

import java.util.ArrayList;
import java.util.List;

public class ClientDataModel {


    private List<Client> client;

    public ClientDataModel() {
        this.client = ReadersService.readFile();
    }

    public List<Client> getClient() {
        return client;
    }

    public void setClient(List<Client> client) {
        this.client = client;
    }
}
