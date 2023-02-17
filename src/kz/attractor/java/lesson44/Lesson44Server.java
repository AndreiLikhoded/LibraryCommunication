package kz.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kz.attractor.java.server.BasicServer;
import kz.attractor.java.server.ContentType;
import kz.attractor.java.server.ResponseCodes;

import java.io.*;

public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);
        registerGet("/sample", this::freemarkerSampleHandler);

        registerGet("/test", this::testHandler);

        registerGet("/books", this::bookHandler);

        registerGet("/books/bookInfo", this::bookInfoHandler);

        registerGet("/client", this::clientInfoHandler);

    }

    private void clientInfoHandler(HttpExchange exchange) {
        renderTemplate(exchange, "client.ftlh", getClientInfoModel());
    }

    private Object getClientInfoModel() {
        return new ClientDataModel();
    }

    private void bookInfoHandler(HttpExchange httpExchange) {
        renderTemplate(httpExchange, "book.ftlh", getBookInfoModel());
    }

    private Object getBookInfoModel() {
        return new BookInfoModel();
    }


    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            // путь к каталогу в котором у нас хранятся шаблоны
            // это может быть совершенно другой путь, чем тот, откуда сервер берёт файлы
            // которые отправляет пользователю
            cfg.setDirectoryForTemplateLoading(new File("data"));

            // прочие стандартные настройки о них читать тут
            // https://freemarker.apache.org/docs/pgui_quickstart_createconfiguration.html
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void freemarkerSampleHandler(HttpExchange exchange) {
        renderTemplate(exchange, "sample.html", getSampleDataModel());
    }

    private void testHandler(HttpExchange exchange){
        renderTemplate(exchange, "test.html", new TestDataModel());
    }

    private void bookHandler(HttpExchange exchange){
        renderTemplate(exchange, "books.ftlh", new BookDataModel());
    }


    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
    try {
        // загружаем шаблон из файла по имени.
        // шаблон должен находится по пути, указанном в конфигурации
        Template temp = freemarker.getTemplate(templateFile);

        // freemarker записывает преобразованный шаблон в объект класса writer
        // а наш сервер отправляет клиенту массивы байт
        // по этому нам надо сделать "мост" между этими двумя системами

        // создаём поток который сохраняет всё, что в него будет записано в байтовый массив
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // создаём объект, который умеет писать в поток и который подходит для freemarker
        try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {

            // обрабатываем шаблон заполняя его данными из модели
            // и записываем результат в объект "записи"
            temp.process(dataModel, writer);
            writer.flush();

            // получаем байтовый поток
            var data = stream.toByteArray();

            // отправляем результат клиенту
            sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
        }
    } catch (IOException | TemplateException e) {
        e.printStackTrace();
    }
}

    private SampleDataModel getSampleDataModel() {
        // возвращаем экземпляр тестовой модели-данных
        // которую freemarker будет использовать для наполнения шаблона
        return new SampleDataModel();
    }

}
