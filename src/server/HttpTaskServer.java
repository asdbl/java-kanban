package server;

import com.sun.net.httpserver.HttpServer;
import manager.Managers;
import manager.taskManager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager manager;
    private HttpServer server;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
    }

    public void createServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/subtasks", new SubtaskHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void start() throws IOException {
        createServer();
        server.start();
        System.out.println("Серевер запущен! Порт:" + PORT + ".");
    }

    public void stop() {
        server.stop(1);
        System.out.println("Сервер остановлен!");
    }


    public static void main(String[] args) {
        try {
            HttpTaskServer server = new HttpTaskServer(Managers.getDefault());
            server.start();
        } catch (IOException e) {
            System.out.println("Ошибка запуска сервера!");
        }
    }
}
