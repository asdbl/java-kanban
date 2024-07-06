package task;

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
