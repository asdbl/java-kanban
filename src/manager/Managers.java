package manager;

import manager.historyManager.HistoryManager;
import manager.historyManager.InMemoryHistoryManager;
import manager.taskManager.InMemoryTaskManager;
import manager.taskManager.TaskManager;

import java.util.HashMap;

public class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(new HashMap<>(), new HashMap<>(),
                new HashMap<>());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
