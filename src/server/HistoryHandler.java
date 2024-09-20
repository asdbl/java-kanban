package server;

import com.sun.net.httpserver.HttpExchange;
import manager.taskManager.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void getTaskResponse(HttpExchange exchange) {
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
