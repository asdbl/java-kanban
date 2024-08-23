import manager.taskManager.FileBackedTaskManager;
import manager.taskManager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileBackedTaskManagerTest {

    @Test
    public void testAdd() throws IOException {
        TaskManager manager = new FileBackedTaskManager(File.createTempFile("savedTest", ".csv").toPath());
        Task task = new Task("t1", "t1d", Status.NEW);
        manager.add(task);
        Assertions.assertTrue(manager.getTasks().contains(task));
    }

    @Test
    public void savingToFileTest() throws IOException {
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Task task = new Task("t1", "t1d", Status.NEW);
        manager.add(task);
        String expected = "1,TASK,t1,NEW,t1d";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertEquals(expected, line);

    }

    @Test
    public void loadingFromFileTest() throws IOException {
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Task task = new Task("t1", "t1d", Status.NEW);
        manager.add(task);
        Task task2 = new Task("t2", "t2d", Status.NEW);
        manager.add(task2);
        Epic epic = new Epic("e3", "e3d", Status.NEW, new ArrayList<>());
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId());
        manager.add(subtask);
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(manager.getAll(), loadedManager.getAll());
    }

    @Test
    public void shouldReturnHistoryFromFileBackedTaskManager() throws IOException {
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Task task = new Task("t1", "t1d", Status.NEW);
        manager.add(task);
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>());
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId());
        manager.add(subtask);
        manager.getTaskById(task.getId());
        manager.getTaskById(epic.getId());
        manager.getTaskById(subtask.getId());
        Assertions.assertEquals(manager.getAll(), manager.getHistory());
    }

    @Test
    public void shouldAddDifferentTasks() throws IOException {
        ArrayList<Task> expected = new ArrayList<>();
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Task task1 = new Task("t1", "t1d", Status.NEW);
        manager.add(task1);
        expected.add(task1);
        Task task2 = new Task("t2", "t2d", Status.NEW);
        manager.add(task2);
        expected.add(task2);
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>());
        manager.add(epic);
        expected.add(epic);
        Epic epic2 = new Epic("e2", "e2d", Status.NEW, new ArrayList<>());
        manager.add(epic2);
        expected.add(epic2);
        Subtask subtask1 = new Subtask("s1", "s1d", Status.NEW, epic.getId());
        manager.add(subtask1);
        expected.add(subtask1);
        Subtask subtask2 = new Subtask("s2", "s2d", Status.NEW, epic.getId());
        manager.add(subtask2);
        expected.add(subtask2);
        Subtask subtask3 = new Subtask("s3", "s3d", Status.NEW, epic.getId());
        manager.add(subtask3);
        expected.add(subtask3);
        Assertions.assertEquals(expected, manager.getAll());
    }

    @Test
    public void savingAndLoadingEmptyFileTest() throws IOException {
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Assertions.assertTrue(Files.readAllLines(file.toPath()).isEmpty());
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(manager.getAll(), loadedManager.getAll());
    }
}
