package task;

import manager.taskManager.InMemoryTaskManager;

import java.util.Objects;

public class Task {
    private int id;
    private String taskName;
    private String taskDescription;
    private Status status;

    public Task(String taskName, String taskDescription, Status status) {
        setId(InMemoryTaskManager.getId());
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
    }

    public String getTaskName() {
        return taskName;
    }

    protected void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    protected void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Status getStatus() {
        return status;
    }

    protected void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, taskName='%s', taskDescription='%s', status='%s'}", getId(), getTaskName(), getTaskDescription(), getStatus());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription);
    }
}
