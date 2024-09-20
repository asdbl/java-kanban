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
    public void getTaskResponse(HttpExchange exchange) throws IOException {
        if (manager.getPrioritizedTasks().isEmpty()) {
            sendEmpty(exchange);
        } else {
            sendText(exchange, gson.toJson(manager.getPrioritizedTasks()), 200);
        }
    }
}
