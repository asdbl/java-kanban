package manager.taskManager;

import manager.Managers;
import manager.historyManager.HistoryManager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private int id = 1;
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (isDuplicate(task)) return;
        task.setId(id++);
        final Task taskToAdd = new Task(task);
        tasks.put(taskToAdd.getId(), taskToAdd);

    }

    @Override
    public void add(Epic epic) {
        if (isDuplicate(epic)) return;
        if (epic.getSubtaskIdList().contains(epic.getId())) {
            System.out.println("В эпик нельзя добавить самого себя");
            return;
        }
        for (int id : epic.getSubtaskIdList()) {
            if (epics.containsKey(id)) {
                System.out.println("Эпик не может быть подзадачей");
                return;
            }
        }
        epic.setId(id++);
        final Epic epicToAdd = new Epic(epic);
        epics.put(epicToAdd.getId(), epicToAdd);
    }

    @Override
    public void add(Subtask subtask) {
        if (isDuplicate(subtask)) return;
        subtask.setId(id++);
        final Subtask subtaskToAdd = new Subtask(subtask);
        subtasks.put(subtaskToAdd.getId(), subtaskToAdd);
        Epic epic = epics.get(subtask.getEpicId());
        if (subtask.getId() == epic.getId()) return;
        if (epic.getSubtaskIdList().contains(subtask.getId())) return;
        epic.getSubtaskIdList().add(subtask.getId());
        updateEpicStatus(epic);
    }


    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskIdList().isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }
        int subtaskDoneCounter = 0;
        int subtaskNewCounter = 0;
        for (Integer subtaskId : epic.getSubtaskIdList()) {
            if (!subtasks.containsKey(subtaskId)) {
                return;
            }
            Subtask sub = subtasks.get(subtaskId);
            if (sub.getStatus() == Status.DONE) {
                subtaskDoneCounter++;
            }
            if (sub.getStatus() == Status.NEW) {
                subtaskNewCounter++;
            }
        }
        if (subtaskDoneCounter == epic.getSubtaskIdList().size()) {
            epic.setStatus(Status.DONE);
        } else if (subtaskNewCounter == epic.getSubtaskIdList().size()) {
            epic.setStatus(Status.NEW);
        } else epic.setStatus(Status.IN_PROGRESS);
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
                subtasks.get(i).setStatus(Status.NEW);
            }
        }
        if (epic.getStatus() == Status.DONE) {
            for (int i : epic.getSubtaskIdList()) {
                subtasks.get(i).setStatus(Status.DONE);
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
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getAll() {
        List<Task> allTasks = new ArrayList<>();
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
        historyManager.clear();
        id = 1;
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
            epic.getSubtaskIdList().remove((Integer) subtaskToRemove.getId());
            subtasks.remove(id);
        } else {
            System.out.println("Неверный id!");
        }
    }

    @Override
    public List<Subtask> getSubtasksByEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        for (int i : epic.getSubtaskIdList()) {
            subtaskList.add(subtasks.get(i));
        }
        return subtaskList;
    }
}

