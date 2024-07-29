package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIdList;

    public Epic(String taskName, String taskDescription, Status status, ArrayList<Integer> subtaskIdList) {
        super(taskName, taskDescription, status);
        this.subtaskIdList = subtaskIdList;
    }

    public Epic(Epic epic) {
        super(epic);
        this.subtaskIdList = epic.subtaskIdList;
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    @Override
    public String toString() {
        return String.format("Epic{id=%d, taskName='%s', taskDescription='%s', status=%s, subtaskIdList=%s}",
                getId(), getTaskName(), getTaskDescription(), getStatus(), getSubtaskIdList());
    }
}
