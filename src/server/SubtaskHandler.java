package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManager.TaskManager;
import task.Subtask;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (pathSplit.length == 2) {
                    getSubtaskResponse(exchange);
                } else {
                    if (getTaskId(exchange).isPresent()) {
                        getSubtaskByIdResponse(exchange, getTaskId(exchange).get());
                    }
                }
                break;
            case "POST":
                postSubtask(exchange);
                break;
            case "DELETE":
                deleteSubtask(exchange, getTaskId(exchange).get());
                break;
        }
    }

    public void getSubtaskResponse(HttpExchange exchange) {
        try {
            if (manager.getSubtasks().isEmpty()) {
                sendText(exchange, "Список подзадач пуст!", 404);
            } else {
                sendText(exchange, gson.toJson(manager.getSubtasks()), 200);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса списка подзадач! " + e.getMessage());
        }
    }

    public void getSubtaskByIdResponse(HttpExchange exchange, int id) {
        try {

            if (manager.getTaskById(id) != null) {
                Subtask subtask = (Subtask) manager.getTaskById(id);
                sendText(exchange, gson.toJson(subtask), 200);
            } else {
                sendText(exchange, "Подзадача не найдена!", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса подзадачи по id! " + e.getMessage());
        }
    }

    public void deleteSubtask(HttpExchange exchange, int id) {
        try {
            if (manager.getTaskById(id) != null) {
                manager.removeById(id);
                sendText(exchange, "Подзадача с id " + id + " успешно удалена!", 200);
            } else {
                sendText(exchange, "Подзадача не найдена", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время удаления подзадачи! " + e.getMessage());
        }
    }

    public void postSubtask(HttpExchange exchange) {
        try {
            String ex = bodyToString(exchange);
            if (!ex.isEmpty()) {
                Subtask subtask = gson.fromJson(ex, Subtask.class);
                if (manager.getTaskById(subtask.getId()) != null) {
                    manager.update(subtask);
                    sendText(exchange, "Подзадача обновлена.", 201);
                } else if (manager.isOverlap(subtask)) {
                    sendText(exchange, "Позадача пересекается по времени!", 406);
                } else {
                    manager.add(subtask);
                    sendText(exchange, "Позадача добавлена.", 201);
                }
            } else {
                sendText(exchange, "Позадача не передана!", 400);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время добавления подзадачи! " + e.getMessage());
        }
    }
}
