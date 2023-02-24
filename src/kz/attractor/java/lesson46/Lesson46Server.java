package kz.attractor.java.lesson46;

import com.sun.net.httpserver.HttpExchange;
import kz.attractor.java.lesson45.Lesson45Server;

import java.io.IOException;
import java.text.CollationKey;
import java.util.HashMap;
import java.util.Map;

public class Lesson46Server extends Lesson45Server {
    public Lesson46Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/lesson46", this::lesson46Handler);
    }



    private void lesson46Handler(HttpExchange exchange) {
//        Cookie sessionCookie = Cookie.make("userId", "123");
//        exchange.getResponseHeaders()
//                .add("Set-Cookie", sessionCookie.toString());
//
//        Map<String, Object> data = new HashMap<>();
//        int times = 42;
//        data.put("times", times);
//
//        Cookie c1 = Cookie.make("user%Id", "456");
//        setCookie(exchange, c1);
//
//        Cookie c2 = Cookie.make("restricted()<>@,;;\\\"/[]?={}", "()<>@,;;\\\"/[]?={}");
//        setCookie(exchange, c2);
//
//        Cookie c3 = Cookie.make("user-mail", "example@mail.com");
//        setCookie(exchange, c3);
//
//        String cookieString = getCookies(exchange);
//            Map<String, String> cookies = Cookie.parse(cookieString);
//            data.put("cookies", cookies);

        Map<String, Object> data = new HashMap<>();
        String name = "times";

        String cookieStr = getCookies(exchange);
        Map<String, String> cookies = Cookie.parse(cookieStr);

        String cookieValue = cookies.getOrDefault(name, "0");
        int times = Integer.parseInt(cookieValue) + 1;

        Cookie response = new Cookie(name, times);
        setCookie(exchange, response);

        data.put(name, times);
        data.put("cookies", cookies);
        renderTemplate(exchange, "cookie.ftlh", data);

    }

}
