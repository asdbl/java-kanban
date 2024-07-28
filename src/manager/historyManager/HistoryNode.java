package manager.historyManager;

import task.Task;

public class HistoryNode<T> {
    Task node;
    HistoryNode<Task> next;
    HistoryNode<Task> prevNode;

    public HistoryNode(HistoryNode<Task> prevNode, Task node, HistoryNode<Task> next) {
        this.node = node;
        this.next = next;
        this.prevNode = prevNode;
    }
}
