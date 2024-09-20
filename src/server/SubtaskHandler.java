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
    public void getTaskResponse(HttpExchange exchange) {
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

    @Override
    public void getTaskByIdResponse(HttpExchange exchange, int id) {
        try {
            final Subtask subtask = (Subtask) manager.getTaskById(id);
            if (subtask == null) {
                sendText(exchange, "Подзадача не найдена!", 404);
                return;
            }
            sendText(exchange, gson.toJson(subtask), 200);
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса подзадачи по id! " + e.getMessage());
        }
    }

    @Override
    public void deleteTask(HttpExchange exchange, int id) {
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

    @Override
    public void postTask(HttpExchange exchange) {
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
