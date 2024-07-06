package task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyList = new ArrayList<>(INITIAL_CAPACITY);
    private static final int INITIAL_CAPACITY = 10;
    private int historyListIndex = 0;

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
