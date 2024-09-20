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
    public void getTaskResponse(HttpExchange exchange) {
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

    @Override
    public void getTaskByIdResponse(HttpExchange exchange, int id) {
        try {
            Epic epic = (Epic) manager.getTaskById(id);
            if (epic == null) {
                sendText(exchange, "Эпик не найден!", 404);
                return;
            }
            sendText(exchange, gson.toJson(epic), 200);
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса эпика по id: " + id + "!");
        }
    }

    @Override
    public void deleteTask(HttpExchange exchange, int id) {
        try {
            if (manager.getTaskById(id) != null) {
                manager.removeById(id);
                sendText(exchange, "Эпик с id " + id + " успешно удален!", 200);
            } else {
                sendText(exchange, "Эпик не найден", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время удаления эпика! " + e.getMessage());
        }
    }

    @Override
    public void postTask(HttpExchange exchange) {
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
