package task;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String taskName, String taskDescription, Status status, int epicId) {
        super(taskName, taskDescription, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return String.format("Subtask{id=%d, taskName='%s', taskDescription='%s', status=%s, epicId=%d}",
                getId(), getTaskName(), getTaskDescription(), getStatus(), getEpicId());
    }
}
