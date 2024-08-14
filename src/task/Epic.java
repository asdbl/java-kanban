package task;

import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtaskIdList;

    public Epic(String taskName, String taskDescription, Status status, List<Integer> subtaskIdList) {
        super(taskName, taskDescription, status);
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
        return String.format("Epic{id=%d, taskName='%s', taskDescription='%s', status=%s, subtaskIdList=%s}",
                getId(), getTaskName(), getTaskDescription(), getStatus(), getSubtaskIdList());
    }
}
