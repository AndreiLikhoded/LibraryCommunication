package kz.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.lesson44.Lesson44Server;
import kz.attractor.java.lesson44.ProfileDataModel;
import kz.attractor.java.libraryCommunication.Client;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.server.ResponseCodes;
import kz.attractor.java.service.BooksService;
import kz.attractor.java.service.ReadersService;
import kz.attractor.java.utils.Utils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson45Server extends Lesson44Server {

    public Lesson45Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/login", this::loginGet);
        registerPost("/login", this::loginPost);

        registerGet("/register", this::registeringGet);
        registerPost("/register", this::registeringPost);
        registerGet("/profile", this::profileHandler);
    }


    private void registeringGet(HttpExchange exchange) {
        renderTemplate(exchange, "register.html", null);
    }

    private void registeringPost(HttpExchange exchange) {
        Map<String, Object> map = new HashMap<>();
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        try {
            if (parsed.size() == 2) {
                List<Client> clients = ReadersService.readFile();
                for (Client client : clients) {
                    if (client.getEmail().equals(parsed.get("email")) && client.getPassword().equals(parsed.get("password"))) {
                        throw new RuntimeException();
                    }
                }
            }
            map.put("fail", true);
            renderTemplate(exchange, "clientHistory.ftlh", map);
        } catch (Exception e) {
            redirect303(exchange, "/profile");
        }

//        Map<String, Object> map = new HashMap<>();
//        String raw = getBody(exchange);
//        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
//        try {
//            if (parsed.size() == Client.class.getDeclaredFields().length - 1) {
//                List<Client> clients = ReadersService.readFile();
//                Client client = Client.createClient(clients.size() + 1, parsed);
//
//                for (Client clientCheck : clients) {
//                    if (Client.checkClientForExistence(client, clientCheck)) {
//                        throw new RuntimeException("user already exists!");
//                    }
//                }
//                clients.add(client);
//                ReadersService.writeFile(clients);
//                redirect303(exchange, "/login");
//            } else {
//                map.put("fail_text", "Please fill in all the fields!");
//                renderTemplate(exchange, "register.html", map);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//            map.put("fail_text", "Something went wrong");
//            renderTemplate(exchange, "register.html", map);
//        }
    }


    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("login.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }


    private void loginPost(HttpExchange exchange) {
//        String cType = getContentType(exchange);
//        String raw = getBody(exchange);
//
//        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
//
//        String data = String.format("<p>Raw data: <b>%s</b></p>" +
//                "<p>Content-type: <b>%s</b></p>" +
//                "<p>After processing: <b>%s</b></p>", raw, cType, parsed);
//        try{
//            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data.getBytes());
//        }catch (IOException e){
//            e.printStackTrace();
//        }




//        redirect303(exchange, "/client/clientHistory");

    }

    private void profileHandler(HttpExchange exchange) {
        renderTemplate(exchange, "profile.ftlh", getProfileInfoHistory());
    }

    private Object getProfileInfoHistory() {
        return new ProfileDataModel();
    }

}
