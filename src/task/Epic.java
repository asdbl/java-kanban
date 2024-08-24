package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIdList;
    private Duration duration;
    private LocalDateTime startTime;

    public Epic(String taskName, String taskDescription, Status status, List<Integer> subtaskIdList) {
        super(taskName, taskDescription, status);
        this.subtaskIdList = subtaskIdList;
    }

    public Epic(String taskName, String taskDescription, Status status, List<Integer> subtaskIdList, Duration duration, LocalDateTime startTime) {
        super(taskName, taskDescription, status, duration, startTime);
        this.subtaskIdList = subtaskIdList;
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtaskIdList = epic.subtaskIdList;
    }

    public List<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    @Override
    public String toString() {
        return String.format("Epic{id=%d, taskName='%s', taskDescription='%s', status=%s, subtaskIdList=%s, startTime=%s, duration=%s, endTime=%s}",
                getId(), getTaskName(), getTaskDescription(), getStatus(), getSubtaskIdList(), getStartTime(), getDuration(), getEndTime());
    }
}