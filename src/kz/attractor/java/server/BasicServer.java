package kz.attractor.java.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class BasicServer {

    private final HttpServer server;
    // путь к каталогу с файлами, которые будет отдавать сервер по запросам клиентов
    private final String dataDir = "data";
    private Map<String, RouteHandler> routes = new HashMap<>();

    protected BasicServer(String host, int port) throws IOException {
        server = createServer(host, port);
        registerCommonHandlers();
    }

    private static String makeKey(String method, String route) {
        return String.format("%s %s", method.toUpperCase(), route);
    }

    private static String makeKey(HttpExchange exchange) {
        var method = exchange.getRequestMethod();
        var path = exchange.getRequestURI().getPath();

        var index = path.lastIndexOf(".");
        var extOrPath = index != -1 ? path.substring(index).toLowerCase() : path;

        return makeKey(method, extOrPath);
    }

    protected static String getContentType(HttpExchange exchange){
        return exchange.getRequestHeaders()
                .getOrDefault("Content-Type", List.of(""))
                .get(0);
    }

    private static void setContentType(HttpExchange exchange, ContentType type) {
        exchange.getResponseHeaders().set("Content-Type", String.valueOf(type));
    }

    protected String getBody(HttpExchange exchange){
        InputStream input = exchange.getRequestBody();
        InputStreamReader isr = new InputStreamReader(input, StandardCharsets.UTF_8);

        try(BufferedReader reader = new BufferedReader(isr)){
            return reader.lines().collect(Collectors.joining(""));
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }

    protected void redirect303(HttpExchange exchange, String path){
        try{
            exchange.getResponseHeaders().add("Location", path);
            exchange.sendResponseHeaders(303, 0);
            exchange.getResponseBody().close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    protected void redirect404(HttpExchange exchange, String path){
//        try{
//            exchange.getResponseHeaders().add("Location", path);
//            exchange.sendResponseHeaders(303, 0);
//            exchange.getResponseBody().close();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }

    private static HttpServer createServer(String host, int port) throws IOException {
        var msg = "Starting server on http://%s:%s/%n";
        System.out.printf(msg, host, port);
        var address = new InetSocketAddress(host, port);
        return HttpServer.create(address, 50);
    }

    private void registerCommonHandlers() {
        // самый основной обработчик, который будет определять
        // какие обработчики вызывать в дальнейшем
        server.createContext("/", this::handleIncomingServerRequests);


        // специфичные обработчики, которые выполняют свои действия
        // в зависимости от типа запроса


        // обработчик для корневого запроса
        // именно этот обработчик отвечает что отображать,
        // когда пользователь запрашивает localhost:9889
        registerGet("/", exchange -> sendFile(exchange, makeFilePath("index.html"), ContentType.TEXT_HTML));

        // эти обрабатывают запросы с указанными расширениями
        registerFileHandler(".css", ContentType.TEXT_CSS);
        registerFileHandler(".html", ContentType.TEXT_HTML);
        registerFileHandler(".jpg", ContentType.IMAGE_JPEG);
        registerFileHandler(".png", ContentType.IMAGE_PNG);

    }

    protected final void registerGet(String route, RouteHandler handler) {
        getRoutes().put("GET " + route, handler);
    }
    protected final void registerPost(String route, RouteHandler handler){
        getRoutes().put("POST " + route, handler);
    }

    protected final void registerFileHandler(String fileExt, ContentType type) {
        registerGet(fileExt, exchange -> sendFile(exchange, makeFilePath(exchange), type));
    }

    protected final Map<String, RouteHandler> getRoutes() {
        return routes;
    }

    protected final void sendFile(HttpExchange exchange, Path pathToFile, ContentType contentType) {
        try {
            if (Files.notExists(pathToFile)) {
                respond404(exchange);
                return;
            }
            var data = Files.readAllBytes(pathToFile);
            sendByteData(exchange, ResponseCodes.OK, contentType, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path makeFilePath(HttpExchange exchange) {
        return makeFilePath(exchange.getRequestURI().getPath());
    }

    protected Path makeFilePath(String... s) {
        return Path.of(dataDir, s);
    }

    protected final void sendByteData(HttpExchange exchange, ResponseCodes responseCode,
                                      ContentType contentType, byte[] data) throws IOException {
        try (var output = exchange.getResponseBody()) {
            setContentType(exchange, contentType);
            exchange.sendResponseHeaders(responseCode.getCode(), 0);
            output.write(data);
            output.flush();
        }
    }

    private void respond404(HttpExchange exchange) {
        try {
            var data = ("<img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAASsAAACoCAMAAACPKThEAAABOFBMVEXYTH7///" +
                    "gpG0b///////v///3aTX/XRXrWQHfYP3cUADnXeJmTHE/BzNEQADfXSHzxv8rSrb3cRnuvsLqttr6biZvVN3PSwszUqLrgT4EAADPdXYkAADAm" +
                    "F0TV290eDEAaGEMeCj8jE0KEf4ykpbAXADuFnqfokKromrELFkEAAC3JR3jQzc4eGUTkg6H54eTutMOnPWu4OGnz8OzMWYSweZPCZ4rib5XdWYf209" +
                    "nrp7n77OvnjajzytL43ODhd5lPI046HkmVN2UAABgAACbr6OVVTGWAeolyLVmiJ1mMlaHqoba8trtkYnWWjpqXKV5QRF9gKFN9boM2Mk/TkKi5b44jJkmUe" +
                    "Yy/NGnn3OA0JkyNVXHTusbMxsmOAEODM15BAEAAABWTaYCQRmixP2+5iJt4YHqjRWseQNpRAAAMH0lEQVR4nO2dC1fbRhaALc3oRcEi0CqVbMsP7AQbA6Zq14S33" +
                    "dpAMS6BdltIHLZddvf//4OdO5Ls0cNOs6ebtJr5zgkxsicn+s6dO3fG0iiXEwgEAoFAIBAIBALBH4WZepDwoW0yj5lTNT1+TLfUbnffMuYp0VVV1/7f/7M/HWYBIXRiRI" +
                    "4ZhVUPKQrCx10jtZFxRRpdcSdLO8QSfhVRYh0oGCuKQt5AHSutTRdJEubOlfUKSzFX1jGS0PFO9/QAY/IiKcs0SRsJb3LmyjhQpJgra1WR8I6lmaau7xJZV/FklrNOwJXCmSut" +
                    "q0iHUVfaDpKUK/93U/PIm/Exz+go+JA7V9CZcEGJuNI9Ce+G/U7fJG+uRvO7dorw4Y7CmyvSmdCmhSR8PNWhXRELR1MLBvS2iBMzB36PeHNFkhV+ZZkRVxZ0yVkgQepn1MGRXfD7kr" +
                    "h6yZEr7ZR0MJLCWVdmN9ojtSsSRcdMdodk9crSOHMFyQqdajnqKrShd4ibg5kEk/iUDmdlA2R+4pc3VyRZKR0SQRFXBikHlNPZwGfuk4SFpnEFyUohfjlzBcnKDxjWlUXUKPtMkQDJXemGB" +
                    "0iyon6pqyNeXEFlhQvUAevKhMlLJJVDJbUTHAG/tJ7gy5VOktVL3xCT2yG1Sx47qyGRNK0O6GBAg44rV6QWIMOZ/5pxpe1EUzkJJSga/FmyqYE26lfb5MeVvkn6WriWx7oi8TKr2gFwFawogN8T/z" +
                    "3qaocLV2aB9MDpqbKuYE5zku4K/IZzQ45cWYcYz9ZaiILVILenu6L5ivp9yXyOD1dWR2Eneayr1D5IKynwG2Y4flxBLa7s6GYAdWX4X0VoR/HcTsfBHRPmNhI2taCN7rt6z/cXGYDUm3i30O0WAoiE433y" +
                    "dzeY0niRRVJSX6F90+wSo51C2Gb/CqbU8KqbbVkQKrCcDiCE6MIogq8iiBIValGV+bAGS8wWndyEjaANXRglf6PDtPX47EDmv5gFXAFKzp/jICZWzEKQwIxVJa2RspttVzm9s8oCXbIDaMHcmZkUQwLDHR2kRdp" +
                    "AJbEKbTK/4KezwLpox4JX8M4Bno2K8Dsx6U+dzUgbWEM+NcirrKuKoiE/cijxCSH0yZSUROeDp9lO62mYrKugRgijRZuzri5c0d9OEVONkooBp410PLqihSW4MqZFpXVMNBxQPyZ8rYoSFRTUouCqq2W9EmVR90kNCot+" +
                    "q6S8DA+SyYykrGqWYZlEG9qMf+2sQt164Nei+zle0DsKlJdQKikYdYK0ZJq7xJ50cnwC9dNm/EIZmBcqfi1KGmW8bJ+hHyO/DKeErnKm0cFUooJOusmLGTDbqMCLK5NMCqezwgLzhq4fdY6PV68KVlKF2Z3TKOOYLJF3NF03dG3" +
                    "uxZHpjQQCgUAgEAgEAoFAIBAIBAKBQCDgEfX9HxEAqm3n7ByvGy18EGruUW6eGZa2X1AtSxfG5qN+tV2RZWf1kF6kt9s5NYSuedhn2zJxhSQKxuiwkzNytm1/6v/YnxB1z5Hl2jBwRX0pHa3ffBSyEhgv68WSM8ISg+Jd55t7YnCMAfvB" +
                    "jKpDRYqCxk5PBFaUtZeIXnItxUHjJRFXEewfRklNgayvxXDIYveddab3KQqa/Yazfm3/h2HvNRt1xs54fFNnAoubK/V+B+pWU65uTIsFdP583P52HLhDCB3wdXX/ItQcqUFr16ErPGjVipNW0CfxsDU+Tlz1yGAu5KOdxEfCfk0K9tJDmNuVekO" +
                    "Wl1oD+juerN9/++WCuDL3P1vA12sf7zQ+Bna/SeJKbnnTLlil9Tu9dUsZ3dbvfpzvyiy4Kwtwf8pU91W3KqBKzk+CwFLGZK5TbKPRaNIeSHXn7u8LdhLQjtyhk59Da8P9+ZcsFWe0BxJmyR3fftc6RwNn1L6/v580GhsLrsLWvnQfStC8TH5WIy9IR667" +
                    "b77KkCt1r+mfWOlhWjSgQVvBo2LeabUcudx4m75JJsV46zrU9Be1WmNcJqo25NrS2JeVX3H7GXJFx0Afpz2t3Emiwu713YNckh/uvEX3Thrt9TwNoVF7yRuRQaGxfl8cTJbov1hzV5Yz5Mp+rISuyrfMcgy93xmhBwUhKSzbVTvlvF0YNQnftfG46L9A9aIfqTfu" +
                    "+rvsuIIqdEpQJjC6Btco3DkGPnu2FW9vnrrnZdrdbrF3DtYa1563kfflX7tfZMhVmNjTAovkrQ3SLQ+n2UpNLvtpP7rDGh0Y8M0dIi/L58qwplzDMbkxdn97t5UVV+oTE1akUJhEVq+wN0QSmt0+afcubJUy/Qf0YBisDR/K5eFdSS4N76q1mxs6NC6tu/9cToTiX5W1fz" +
                    "iOs9SoBa5KZY/thWg48rdMC7Avzvb2np62tnLTNXjj+YqfxsuNarVB/tAf5ZKvfmWlnxlX2pHnDdqj8U0rKIhqd+xin3J747Gb7pBxoFJpAr2LoGvprj8M1oYblHNgww8rueGuvMuMK8PzN6xAg/Oif3rVO2/aDfHYQ7tsaaXuhWNmpXJBI8vcd7+nw2B53AYmnoc9abBR9v2RYXA" +
                    "5I99kw85CYX0wyfuyatUJCkNrgE4iG9uT7Fah9C72+vSI+Zl7W/Yl00nNd15t5LSvg255TobB5U9zan84sIfONDe1i0HOKg7bCEFXTGzUruYe+5SLXtOPKzIMBv0t6MP41/tfvbugC37v/racEVewhyojq74UnrDzMB4NPOnwNNIBbXuvb4fknuhB/ZlbZlSVbrxvx1XJr67k/OdkGFzORh" +
                    "f0t1yfZXJ5GiGlap6Mj78w/Y+U7P1ec1YrBWUDGQbzrCtZGT4MQumN9spyRlzBdkPRwrPK1loy862gam89NivbF4nvCTV3vcG2KZOifewE4h7cz99lxBVsoROJq3HktLdfh2ZU++l1k5T3zUQJbhbcaCO57DjhATIM1t8tX2bClRGb/KHzcoorSFNnYEpOC6vP3GtnaQ7OuftzRlzNCoYALx+JELn" +
                    "5ZBNTucftoKiqQCs9uvXTj+76fFbcN8vZcAWbD0W64Ggp6kqubNlbF5Vp+dknYVV49mWE5+6Mlc8T/JANV7AxYbQL+msDYbKBTndG01Qk1T+fz9sv4vx8eXmZhSUZ2MA+AnZYVX7pzazXNP2TNuajryWILEn8dbG8mKo266oxahfZelyucHzBGt00NJKu6uzg3xij+zwja/uMX1WJ4kpCt2zFUL5GyqA2S2" +
                    "DbGVkt+J+w4hdboZtIah8iCQ/k8FCyCuUI2IEvlq9KbH6iFzdg78GXBZUWvyRGQcljU7ss56k/XxbfqiIrV35YDaKuWnThHXtLpe0K36riSwzEyn3UVdGlgadMij2ec1UuZS4YK69mF82gb9b4VpUzjuPpKu6qUQ+Sf8b3F38/iXSVcFWq3vuhx9dT8pKY+/EumMjtcskJZHEeWPQJEotrBpDVpp9CPDxuYz7J6oo" +
                    "EllxKlzV7UAmXGCcpd90Ma3JcVrENCT7xHFmuMBKpHb6ZiC+L0sgisjh6pmAKZtpdSt5DOSkLIot5cCp/mIX4xJkmLO+ukSJrguDxLtxCH5KXIgtfx8dCX1bkmZackZzhhPm97lQTsloTxMlDBdOAZw7PkeWdO4mOWJysLrp1KdtoB/NcSRgNNpx8NVpqOf/K2A1IH8ACV2ALT84forVWfo/bFayFrqiu6OK7LDcf0+4C4AH" +
                    "tak5un8oaxQfECq8rfmlT5wjoOlmWcvpVTuJShoSrWBcEZpdj8YW2OF/Fvlfl21XaOkMkX7VbCVeVPpd9cH7hPg2scSu+msVpvsqZ1iFeDBrV8v6VjHCLTZV0SU4v/jALbtt9L8GN8PXvgSq3+zoV3j579uztynPysx7yLJ23dXo7UpnfbZ10XTe+fvGNYehOA8g7T2t6GsbfXlw3yuUat2FFUYmrteBWy+bZ1pywMYkrqLU4zVYBvq" +
                    "t+hVRO2/25sz3i6t81fgfBAOrKvtjebl7k5gcNcfVTTa4k7wLIPircdBRAXNn2mfyYs211LjZx1a/0bOYzn/ocPhLq1tZXAZeX/3nx2+VlpXfWO1tewJsXb5YrvV7v9Q9wrTolKzflLkY1p7IuGRa5Isw+6Kviw1WkD/5OEh30U5+DQCAQCAQCgUAgEAgEAoFAIBAIBAKBQJBB/gu24YmD/V8rTAAAAABJRU5ErkJggg==\" alt=\"not found\">").getBytes();
            sendByteData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_HTML, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleIncomingServerRequests(HttpExchange exchange) {
        var route = getRoutes().getOrDefault(makeKey(exchange), this::respond404);
        route.handle(exchange);
    }

    public final void start() {
        server.start();
    }
}
