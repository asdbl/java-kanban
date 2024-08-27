import exceptions.ManagerSaveException;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileBackedTaskManagerTest extends InMemoryTaskManagerTest {

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
        String expected = "1,TASK,t1,NEW,t1d,-," +
                task.getStartTime() + "," + task.getDuration().toMinutes() + "," + task.getEndTime();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        reader.readLine();
        String line = reader.readLine();
        Assertions.assertEquals(expected, line);
    }

    @Test
    public void loadingFromFileTest() throws IOException {
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Task task = new Task("t1", "t1d", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2020, 1, 10, 10, 10));
        manager.add(task);
        Task task2 = new Task("t2", "t2d", Status.NEW,
                Duration.ofMinutes(15), LocalDateTime.of(2020, 2, 10, 10, 10));
        manager.add(task2);
        Epic epic = new Epic("e3", "e3d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(15), LocalDateTime.of(2020, 3, 10, 10, 10));
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId(),
                Duration.ofMinutes(15), LocalDateTime.of(2020, 4, 10, 10, 10));
        manager.add(subtask);
        TaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        Assertions.assertEquals(manager.getPrioritizedTasks(), loadedManager.getPrioritizedTasks());
    }

    @Test
    public void shouldReturnHistoryFromFileBackedTaskManager() throws IOException {
        File file = File.createTempFile("savedTest", ".csv");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Task task = new Task("t1", "t1d", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2020, 10, 10, 10, 10));
        manager.add(task);
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>());
        manager.add(epic);
        Subtask subtask = new Subtask("s1", "s1d", Status.NEW, epic.getId(),
                Duration.ofMinutes(10), LocalDateTime.of(2020, 11, 10, 10, 10));
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
        Task task1 = new Task("t1", "t1d", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.of(2020, 10, 10, 10, 10));
        manager.add(task1);
        Task task2 = new Task("t2", "t2d", Status.NEW, Duration.ofMinutes(15), LocalDateTime.now());
        manager.add(task2);
        Epic epic = new Epic("e1", "e1d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(15), LocalDateTime.of(2020, 11, 10, 10, 10));
        manager.add(epic);
        Epic epic2 = new Epic("e2", "e2d", Status.NEW, new ArrayList<>(),
                Duration.ofMinutes(15), LocalDateTime.of(2020, 11, 11, 10, 10));
        manager.add(epic2);
        Subtask subtask1 = new Subtask("s1", "s1d", Status.NEW, epic.getId(),
                Duration.ofMinutes(10), LocalDateTime.of(2021, 11, 10, 10, 10));
        manager.add(subtask1);
        Subtask subtask2 = new Subtask("s2", "s2d", Status.NEW, epic.getId(),
                Duration.ofMinutes(15), LocalDateTime.of(2022, 11, 10, 10, 10));
        manager.add(subtask2);
        Subtask subtask3 = new Subtask("s3", "s3d", Status.NEW, epic.getId(),
                Duration.ofMinutes(15), LocalDateTime.of(2023, 11, 10, 10, 10));
        manager.add(subtask3);
        epic.setDuration(subtask1.getDuration().plus(subtask2.getDuration()).plus(subtask3.getDuration()));
        epic.setStartTime(subtask1.getStartTime());
        expected.add(task1);
        expected.add(task2);
        expected.add(epic);
        expected.add(epic2);
        expected.add(subtask1);
        expected.add(subtask2);
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

    @Test
    public void shouldThrowManagerSaveExceptionTest() {
        File file = new File("test");
        TaskManager manager = new FileBackedTaskManager(file.toPath());
        Assertions.assertThrows(ManagerSaveException.class, () ->
                manager.add(new Task("t2", "t2d", Status.NEW)));
    }
}
