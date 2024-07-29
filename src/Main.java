import manager.taskManager.InMemoryTaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("t1", "t1d", Status.NEW);
        inMemoryTaskManager.add(task1);
        Task task2 = new Task("t2", "t2d", Status.NEW);
        inMemoryTaskManager.add(task2);
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic);
        Epic epic2 = new Epic("e2", "e2d", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic2);
        Subtask subtask1 = new Subtask("s1", "s1d", Status.NEW, epic.getId());
        inMemoryTaskManager.add(subtask1);
        Subtask subtask2 = new Subtask("s2", "s2d", Status.NEW, epic.getId());
        inMemoryTaskManager.add(subtask2);
        Subtask subtask3 = new Subtask("s3", "s3d", Status.NEW, epic.getId());
        inMemoryTaskManager.add(subtask3);

        inMemoryTaskManager.getTaskById(task1.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(epic.getId());
        inMemoryTaskManager.getTaskById(epic2.getId());
        inMemoryTaskManager.getTaskById(subtask1.getId());
        inMemoryTaskManager.getTaskById(subtask2.getId());
        inMemoryTaskManager.getTaskById(subtask3.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        System.out.println("История: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.removeById(task1.getId());
        System.out.println("История после удаления задачи: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(task2.getId());
        inMemoryTaskManager.getTaskById(epic.getId());
        inMemoryTaskManager.removeById(epic.getId());
        System.out.println("История после удаления эпика с тремя подзадачами: " + inMemoryTaskManager.getHistory());
        inMemoryTaskManager.clear();
        System.out.println("История после удаления всех задач: " + inMemoryTaskManager.getHistory());
    }
}
