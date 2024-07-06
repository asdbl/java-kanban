package test;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.*;

import java.util.ArrayList;
import java.util.HashMap;

class InMemoryTaskManagerTest {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(tasks, epics, subtasks);

    @Test
    void shouldBeAddedTaskToTaskManager() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.add(task1);
        Assertions.assertEquals(task1, inMemoryTaskManager.getTaskById(task1.getId()));
    }

    @Test
    void shouldBeAddedEpicToTaskManager() {
        Epic epic = new Epic("task1", "task1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic);
        Assertions.assertEquals(epic, inMemoryTaskManager.getTaskById(epic.getId()));
    }

    @Test
    void shouldBeAddedSubtaskToTaskManager() {
        Epic epic = new Epic("task1", "task1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic);
        Subtask subtask = new Subtask("task1", "task1 description", Status.NEW, epic.getId());
        inMemoryTaskManager.add(subtask);
        Assertions.assertEquals(subtask, inMemoryTaskManager.getTaskById(subtask.getId()));
    }

    @Test
    void shouldReplaceTaskToUpdateInsteadTask1() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.add(task1);
        Task taskToUpdate = new Task("task2", "task2 description", Status.IN_PROGRESS);
        task1 = taskToUpdate;
        inMemoryTaskManager.update(task1);
        Assertions.assertEquals(taskToUpdate, inMemoryTaskManager.getTaskById(task1.getId()));
    }

    @Test
    void shouldReplaceEpicToUpdateInsteadEpic1() {
        Epic epic1 = new Epic("task1", "task1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic1);
        Epic epicToUpdate = new Epic("task2", "task2 description", Status.IN_PROGRESS, new ArrayList<>());
        epic1 = epicToUpdate;
        inMemoryTaskManager.update(epic1);
        Assertions.assertEquals(epicToUpdate, inMemoryTaskManager.getTaskById(epic1.getId()));

    }

    @Test
    void shouldReplaceSubtaskToUpdateInsteadSubtask1() {
        Epic epic1 = new Epic("task1", "task1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("task1", "task1 description", Status.NEW, epic1.getId());
        inMemoryTaskManager.add(subtask1);
        Assertions.assertEquals(subtask1, inMemoryTaskManager.getTaskById(subtask1.getId()));
        Subtask subtaskToUpdate = new Subtask("task2", "task2 description", Status.IN_PROGRESS, epic1.getId());
        subtask1 = subtaskToUpdate;
        inMemoryTaskManager.update(subtask1);
        Assertions.assertEquals(subtaskToUpdate, inMemoryTaskManager.getTaskById(subtask1.getId()));
    }

    @Test
    void shouldReturnListOfTasks() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Task task2 = new Task("task2", "task2 description", Status.IN_PROGRESS);
        Task task3 = new Task("task3", "task3 description", Status.IN_PROGRESS);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(task3);
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task1);
        expectedTaskList.add(task2);
        expectedTaskList.add(task3);
        Assertions.assertEquals(expectedTaskList, inMemoryTaskManager.getTasks());
    }

    @Test
    void shouldReturnListOfEpics() {
        Epic epic1 = new Epic("task1", "task1 description", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("task2", "task2 description", Status.NEW, new ArrayList<>());
        Epic epic3 = new Epic("task3", "task3 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(epic2);
        inMemoryTaskManager.add(epic3);
        ArrayList<Epic> expectedEpicList = new ArrayList<>();
        expectedEpicList.add(epic1);
        expectedEpicList.add(epic2);
        expectedEpicList.add(epic3);
        Assertions.assertEquals(expectedEpicList, inMemoryTaskManager.getEpics());
    }

    @Test
    void shouldReturnListOfSubtasks() {
        Epic epic1 = new Epic("task1", "task1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("task1", "task1 description", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("task2", "task2 description", Status.NEW, epic1.getId());
        Subtask subtask3 = new Subtask("task3", "task3 description", Status.NEW, epic1.getId());
        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.add(subtask2);
        inMemoryTaskManager.add(subtask3);
        ArrayList<Subtask> expectedSubtaskList = new ArrayList<>();
        expectedSubtaskList.add(subtask1);
        expectedSubtaskList.add(subtask2);
        expectedSubtaskList.add(subtask3);
        Assertions.assertEquals(expectedSubtaskList, inMemoryTaskManager.getSubtasks());
    }

    @Test
    void shouldReturnAllTaskEpicsAndSubtask() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, epic1.getId());
        inMemoryTaskManager.add(subtask1);
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task1);
        expectedTaskList.add(epic1);
        expectedTaskList.add(subtask1);
        Assertions.assertEquals(expectedTaskList, inMemoryTaskManager.getAll());
    }

    @Test
    void shouldClearInMemoryTaskManager() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Task task2 = new Task("task2", "task2 description", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, epic1.getId());
        inMemoryTaskManager.add(subtask1);
        inMemoryTaskManager.clear();
        Assertions.assertEquals(0, inMemoryTaskManager.getTasks().size());
        Assertions.assertEquals(0, inMemoryTaskManager.getEpics().size());
        Assertions.assertEquals(0, inMemoryTaskManager.getSubtasks().size());
    }

    @Test
    void shouldReturnTask1ById1() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.add(task1);
        Assertions.assertEquals(task1, inMemoryTaskManager.getTaskById(1));
    }

    @Test
    void shouldReturnListOfHistory() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Task task2 = new Task("task2", "task2 description", Status.NEW);
        Task task3 = new Task("task3", "task3 description", Status.NEW);
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(task2);
        inMemoryTaskManager.add(task3);
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task1);
        expectedTaskList.add(task2);
        expectedTaskList.add(task3);
        inMemoryTaskManager.getTaskById(task1.getId());
        inMemoryTaskManager.getTaskById(2);
        inMemoryTaskManager.getTaskById(3);
        Assertions.assertEquals(expectedTaskList, inMemoryTaskManager.getHistory());
    }

    @Test
    void shouldRemoveTask1ById1() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.add(task1);
        Assertions.assertEquals(task1, inMemoryTaskManager.getTaskById(task1.getId()));
        inMemoryTaskManager.removeById(1);
        Assertions.assertNull(inMemoryTaskManager.getTaskById(task1.getId()));
    }

    @Test
    void shouldReturnSubtaskByEpic() {
        Epic epic = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic);
        Subtask subtask = new Subtask("subtask1", "subtask1 description", Status.NEW, epic.getId());
        inMemoryTaskManager.add(subtask);
        Assertions.assertEquals(subtask, inMemoryTaskManager.getSubtasksByEpic(epic).getFirst());
    }

    @Test
    void shouldBeEqualIfTaskSame() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Task task2 = new Task("task1", "task1 description", Status.NEW);
        Assertions.assertEquals(task1, task2);
    }

    @Test
    void shouldBeEqualIfEpicSame() {
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        Assertions.assertEquals(epic1, epic2);
    }

    @Test
    void shouldBeEqualIfSubtaskSame() {
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("subtask1", "subtask1 description", Status.NEW, epic1.getId());
        Assertions.assertEquals(subtask1, subtask2);
    }

    @Test
    void shouldNotBeAbleToPutEpicAsSubtask() {
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        Epic epic2 = new Epic("epic2", "epic2 description", Status.NEW, new ArrayList<>());
        epic1.getSubtaskIdList().add(epic2.getId());
        epic2.getSubtaskIdList().add(epic1.getId());
        inMemoryTaskManager.add(epic1);
        inMemoryTaskManager.add(epic2);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(0);
        Assertions.assertNull(inMemoryTaskManager.getTaskById(0));
        Assertions.assertEquals(1, epic1.getId());
        Assertions.assertEquals(2, epic2.getId());
        Assertions.assertEquals(expected, epic1.getSubtaskIdList());
        Assertions.assertEquals(expected, epic2.getSubtaskIdList());
    }

    @Test
    void shouldNotBeAbleToMakeSubtaskAsEpic() {
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, 1);
        inMemoryTaskManager.add(subtask1);
        Assertions.assertNotEquals(subtask1.getId(), subtask1.getEpicId());
    }

    @Test
    void managerShouldReturnReadyToUseTaskManager() {
        TaskManager taskManager = new InMemoryTaskManager(new HashMap<Integer, Task>(), new HashMap<Integer, Epic>(), new HashMap<Integer, Subtask>());
        TaskManager taskManager1 = Managers.getDefault();
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        taskManager1.add(task1);
        taskManager.add(task1);
        Assertions.assertEquals(taskManager.getTaskById(1), taskManager1.getTaskById(1));
    }

    @Test
    void inMemoryTaskManagerShouldAddDifferentTypesOfTasks() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        Epic epic1 = new Epic("epic1", "epic1 description", Status.NEW, new ArrayList<>());
        inMemoryTaskManager.add(task1);
        inMemoryTaskManager.add(epic1);
        Subtask subtask1 = new Subtask("subtask1", "subtask1 description", Status.NEW, epic1.getId());
        inMemoryTaskManager.add(subtask1);
        ArrayList<Task> expectedTaskList = new ArrayList<>();
        ArrayList<Epic> expectedEpicList = new ArrayList<>();
        ArrayList<Subtask> expectedSubtaskList = new ArrayList<>();
        expectedTaskList.add(task1);
        expectedEpicList.add(epic1);
        expectedSubtaskList.add(subtask1);
        Assertions.assertEquals(expectedTaskList, inMemoryTaskManager.getTasks());
        Assertions.assertEquals(expectedEpicList, inMemoryTaskManager.getEpics());
        Assertions.assertEquals(expectedSubtaskList, inMemoryTaskManager.getSubtasks());
    }

    @Test
    void createdIdAndGeneratedIdShouldNotConflict() {
        Task task1 = new Task("task1", "task1 description", Status.NEW);
        inMemoryTaskManager.add(task1);
        Task task2 = new Task("task2", "task2 description", Status.NEW);
        inMemoryTaskManager.add(task2);
        Assertions.assertEquals(1, inMemoryTaskManager.getTaskById(task1.getId()).getId());
        Assertions.assertEquals(2, inMemoryTaskManager.getTaskById(task2.getId()).getId());
    }

    @Test
    void historyShouldSaveOldVersionsOfTask() {
        Task task = new Task("task", "task description", Status.NEW);
        inMemoryTaskManager.add(task);
        Task expectedTask1 = task;
        inMemoryTaskManager.getTaskById(task.getId());
        Task task2 = new Task("task2", "task2 description", Status.NEW);
        task = task2;
        inMemoryTaskManager.update(task);
        inMemoryTaskManager.getTaskById(task.getId());
        Assertions.assertEquals(expectedTask1, inMemoryTaskManager.getHistory().get(0));
        Assertions.assertEquals(task, inMemoryTaskManager.getHistory().get(1));
    }

    @Test
    void maxSizeOfHistoryIs10() {
        for (int i = 0; i < 11; i++) {
            Task task = new Task("task" + i, "task" + i, Status.NEW);
            inMemoryTaskManager.add(task);
            inMemoryTaskManager.getTaskById(task.getId());
        }
        Assertions.assertEquals(10, inMemoryTaskManager.getHistory().size());
    }

    @Test
    void shouldRemoveTheOldestTaskWhenMaxSizeMoreThen10() {
        for (int i = 0; i < 11; i++) {
            Task task = new Task("task" + i, "task" + i, Status.NEW);
            inMemoryTaskManager.add(task);
            inMemoryTaskManager.getTaskById(task.getId());
        }
        Assertions.assertNotEquals(inMemoryTaskManager.getTaskById(1), inMemoryTaskManager.getHistory().get(0));
    }
}