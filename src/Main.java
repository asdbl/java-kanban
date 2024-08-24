import manager.Managers;
import manager.taskManager.TaskManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        TaskManager fileBackedTaskManager = Managers.getDefault();

        Task task1 = new Task("t1", "t1d", Status.NEW, Duration.of(100, ChronoUnit.MINUTES), LocalDateTime.now());
        fileBackedTaskManager.add(task1);
        Task task2 = new Task("t2", "t2d", Status.NEW, Duration.of(100, ChronoUnit.MINUTES), LocalDateTime.now());
        fileBackedTaskManager.add(task2);
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>());
        fileBackedTaskManager.add(epic);
        Epic epic2 = new Epic("e2", "e2d", Status.NEW, new ArrayList<>());
        fileBackedTaskManager.add(epic2);
        Subtask subtask1 = new Subtask("s1", "s1d", Status.DONE, epic.getId(), Duration.of(100, ChronoUnit.MINUTES), LocalDateTime.now());
        fileBackedTaskManager.add(subtask1);
        Subtask subtask2 = new Subtask("s2", "s2d", Status.DONE, epic.getId(), Duration.of(100, ChronoUnit.MINUTES), LocalDateTime.now());
        fileBackedTaskManager.add(subtask2);
        Subtask subtask3 = new Subtask("s3", "s3d", Status.DONE, epic.getId(), Duration.of(100, ChronoUnit.MINUTES), LocalDateTime.now());
        fileBackedTaskManager.add(subtask3);

        fileBackedTaskManager.getTaskById(task1.getId());
        fileBackedTaskManager.getTaskById(task2.getId());
        fileBackedTaskManager.getTaskById(epic.getId());
        fileBackedTaskManager.getTaskById(epic2.getId());
        fileBackedTaskManager.getTaskById(subtask1.getId());
        fileBackedTaskManager.getTaskById(subtask2.getId());
        fileBackedTaskManager.getTaskById(subtask3.getId());
        fileBackedTaskManager.getTaskById(task2.getId());

        System.out.println("История fileBackedTaskManager: " + fileBackedTaskManager.getHistory());
        fileBackedTaskManager.removeById(task1.getId());
        System.out.println("История fileBackedTaskManager после удаления задачи: " + fileBackedTaskManager.getHistory());
        fileBackedTaskManager.getTaskById(task2.getId());
        fileBackedTaskManager.getTaskById(task2.getId());
        fileBackedTaskManager.getTaskById(epic.getId());
        System.out.println(fileBackedTaskManager.getTaskById(epic.getId()).getStartTime());
        System.out.println(subtask1.getStartTime());
        System.out.println(fileBackedTaskManager.getTaskById(epic.getId()).getDuration());
        fileBackedTaskManager.removeById(epic.getId());
        System.out.println("История fileBackedTaskManager после удаления эпика с тремя подзадачами: " + fileBackedTaskManager.getHistory());
        fileBackedTaskManager.clear();
        System.out.println("История fileBackedTaskManager после удаления всех задач: " + fileBackedTaskManager.getHistory());
    }
}
