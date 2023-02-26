package kz.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.lesson45.Lesson45Server;
import kz.attractor.java.libraryCommunication.Client;
import kz.attractor.java.libraryCommunication.Clients;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.CollationKey;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Lesson46Server extends Lesson45Server {

    private Clients clients = new Clients();
    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/lesson46", this::lesson46Handler);
    }


    private void lesson46Handler(HttpExchange exchange) {
        Cookie sessionCookie = Cookie.make("userId", "123");
        exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());

        Map<String, Object> data = new HashMap<>();
        String name = "times";

        Cookie c1 = Cookie.make("user%Id", "456");
        setCookie(exchange, c1);

        Cookie c2 = Cookie.make("user-mail", "456");
        setCookie(exchange, c2);

        Cookie c3 = Cookie.make("restricted()<>@,;;\\\"/[]?={}", "restricted()<>@,;;\\\"/[]?={}");
        setCookie(exchange, c3);


        String cookieString = getCookies(exchange);


        Map<String, String> cookies = Cookie.parse(cookieString);
        String cookieValue = cookies.getOrDefault(name, "0");
        int times = Integer.parseInt(cookieValue) + 1;

        Cookie<Integer> c4 = new Cookie<>(name, times);
        setCookie(exchange, c4);

        data.put(name, times);
        data.put("cookies", cookies);


        renderTemplate(exchange, "cookie.html", data);

    }
    private Client clientIdentification(HttpExchange exchange) {

        String cookieId = getCookieFromClient(exchange, "cookieId");
        String clientEmail = getCookieFromClient(exchange, "email");

        Client inentifiedClient = null;

        for (Client client : clients.getClients()) {
            if (client.getEmail().equals(clientEmail)) {
                if (client.getCookieId() != null && client.getCookieId().equals(cookieId)) {
                    inentifiedClient = client;
                }
                break;
            }
        }

        return inentifiedClient;
    }

    private String getCookieFromClient(HttpExchange exchange, String key) {

        String cookies = getCookies(exchange);

        String decode = URLDecoder.decode(cookies, UTF_8);

        String result = "";

        String[] split = decode.split(";");
        for (String s : split) {
            if (s.contains(key)) {
                result = s.split("=")[1].trim();
            }
        }

        return result;
    }


}