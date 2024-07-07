package manager;

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
        Task tempTask = new Task(task.getTaskName(), task.getTaskDescription(), task.getStatus());
        tempTask.setId(task.getId());
        historyList.add(tempTask);
        historyListIndex++;
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}
