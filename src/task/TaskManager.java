package task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    void add(Task task);
    void add(Epic epic);
    void add(Subtask subtask);
    void update(Task task);
    void update(Epic epic);
    void update(Subtask subtask);
    ArrayList<Task> getTasks();
    ArrayList<Epic> getEpics();
    ArrayList<Subtask> getSubtasks();
    ArrayList<Task> getAll();
    void clear();
    Task getTaskById(int id);
    void removeById(int id);
    ArrayList<Subtask> getSubtasksByEpic(Epic epic);


}
