package kz.attractor.java;

import kz.attractor.java.lesson44.Lesson44Server;
import kz.attractor.java.lesson45.Lesson45Server;
import kz.attractor.java.lesson46.Lesson46Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            new Lesson46Server("localhost", 9889).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
