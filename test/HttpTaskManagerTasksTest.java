import com.google.gson.Gson;
import manager.taskManager.InMemoryTaskManager;
import manager.taskManager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.BaseHttpHandler;
import server.HttpTaskServer;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    BaseHttpHandler handler = new BaseHttpHandler(manager);
    Gson gson = handler.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() throws IOException {
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void addTaskTest() throws IOException, InterruptedException {
        Task task = new Task("t1", "t1d", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        String json = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager, "Список пуст");
        assertEquals("t1", manager.getTasks().getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void addEpicTest() throws IOException, InterruptedException {
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        String json = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager, "Список пуст");
        assertEquals("e1", manager.getEpics().getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void addSubtaskTest() throws IOException, InterruptedException {
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        String json = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertNotNull(manager, "Список пуст");
        assertEquals("s1", manager.getSubtasks().getFirst().getTaskName(), "Некорректное имя задачи");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        Task task = new Task("t1", "t1d",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlForDelete = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Задача с id 1 успешно удалена!", responseDelete.body());
        assertEquals(new ArrayList<>(), manager.getTasks(), "Задача не удалена");
    }

    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlForDelete = URI.create("http://localhost:8080/epics/1");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Эпик с id 1 успешно удален!", responseDelete.body());
        assertEquals(new ArrayList<>(), manager.getEpics(), "Эпик не удален");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(), Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));

        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());

        URI urlForDelete = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest requestForDelete = HttpRequest.newBuilder()
                .uri(urlForDelete)
                .DELETE()
                .build();
        System.out.println(manager.getSubtasks().getFirst().getId());
        HttpResponse<String> responseDelete = client.send(requestForDelete, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, responseDelete.statusCode());
        assertEquals("Подзадача с id 2 успешно удалена!", responseDelete.body());
        assertEquals(new ArrayList<>(), manager.getSubtasks(), "Подзадача не удалена");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("t1", "t1d",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());

        String taskJson = gson.toJson(task);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Task updatedTask = new Task(1, "t2", "t2d",
                Status.DONE, Duration.ofMinutes(15), LocalDateTime.now().plusMinutes(60));
        String updatedTaskJson = gson.toJson(updatedTask);

        URI urlForUpdate = URI.create("http://localhost:8080/tasks/1");
        HttpRequest requestForUpdate = HttpRequest.newBuilder()
                .uri(urlForUpdate)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();
        HttpResponse<String> responseUpdate = client.send(requestForUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseUpdate.statusCode());
        assertEquals("Задача обновлена.", responseUpdate.body());
        assertEquals("t2", manager.getTaskById(updatedTask.getId()).getTaskName());
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(5), LocalDateTime.of(2024, 9, 11, 15, 30));
        String epicJson = gson.toJson(epic);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic updatedEpic = new Epic(1, "e2", "e2d",
                Status.DONE, new ArrayList<>(), Duration.ofMinutes(15), LocalDateTime.now().plusMinutes(60));
        String updatedTaskJson = gson.toJson(updatedEpic);
        URI urlForUpdate = URI.create("http://localhost:8080/tasks");
        HttpRequest updateRequest = HttpRequest.newBuilder()
                .uri(urlForUpdate)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();
        HttpResponse<String> responseUpdate = client.send(updateRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseUpdate.statusCode());
        assertEquals("Задача обновлена.", responseUpdate.body());
        assertEquals("e2", manager.getTaskById(updatedEpic.getId()).getTaskName());
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(5), LocalDateTime.now());
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtaskToUpdate = new Subtask(2, "s2", "s2d", Status.NEW, epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(120));
        String updatedTaskJson = gson.toJson(subtaskToUpdate);
        URI urlForUpdate = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest requestForUpdate = HttpRequest.newBuilder()
                .uri(urlForUpdate)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();
        HttpResponse<String> responseUpdate = client.send(requestForUpdate, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, responseUpdate.statusCode());
        assertEquals("Подзадача обновлена.", responseUpdate.body());
        assertEquals("s2", manager.getTaskById(subtaskToUpdate.getId()).getTaskName());
    }

    @Test
    public void testPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("t1", "t1d",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(60));
        Task task2 = new Task("t2", "t2d",
                Status.NEW, Duration.ofMinutes(20), LocalDateTime.now());
        Task task3 = new Task("t3", "t3d",
                Status.NEW, Duration.ofMinutes(35), LocalDateTime.now().plusMinutes(120));
        URI url = URI.create("http://localhost:8080/tasks");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3)))
                .build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());
        String firstPrioritized = manager.getPrioritizedTasks().getFirst().getTaskName();
        assertEquals("t2", firstPrioritized);
    }

    @Test
    public void testHistory() throws IOException, InterruptedException {
        Task task1 = new Task("t1", "t1d",
                Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(60));
        Task task2 = new Task("t2", "t2d",
                Status.NEW, Duration.ofMinutes(20), LocalDateTime.now());
        Task task3 = new Task("t3", "t3d",
                Status.NEW, Duration.ofMinutes(35), LocalDateTime.now().plusMinutes(120));
        URI url = URI.create("http://localhost:8080/tasks");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();
        client.send(request1, HttpResponse.BodyHandlers.ofString());
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task2)))
                .build();
        client.send(request2, HttpResponse.BodyHandlers.ofString());
        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task3)))
                .build();
        client.send(request3, HttpResponse.BodyHandlers.ofString());

        URI url1 = URI.create("http://localhost:8080/tasks/1");
        URI url2 = URI.create("http://localhost:8080/tasks/3");
        URI url3 = URI.create("http://localhost:8080/tasks/2");
        HttpRequest getRequest1 = HttpRequest.newBuilder()
                .uri(url1)
                .GET()
                .build();
        HttpRequest getRequest2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpRequest getRequest3 = HttpRequest.newBuilder()
                .uri(url3)
                .GET()
                .build();

        client.send(getRequest1, HttpResponse.BodyHandlers.ofString());
        client.send(getRequest2, HttpResponse.BodyHandlers.ofString());
        client.send(getRequest3, HttpResponse.BodyHandlers.ofString());

        URI historyUrl = URI.create("http://localhost:8080/history");

        HttpRequest getHistory = HttpRequest.newBuilder()
                .uri(historyUrl)
                .GET()
                .build();
        HttpResponse<String> historyResponse = client.send(getHistory, HttpResponse.BodyHandlers.ofString());
        assertEquals(gson.toJson(manager.getHistory()), historyResponse.body());
    }
}


