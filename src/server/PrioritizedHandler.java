package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManager.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager manager) {
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
                        getPrioritized(exchange);
                    }
                    break;
                default:
                    sendWrongMethod(exchange);
                    break;
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса списка приоритетных задач");
        }
    }

    public void getPrioritized(HttpExchange exchange) throws IOException {
        if (manager.getPrioritizedTasks().isEmpty()) {
            sendEmpty(exchange);
        } else {
            sendText(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
        }
    }
}
