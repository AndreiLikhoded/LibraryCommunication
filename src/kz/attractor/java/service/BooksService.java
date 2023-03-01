package kz.attractor.java.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.attractor.java.libraryCommunication.Book;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BooksService {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = Paths.get("./library.json");

    public static List<Book> readFile(){
        String json = "";
        try{
            json = Files.readString(PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Book[] users = GSON.fromJson(json, Book[].class);
        return new ArrayList<Book>(List.of(users));
    }

    public static void writeFile(List<Book> books){
        String json = GSON.toJson(books);
        try{
            byte[] arr = json.getBytes();
            Files.write(PATH, arr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNewBookId(){
        return BooksService.readFile().stream()
                .mapToInt(Book::getBookId)
                .max().orElseThrow(RuntimeException::new) + 1;
    }
}
