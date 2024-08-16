package manager;

import manager.historyManager.HistoryManager;
import manager.historyManager.InMemoryHistoryManager;
import manager.taskManager.FileBackedTaskManager;
import manager.taskManager.TaskManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        try {
            Path file = Path.of("src/resources/savedTask.csv");
            Path dir = Path.of("src/resources");
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
                Files.createFile(file);
            }
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
            return new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
