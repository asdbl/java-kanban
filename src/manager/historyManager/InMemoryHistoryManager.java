package manager.historyManager;

import task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList;
    private static final int INITIAL_CAPACITY = 10;
    private int historyListIndex = 0;

    public InMemoryHistoryManager() {
        historyList = new LinkedList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (historyList.size() == INITIAL_CAPACITY) {
            historyList.removeFirst();
        }
        historyList.add(task);
        historyListIndex++;
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
