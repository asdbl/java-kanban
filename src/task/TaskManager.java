package task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 1;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public TaskManager(HashMap<Integer, Task> tasks, HashMap<Integer, Epic> epics, HashMap<Integer, Subtask> subtasks) {
        this.tasks = tasks;
        this.epics = epics;
        this.subtasks = subtasks;
    }

    public void add(Task task) {
        if (isDuplicate(task)) return;
        task.setId(id++);
        tasks.put(task.getId(), task);
    }

    public void add(Epic epic) {
        if (isDuplicate(epic)) return;
        epic.setId(id++);
        epics.put(epic.getId(), epic);
    }

    public void add(Subtask subtask) {
        if (isDuplicate(subtask)) return;
        subtask.setId(id++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtaskIdList().add(subtask.getId());
        updateEpicStatus(epic);

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


    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

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

    public void update(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);

        Epic epic = epics.get(subtask.getEpicId());

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

    public ArrayList<Task> getTasks(HashMap<Integer, Task> tasks) {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getEpics(HashMap<Integer, Epic> epics) {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getSubtasks(HashMap<Integer, Subtask> subtasks) {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Task> getAll(HashMap<Integer, Task> tasks, HashMap<Integer, Epic> epics, HashMap<Integer, Subtask> subtasks) {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getTasks(tasks));
        allTasks.addAll(getEpics(epics));
        allTasks.addAll(getSubtasks(subtasks));
        return allTasks;
    }

    public void clear() {
        tasks.clear();
        epics.clear();
        subtasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void removeById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            Epic epicToRemove = epics.get(id);
            for (Integer subtaskId : epicToRemove.getSubtaskIdList()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            Subtask subtaskToRemove = subtasks.get(id);
            Epic epic = epics.get(subtaskToRemove.getEpicId());
            epic.getSubtaskIdList().remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("Неверный id!");
        }
    }

    public ArrayList<Subtask> getSubtasksByEpic(Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (int i : epic.getSubtaskIdList()) {
            subtaskList.add(subtasks.get(i));
        }
        return subtaskList;
    }
}
