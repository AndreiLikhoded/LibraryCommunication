package kz.attractor.java.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.attractor.java.libraryCommunication.Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class ReadersService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Paths.get("./readers.json");

    public static List<Client> readFile(){
        String json = "";
        try{
            json = Files.readString(PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Client[] clients = GSON.fromJson(json, Client[].class);
        return new ArrayList<Client>(List.of(clients));
    }

    public static void writeFile(List<Client> library){
        String json = GSON.toJson(library);
        try{
            byte[] arr = json.getBytes();
            Files.write(PATH, arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNewEmployeeId(){
        return ReadersService.readFile().stream()
                .mapToInt(Client::getId)
                .max().orElseThrow(RuntimeException::new) + 1;
    }

}
