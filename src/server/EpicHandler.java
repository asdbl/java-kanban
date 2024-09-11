package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManager.TaskManager;
import task.Epic;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (pathSplit.length == 2) {
                    getEpicResponse(exchange);
                } else {
                    if (getTaskId(exchange).isPresent()) {
                        getEpicByIdResponse(exchange, getTaskId(exchange).get());
                    }
                }
                break;
            case "POST":
                postEpic(exchange);
                break;
            case "DELETE":
                deleteEpic(exchange, getTaskId(exchange).get());
                break;
        }

    }

    public void getEpicResponse(HttpExchange exchange) {
        try {
            if (manager.getEpics().isEmpty()) {
                sendText(exchange, "Список эпиков пуст!", 404);
            } else {
                sendText(exchange, gson.toJson(manager.getEpics()), 200);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса списка эпиков! " + e.getMessage());
        }
    }

    public void getEpicByIdResponse(HttpExchange exchange, int id) {
        try {
            if (manager.getTaskById(id) != null) {
                Epic epic = (Epic) manager.getTaskById(id);
                sendText(exchange, gson.toJson(epic), 200);
            } else {
                sendText(exchange, "Эпик не найден!", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса эпика по id: " + id + "! " + e.getMessage());
        }
    }

    public void deleteEpic(HttpExchange exchange, int id) {
        try {
            if (manager.getEpics().contains(manager.getTaskById(id))) {
                manager.removeById(id);
                sendText(exchange, "Эпик с id " + id + " успешно удален!", 200);
            } else {
                sendText(exchange, "Эпик не найден", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время удаления эпика! " + e.getMessage());
        }
    }

    public void postEpic(HttpExchange exchange) {
        try {
            String ex = bodyToString(exchange);
            if (!ex.isEmpty()) {
                Epic epic = gson.fromJson(ex, Epic.class);
                if (manager.isOverlap(epic)) {
                    sendHasInteractions(exchange);
                } else if (!manager.getEpics().contains(epic)) {
                    manager.add(epic);
                    sendText(exchange, "Эпик добавлен.", 201);
                } else {
                    manager.update(epic);
                    sendText(exchange, "Эпик обновлен.", 201);
                }
            } else {
                sendText(exchange, "Эпик не передан!", 400);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время добавления/обновления эпика! " + e.getMessage());
        }
    }
}
