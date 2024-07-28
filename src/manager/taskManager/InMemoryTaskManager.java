package manager.taskManager;

import manager.Managers;
import manager.historyManager.HistoryManager;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager extends StatusSetter implements TaskManager{
    private static int id = 1;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager(HashMap<Integer, Task> tasks, HashMap<Integer, Epic> epics, HashMap<Integer, Subtask> subtasks) {
        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
    }

    @Override
    protected void setTaskStatus(Task task, Status status) {
        super.setTaskStatus(task, status);
    }

    public static int getId() {
        return id++;
    }

    @Override
    public void add(Task task) {
        if (isDuplicate(task)) return;
        tasks.put(task.getId(), task);
    }

    @Override
    public void add(Epic epic) {
        if (isDuplicate(epic)) return;
        if(epic.getSubtaskIdList().contains(epic.getId())) {
            System.out.println("В эпик нельзя добавить самого себя");
            return;
        }
        for (int id : epic.getSubtaskIdList()){
            if (epics.containsKey(id)) {
                System.out.println("Эпик не может быть подзадачей");
                return;
            }
        }
        epics.put(epic.getId(), epic);
    }

    @Override
    public void add(Subtask subtask) {
        if (isDuplicate(subtask)) return;
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (subtask.getId() == epic.getId()) return;
        epic.getSubtaskIdList().add(subtask.getId());
        updateEpicStatus(epic);
    }


    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskIdList().isEmpty()) {
            setTaskStatus(epic, Status.NEW);
            return;
        }
        int subtaskDoneCounter = 0;
        int subtaskNewCounter = 0;
        for (Integer subtaskId : epic.getSubtaskIdList()) {
            Subtask sub = subtasks.get(subtaskId);
            if (sub.getStatus() == Status.DONE) {
                subtaskDoneCounter++;
            }
            if (sub.getStatus() == Status.NEW) {
                subtaskNewCounter++;
            }
        }
        if (subtaskDoneCounter == epic.getSubtaskIdList().size()) {
            setTaskStatus(epic, Status.DONE);
        } else if (subtaskNewCounter == epic.getSubtaskIdList().size()) {
            setTaskStatus(epic, Status.NEW);
        } else setTaskStatus(epic, Status.IN_PROGRESS);
    }

    private boolean isDuplicate(Task task) {
        for (Integer i : tasks.keySet()) {
            if (tasks.get(i).equals(task)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicate(Epic epic) {
        for (Integer i : epics.keySet()) {
            if (epics.get(i).equals(epic)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicate(Subtask subtask) {
        for (Integer i : subtasks.keySet()) {
            if (subtasks.get(i).equals(subtask)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        if (epic.getStatus() == Status.NEW) {
            for (int i : epic.getSubtaskIdList()) {
                setTaskStatus(subtasks.get(i),Status.NEW);
            }
        }
        if (epic.getStatus() == Status.DONE) {
            for (int i : epic.getSubtaskIdList()) {
                setTaskStatus(subtasks.get(i),Status.DONE);
            }
        }
    }

    @Override
    public void update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Task> getAll() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks());
        allTasks.addAll(getEpics());
        allTasks.addAll(getSubtasks());
        return allTasks;
    }

    @Override
    public void clear() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        }
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        }
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        }
        System.out.println("Неверный id...");
        return null;
    }


    public List<Task> getHistory() {

        return historyManager.getHistory();
    }

    @Override
    public void removeById(int id) {
        if (historyManager.getHistory().contains(getTaskById(id))) {
            historyManager.remove(id);
        }
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Epic epicToRemove = epics.get(id);
            for (Integer subtaskId : epicToRemove.getSubtaskIdList()) {
                if (historyManager.getHistory().contains(getTaskById(subtaskId))) {
                    historyManager.remove(subtaskId);
                }
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtaskToRemove = subtasks.get(id);
            Epic epic = epics.get(subtaskToRemove.getEpicId());
            epic.getSubtaskIdList().remove(epic.getSubtaskIdList().indexOf(subtaskToRemove.getId()));
            subtasks.remove(id);
        } else {
            System.out.println("Неверный id!");
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (int i : epic.getSubtaskIdList()) {
            subtaskList.add(subtasks.get(i));
        }
        return subtaskList;
    }


}

