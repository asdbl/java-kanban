package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIdList;
    private LocalDateTime endTime;

    public Epic(String taskName, String taskDescription, Status status, List<Integer> subtaskIdList) {
        super(taskName, taskDescription, status);
        this.subtaskIdList = subtaskIdList;
        this.endTime = LocalDateTime.now();
    }

    public Epic(String taskName, String taskDescription, Status status, List<Integer> subtaskIdList,
                Duration duration, LocalDateTime startTime) {
        super(taskName, taskDescription, status, duration, startTime);
        this.subtaskIdList = subtaskIdList;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtaskIdList = epic.subtaskIdList;
        this.endTime = epic.endTime;
    }

    public List<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("Epic{id=%d, taskName='%s', taskDescription='%s', status=%s, subtaskIdList=%s," +
                        " startTime=%s, duration=%s, endTime=%s}",
                getId(), getTaskName(), getTaskDescription(), getStatus(),
                getSubtaskIdList(), getStartTime(), getDuration(), getEndTime());
    }
}