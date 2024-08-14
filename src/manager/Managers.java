package manager;

import manager.historyManager.HistoryManager;
import manager.historyManager.InMemoryHistoryManager;
import manager.taskManager.FileBackedTaskManager;
import manager.taskManager.InMemoryTaskManager;
import manager.taskManager.TaskManager;

import java.nio.file.Paths;

public class Managers {

    private Managers() {
    }

    public static FileBackedTaskManager getFileBackedTaskManager() {
        return new FileBackedTaskManager(Paths.get("savedTasks.csv"));
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
