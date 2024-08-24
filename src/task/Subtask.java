package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String taskDescription, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(taskName, taskDescription, status, duration, startTime);
        this.epicId = epicId;
    }


    public Subtask(Subtask subtask) {
        super(subtask);
        this.epicId = subtask.getEpicId();
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("Subtask{id=%d, taskName='%s', taskDescription='%s', status=%s, epicId=%d, startTime=%s, duration=%s, endTime=%s}",
                getId(), getTaskName(), getTaskDescription(), getStatus(), getEpicId(), getStartTime(), getDuration(), getEndTime());
    }
}
