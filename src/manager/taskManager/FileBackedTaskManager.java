package manager.taskManager;

import exceptions.ManagerSaveException;
import task.*;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path filename;

    public FileBackedTaskManager(Path filename) {
        super();
        this.filename = filename;
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    public void addWithoutSave(Task task) {
        super.add(task);
    }

    public void addWithoutSave(Epic epic) {
        super.add(epic);
    }

    public void addWithoutSave(Subtask subtask) {
        super.add(subtask);
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    public String toString(Task task) {
        TasksType tasksType;
        String taskToString = null;
        if (task.getClass() == Task.class) {
            tasksType = TasksType.TASK;
            taskToString = String.format("%d,%s,%s,%s,%s", task.getId(), tasksType, task.getTaskName(), task.getStatus(), task.getTaskDescription());
        } else if (task.getClass() == Epic.class) {
            tasksType = TasksType.EPIC;
            Epic epic = (Epic) task;
            taskToString = String.format("%d,%s,%s,%s,%s,%s", epic.getId(), tasksType, epic.getTaskName(), epic.getStatus(), epic.getTaskDescription(), epic.getSubtaskIdList());
        } else if (task.getClass() == Subtask.class) {
            tasksType = TasksType.SUBTASK;
            Subtask subtask = (Subtask) task;
            taskToString = String.format("%d,%s,%s,%s,%s,%d", subtask.getId(), tasksType, subtask.getTaskName(), subtask.getStatus(), subtask.getTaskDescription(), subtask.getEpicId());
        }
        return taskToString;
    }

    public static void addToManagerFromString(String value, FileBackedTaskManager fileBackedTaskManager) {
        if (value != null && !value.isEmpty()) {
            String[] taskFromString = value.split(",");
            int id = Integer.parseInt(taskFromString[0]);
            String taskName = taskFromString[2];
            Status status = Status.valueOf(taskFromString[3]);
            String description = taskFromString[4];

            switch (taskFromString[1]) {
                case "TASK" -> {
                    Task task = new Task(taskName, description, status);
                    task.setId(id);
                    fileBackedTaskManager.addWithoutSave(task);
                }
                case "EPIC" -> {
                    List<Integer> subtaskId = new ArrayList<>();
                    if (taskFromString[5] != null && !taskFromString[5].isEmpty()) {
                        for (int i = 5; i < taskFromString.length; i++) {
                            String s = taskFromString[i].replaceAll("\\D+", "");
                            if (!s.isEmpty()) {
                                subtaskId.add(Integer.parseInt(s));
                            }
                        }
                    }
                    Epic epic = new Epic(taskName, description, status, subtaskId);
                    epic.setId(id);
                    fileBackedTaskManager.addWithoutSave(epic);
                }
                case "SUBTASK" -> {
                    if (taskFromString[5] != null) {
                        int epicIdFromFile = Integer.parseInt(taskFromString[5]);
                        Subtask subtask = new Subtask(taskName, description, status, epicIdFromFile);
                        subtask.setId(id);
                        fileBackedTaskManager.addWithoutSave(subtask);
                    }
                }
            }
        }
    }

    public void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename.toFile()))) {
            writer.write("id,type,name,status,description,epic");
            writer.write("\n");
            for (Task task : getAll()) {
                writer.write(toString(task));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла.", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line = bufferedReader.readLine();
            FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file.toPath());
            while (line != null) {
                line = bufferedReader.readLine();
                addToManagerFromString(line, fileBackedTaskManager);
            }
            return fileBackedTaskManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
