package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String[] splitPath = exchange.getRequestURI().getPath().split("/");
        try {
            switch (method) {
                case "GET":
                    if (splitPath.length == 2) {
                        getHistory(exchange);
                    }
                    break;
                default:
                    sendWrongMethod(exchange);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса истории просмотра задач!");
        }
    }

    public void getHistory(HttpExchange exchange) {
        try {
            if (manager.getHistory().isEmpty()) {
                sendEmpty(exchange);
            } else {
                sendText(exchange, gson.toJson(manager.getHistory()), 200);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса истории просмотра задач!");
        }
    }
}
