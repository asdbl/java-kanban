package task;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIdList;

    public Epic(String taskName, String taskDescription, Status status, ArrayList<Integer> subtaskIdList) {
        super(taskName, taskDescription, status);
        this.subtaskIdList = subtaskIdList;
    }

    public ArrayList<Integer> getSubtaskIdList() {
        return subtaskIdList;
    }

    @Override
    public String toString() {
        return "Epic{"+
                "id=" + getId() +
                ", taskName='" + getTaskName() + '\'' +
                ", taskDescription='" + getTaskDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIdList=" + subtaskIdList +
                '}';
    }
}
