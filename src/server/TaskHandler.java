package server;

import com.sun.net.httpserver.HttpExchange;
import manager.taskManager.TaskManager;
import task.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void getTaskResponse(HttpExchange exchange) {
        try {
            if (manager.getTasks().isEmpty()) {
                sendText(exchange, "Список задач пуст!", 404);
            } else {
                sendText(exchange, gson.toJson(manager.getTasks()), 200);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса списка задач! " + e.getMessage());
        }
    }

    @Override
    public void getTaskByIdResponse(HttpExchange exchange, int id) {
        try {
            Task task = manager.getTaskById(id);
            if (task == null) {
                sendText(exchange, "Задача не найдена!", 404);
            } else {
                sendText(exchange, gson.toJson(task), 200);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса задачи по id! " + e.getMessage());
        }
    }

    @Override
    public void deleteTask(HttpExchange exchange, int id) {
        try {
            if (manager.getTaskById(id) != null) {
                manager.removeById(id);
                sendText(exchange, "Задача с id " + id + " успешно удалена!", 200);
            } else {
                sendText(exchange, "Задача не найдена", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время удаления задачи! " + e.getMessage());
        }
    }

    @Override
    public void postTask(HttpExchange exchange) {
        try {
            String ex = bodyToString(exchange);
            if (!ex.isEmpty()) {
                Task task = gson.fromJson(ex, Task.class);
                if (manager.getTaskById(task.getId()) != null) {
                    manager.update(task);
                    sendText(exchange, "Задача обновлена.", 201);
                } else if (manager.isOverlap(task)) {
                    sendText(exchange, "Задача пересекается по времени!", 406);
                } else {
                    manager.add(task);
                    sendText(exchange, "Задача добавлена.", 201);
                }
            } else {
                sendText(exchange, "Задача не передана!", 400);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время добавления задачи! " + e.getMessage());
        }
    }
}
