import task.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        HashMap<Integer, Task> tasks = new HashMap<>();
        HashMap<Integer, Subtask> subtasks = new HashMap<>();
        HashMap<Integer, Epic> epics = new HashMap<>();

        TaskManager taskManager = new TaskManager(tasks, epics, subtasks);

        Task task1 = new Task("task1", "task1 description", Status.NEW);
        taskManager.add(task1);
        Task task2 = new Task("task2", "task2 description", Status.IN_PROGRESS);
        taskManager.add(task2);
        Task task3 = new Task("task3", "task3 description", Status.DONE);
        taskManager.add(task3);

        ArrayList<Integer> subtaskIds1 = new ArrayList<>();
        ArrayList<Integer> subtaskIds2 = new ArrayList<>();

        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, subtaskIds1);
        taskManager.add(epic1);

        Epic epic2 = new Epic("epic2", "epic2 description", Status.NEW, subtaskIds2);
        taskManager.add(epic2);

        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, epic1.getId());
        taskManager.add(subtask1);
        Subtask subtask2 = new Subtask("subtask2", "subtask2 description", Status.NEW, epic1.getId());
        taskManager.add(subtask2);
        Subtask subtask3 = new Subtask("subtask3", "subtask3 description", Status.NEW, epic1.getId());
        taskManager.add(subtask3);
        Subtask subtask4 = new Subtask("subtask4", "subtask4 description", Status.NEW, epic2.getId());
        taskManager.add(subtask4);


        System.out.println("Получение всех задач: ");
        System.out.println(taskManager.getAll(tasks,epics,subtasks));
        printSmth();

        System.out.println("Добавление дубликата: ");
        Task taskDuplicate = new Task("task1", "task1 description", Status.NEW);
        System.out.println("Количество задач до добавления: " + taskManager.getTasks(tasks).size());
        taskManager.add(taskDuplicate);
        System.out.println("Количество задач после добавления: " + taskManager.getTasks(tasks).size());

        printSmth();

        System.out.println("Обновление подзадачи: ");
        subtask1.setTaskName("Update Subtask");
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.update(subtask1);
        System.out.println(taskManager.getSubtasks(subtasks));
        System.out.println(taskManager.getEpics(epics));

        printSmth();

        System.out.println("Обновление всех подзадач. Статус - Done: ");
        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        System.out.println("Статус эпика до обновления всех подзадач: " + epic1.getStatus());
        subtask3.setStatus(Status.DONE);
        taskManager.update(subtask1);
        taskManager.update(subtask2);
        taskManager.update(subtask3);
        System.out.println("Статус эпика после обновления всех подзадач: " + epic1.getStatus());

        printSmth();

        System.out.println("Статус всех подзадач, если статус epic1 - NEW: ");
        epic1.setStatus(Status.NEW);
        taskManager.update(epic1);
        for (Integer i : subtasks.keySet()) {
            System.out.println("Задача с id: " +  i + ". Статус - "+ subtasks.get(i).getStatus());
        }

        System.out.println();

        System.out.println("Статус всех подзадач, если статус epic1 - DONE: ");
        epic1.setStatus(Status.DONE);
        taskManager.update(epic1);
        for (Integer i : subtasks.keySet()) {
            System.out.println("Задача с id: " +  i + ". Статус - "+ subtasks.get(i).getStatus());
        }

        printSmth();

        System.out.print("Получение задачи по id: ");
        System.out.println(taskManager.getTaskById(task1.getId()));
        System.out.print("Получение эпика по id: ");
        System.out.println(taskManager.getTaskById(epic1.getId()));
        System.out.print("Получение подзадачи по id: ");
        System.out.println(taskManager.getTaskById(subtask1.getId()));

        printSmth();

        System.out.print("Удаление задачи по id: ");
        taskManager.removeById(task1.getId());
        System.out.println(taskManager.getTaskById(task1.getId()));

        printSmth();

        System.out.print("Получение подзадачи по эпику: ");
        System.out.println(taskManager.getSubtasksByEpic(epic1));

        printSmth();

        System.out.println("Удаление эпика: ");
        taskManager.removeById(epic1.getId());
        System.out.println("Эпик - " + taskManager.getTaskById(epic1.getId()));
        System.out.println("Подзадача 1 - " + taskManager.getTaskById(subtask1.getId()));
        System.out.println("Подзадача 2 - " + taskManager.getTaskById(subtask2.getId()));
        System.out.println("Подзадача 3 - " + taskManager.getTaskById(subtask3.getId()));

        printSmth();

        System.out.println("Удаление всех задач: ");
        taskManager.clear();
        System.out.println("Количество задач: " + taskManager.getAll(tasks,epics,subtasks).size());
    }

    static void printSmth() {
        System.out.println("-".repeat(5));
    }
}
