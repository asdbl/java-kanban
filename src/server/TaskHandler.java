package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.taskManager.TaskManager;
import task.Task;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String[] pathSplit = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (pathSplit.length == 2) {
                    getTaskResponse(exchange);
                } else {
                    if (getTaskId(exchange).isPresent()) {
                        getTaskByIdResponse(exchange, getTaskId(exchange).get());
                    }
                }
                break;
            case "POST":
                postTask(exchange);
                break;
            case "DELETE":
                deleteTask(exchange, getTaskId(exchange).get());
                break;
        }
    }

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

    public void getTaskByIdResponse(HttpExchange exchange, int id) {
        try {
            if (manager.getTasks().contains(manager.getTaskById(id))) {
                Task taskById = manager.getTaskById(id);
                sendText(exchange, gson.toJson(taskById), 200);
            } else {
                sendText(exchange, "Задача не найдена!", 404);
            }
        } catch (IOException e) {
            System.out.println("Ошибка во время запроса задачи по id! " + e.getMessage());
        }
    }

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
