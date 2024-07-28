package manager.historyManager;

import task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, HistoryNode<Task>> historyMap;
    HistoryNode<Task> lastNode;
    HistoryNode<Task> currentNode;
    HistoryNode<Task> firstNode;

    public InMemoryHistoryManager() {
        historyMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {

        if (task == null) {
            return;
        }
        if (historyMap.containsKey(task.getId())) {
            removeNode(historyMap.get(task.getId()));
        }
        linkLast(task);
        historyMap.put(task.getId(), lastNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTask();
    }

    @Override
    public void remove(int id) {
        removeNode(historyMap.get(id));
        historyMap.remove(id);
    }

   private void linkLast(Task task) {
        HistoryNode<Task> l = lastNode;
        if (l == null) {
            HistoryNode<Task> newNode = new HistoryNode<>(null, task, null);
            lastNode = newNode;
            firstNode = newNode;
            return;
        }
        HistoryNode<Task> newNode = new HistoryNode<>(l, task, null);
        l.next = newNode;
        lastNode = newNode;
    }

   private ArrayList<Task> getTask() {
        ArrayList<Task> taskList = new ArrayList<>();
        currentNode = firstNode;
        while (currentNode != null) {
            taskList.add(currentNode.node);
            currentNode = currentNode.next;
        }
        return taskList;
    }

   private void removeNode(HistoryNode<Task> node) {
        if (node.prevNode != null) {
            node.prevNode.next = node.next;
        } else
            firstNode = node.next;
        if (node.next != null) {
            node.next.prevNode = node.prevNode;
        } else
            lastNode = node.prevNode;
    }

}
