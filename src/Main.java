import manager.taskManager.InMemoryTaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Task task1 = new Task("t1","t1d", Status.NEW);
        Task task2 = new Task("t2","t2d", Status.NEW);
        Epic epic = new Epic("e1","e1d", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("e2","e2d", Status.NEW, new ArrayList<>());
        Subtask subtask1 = new Subtask("s1","s1d", Status.NEW, epic.getId());
        Subtask subtask2 = new Subtask("s2","s2d", Status.NEW, epic.getId());
        Subtask subtask3 = new Subtask("s3","s3d", Status.NEW, epic.getId());
        HashMap<Integer, Task> tasks = new HashMap<>();
        HashMap<Integer, Epic> epics = new HashMap<>();
        HashMap<Integer, Subtask> subtasks = new HashMap<>();
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(tasks, epics, subtasks);

        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(epic);
        inMemoryTaskManager.add(epic2);
        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);
        inMemoryTaskManager.add(subtask3);

        inMemoryTaskManager.getTaskById(task1.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(epic.getId());
        inMemoryTaskManager.getTaskById(epic2.getId());
        inMemoryTaskManager.getTaskById(subtask1.getId());
        inMemoryTaskManager.getTaskById(subtask2.getId());
        inMemoryTaskManager.getTaskById(subtask3.getId());
        inMemoryTaskManager.getTaskById(task1.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        System.out.println("История: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeById(task1.getId());
        System.out.println("История после удаления задачи: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(epic.getId());

        inMemoryTaskManager.removeById(epic.getId());
        System.out.println("История после удаления эпика с тремя подзадачами: " + inMemoryTaskManager.getHistory());
    }
}
