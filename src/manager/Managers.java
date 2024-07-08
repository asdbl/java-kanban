package manager;

import manager.historyManager.HistoryManager;
import manager.historyManager.InMemoryHistoryManager;
import manager.taskManager.InMemoryTaskManager;
import manager.taskManager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.HashMap;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(new HashMap<Integer, Task>(), new HashMap<Integer, Epic>(),
                new HashMap<Integer, Subtask>());
    }

    public static HistoryManager getDefaultHistory() {
       return new InMemoryHistoryManager();
    }
}
