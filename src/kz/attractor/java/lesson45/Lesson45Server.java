package kz.attractor.java.lesson45;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.lesson44.Lesson44Server;
import kz.attractor.java.lesson44.ProfileDataModel;
import kz.attractor.java.lesson44.SuccessfulRegistrationDataModel;
import kz.attractor.java.libraryCommunication.Client;
import kz.attractor.java.server.ContentType;
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
        registerGet("/successfulRegistration", this::registrationHandler);
    }

    private void registrationHandler(HttpExchange exchange) {
        renderTemplate(exchange, "/successfulRegistration.html", getRegistration());
    }

    private Object getRegistration() {
        return new SuccessfulRegistrationDataModel();
    }


    private void registeringGet(HttpExchange exchange) {
        renderTemplate(exchange, "register.html", null);
    }

    private void registeringPost(HttpExchange exchange) {
        Map<String, Object> map = new HashMap<>();
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        try {
            if (parsed.size() == Client.class.getDeclaredFields().length - 8) {
                List<Client> clients = ReadersService.readFile();
                Client client = Client.createClient(clients.size() + 1, parsed);


                for (Client clientCheck : clients) {
                    if (Client.checkClientForExistence(client, clientCheck)) {
                        throw new RuntimeException("user already exists!");
                    }
                }
                clients.add(client);
                ReadersService.writeFile(clients);
                redirect303(exchange, "/successfulRegistration");
            } else {
                map.put("fail_text", "Please fill in all the fields!");
                renderTemplate(exchange, "register.html", map);
            }
        } catch (Exception e) {
            e.printStackTrace();

            map.put("fail_text", "Incorrect input!");
            renderTemplate(exchange, "register.html", map);
        }
    }


    private void loginGet(HttpExchange exchange) {
        Path path = makeFilePath("login.html");
        sendFile(exchange, path, ContentType.TEXT_HTML);
    }


    private void loginPost(HttpExchange exchange) {
        Map<String, Object> map = new HashMap<>();
        String raw = getBody(exchange);
        Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
        try {
            if (parsed.size() == 2) {
                List<Client> clients = ReadersService.readFile();
                for (Client checkClient : clients) {
                    if (checkClient.getEmail().equals(parsed.get("email")) && checkClient.getPassword().equals(parsed.get("password"))) {
                        throw new RuntimeException();
                    }
                }
                map.put("Correct", true);
                redirect303(exchange, "/profile");
            }else {
                map.put("fail-text", "Incorrect input");
                renderTemplate(exchange, "login.html", map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void profileHandler(HttpExchange exchange) {
        renderTemplate(exchange, "profile.ftlh", getProfileInfoHistory());
    }

    private Object getProfileInfoHistory() {
        return new ProfileDataModel();
    }

}
