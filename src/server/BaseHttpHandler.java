package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import manager.taskManager.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class BaseHttpHandler {
    protected TaskManager manager;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    protected Gson gson;

    public BaseHttpHandler(TaskManager manager) {
        this.manager = manager;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }

    public void sendText(HttpExchange exchange, String text, int responseCode) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(text.getBytes(CHARSET));
        }
    }

    public void sendNotFound(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Задача не найдена!".getBytes(CHARSET));
        }
    }

    public void sendHasInteractions(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(406, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Задача пересекается с уже существующими!".getBytes(CHARSET));
        }
    }

    public void sendEmpty(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(404, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Список задач пуст!".getBytes(CHARSET));
        }
    }

    public void sendWrongMethod(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(400, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write("Невереный метод!".getBytes(CHARSET));
        }
    }

    public String bodyToString(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), CHARSET);
    }

    public Gson getGson() {
        return gson;
    }

    public Optional<Integer> getTaskId(HttpExchange exchange) {
        try {
            String[] splitPath = exchange.getRequestURI().getPath().split("/");
            return Optional.of(Integer.parseInt(splitPath[2]));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

}
