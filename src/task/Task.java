package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private int id;
    private String taskName;
    private String taskDescription;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String taskName, String taskDescription, Status status) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.between(startTime, LocalDateTime.now());
    }

    public Task(String taskName, String taskDescription, Status status, Duration duration, LocalDateTime startTime) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(int id, String taskName, String taskDescription, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Task task) {
        this.taskName = task.taskName;
        this.taskDescription = task.taskDescription;
        this.status = task.status;
        this.id = task.id;
        this.duration = task.duration;
        this.startTime = task.startTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    @Override
    public String toString() {
        return String.format("Task{id=%d, taskName='%s', taskDescription='%s', status='%s', -," +
                        " startTime='%s', duration='%s', endTime='%s'}",
                getId(), getTaskName(), getTaskDescription(),
                getStatus(), getStartTime(), getDuration(), getEndTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskName, task.taskName) && Objects.equals(taskDescription, task.taskDescription) &&
                Objects.equals(status, task.status) && id == task.id && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskName, taskDescription, id, status, startTime);
    }


    @Override
    public int compareTo(Task o) {
        return startTime.compareTo(o.startTime);
    }
}
