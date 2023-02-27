package kz.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.lesson44.BookDataModel;
import kz.attractor.java.lesson45.Lesson45Server;
import kz.attractor.java.libraryCommunication.*;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.service.BooksService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.text.CollationKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Lesson46Server extends Lesson45Server {

    private final Clients clients = new Clients();

    private Book book;

    private Book book2;

    private Client client;

    private BookDataModel books = new BookDataModel();

    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/lesson46", this::lesson46Handler);
        loginGet("/booksOnHand", this::clientGetBook);
        loginGet("/returnBook", this::returnBook);
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
    private void booksHandler(HttpExchange exchange) {

        Client authorisedClient = clientIdentification(exchange);

        if (authorisedClient != null) {

            BooksOnHand books = new BooksOnHand();

            for (Book book : this.books.getBooks()) {
                boolean isHandle = book.getClientEmail() != null && book.getClientEmail().equals(authorisedClient.getEmail());
                BookOnHand bookOnHand = new BookOnHand(book.getName(), book.getAuthor(), book.getImg(), book.getBookId(), book.getClientEmail(), isHandle);
                books.getBooks().add(bookOnHand);
            }

            renderTemplate(exchange, "books.html", books);
        } else {
            Path path = makeFilePath("bookError.html");
            sendFile(exchange, path, ContentType.TEXT_HTML);
        }
    }

    private void clientGetBook(HttpExchange exchange) {

        Client authorisedClient = clientIdentification(exchange);
        if (authorisedClient != null) {
            List<Book> book = BooksService.readFile();
            for (Book books : book) {
                if (book2.getBookId().equals(client.getId())) {
                    this.book = books;
                }
            }
            renderTemplate(exchange, "books.html", books);
        } else {
            Path path = makeFilePath("bookError.html");
            sendFile(exchange, path, ContentType.TEXT_HTML);
        }
        if (authorisedClient != null && authorisedClient.getBookId().size() > 2) {
            Path path = makeFilePath("bookError.html");
            sendFile(exchange, path, ContentType.TEXT_HTML);
        }
        String params = exchange.getRequestURI().getQuery();
        String id = params.split("=")[1];

        client.setBooks();

        String message = null;
        if (book != null && book.getClientEmail() == null) {
            assert authorisedClient != null;
            book.setClientEmail(authorisedClient.getEmail());
            book.getDataBook().add(authorisedClient.getEmail());

            for (Client client1 : clients.getClients()) {
                if (client1.getEmail().equals(authorisedClient.getEmail())) {
                    client1.getBookId().add(book.getBookId());
                    break;
                }
            }
        } else {
            message = "The book is reserved";
        }
        renderTemplate(exchange, "booksOnHand.html", new Information(message));
    }

    private void returnBook(HttpExchange exchange) {

        Client authorisedClient = clientIdentification(exchange);

        if (authorisedClient == null) {
            Path path = makeFilePath("loginError.html");
            sendFile(exchange, path, ContentType.TEXT_HTML);
            return;
        }
        String params = exchange.getRequestURI().getQuery();

        String bookId = params.split("=")[1];

        Book book = null;

        for (Book book1 : books.getBooks()) {
            if (book1.getBookId() == Integer.parseInt(bookId)) {
                book = book1;
                book1.setClientEmail(null);
                break;
            }
        }

        for (Client client : clients.getClients()) {

            if (client.getEmail().equals(authorisedClient.getEmail())) {
                client.getBookId().remove((Object) Integer.parseInt(bookId));
                break;
            }
        }

        String message = "You return the book ";

        renderTemplate(exchange, "returnBook.html", new Information(message));
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

    private void logout(HttpExchange exchange) {
        for (Client client : clients.getClients()) {
            client.setCookieId(null);
        }
    }
}