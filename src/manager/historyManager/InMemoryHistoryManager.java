package manager.historyManager;

import task.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, HistoryNode<Task>> historyMap;
    private HistoryNode<Task> currentNode;
    private HistoryNode<Task> lastNode;
    private HistoryNode<Task> firstNode;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        removeNode(historyMap.remove(task.getId()));
        linkLast(task);
        historyMap.put(task.getId(), lastNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.remove(id));
    }

    @Override
    public void clear() {
        currentNode = firstNode;
        while (currentNode != null) {
            removeNode(currentNode);
            currentNode = currentNode.next;
        }
        historyMap.clear();
    }

    private void linkLast(Task task) {
        HistoryNode<Task> l = lastNode;
        HistoryNode<Task> newNode = new HistoryNode<>(l, task, null);
        if (firstNode == null) {
            firstNode = newNode;
        } else {
            lastNode.next = newNode;
        }
        lastNode = newNode;
    }

    private List<Task> getTask() {
        List<Task> taskList = new LinkedList<>();
        currentNode = firstNode;
        while (currentNode != null) {
            taskList.add(currentNode.node);
            currentNode = currentNode.next;
        }
        return taskList;
    }

    private void removeNode(HistoryNode<Task> node) {
        if (node == null) {
            return;
        }
        HistoryNode<Task> prev = node.prevNode;
        HistoryNode<Task> next = node.next;
        if (prev != null) {
            prev.next = next;
        } else
            firstNode = next;
        if (next != null) {
            next.prevNode = prev;
        } else
            lastNode = prev;
    }

    static class HistoryNode<T extends Task> {
        private final Task node;
        private HistoryNode<Task> next;
        private HistoryNode<Task> prevNode;

        private HistoryNode(HistoryNode<Task> prevNode, Task node, HistoryNode<Task> next) {
            this.node = node;
            this.next = next;
            this.prevNode = prevNode;
        }
    }
}