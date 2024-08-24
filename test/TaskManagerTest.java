import manager.taskManager.TaskManager;
import org.junit.jupiter.api.Test;

public abstract class TaskManagerTest<T extends TaskManager> {
    @Test
    public void shouldBeAddedTaskToTaskManager() {
    }

    @Test
    void shouldBeAddedEpicToTaskManager() {
    }

    @Test
    void shouldBeAddedSubtaskToTaskManager() {
    }

    @Test
    void shouldReplaceTaskToUpdateInsteadTask1() {
    }

    @Test
    void shouldReplaceEpicToUpdateInsteadEpic1() {
    }

    @Test
    void shouldReplaceSubtaskToUpdateInsteadSubtask1() {
    }

    @Test
    void shouldReturnListOfTasks() {
    }

    @Test
    void shouldReturnListOfEpics() {
    }

    @Test
    void shouldReturnListOfSubtasks() {
    }

    @Test
    void shouldReturnAllTaskEpicsAndSubtask() {
    }

    @Test
    void shouldClearInMemoryTaskManager() {
    }

    @Test
    void shouldReturnTask1ById1() {
    }

    @Test
    void shouldReturnListOfHistory() {
    }

    @Test
    void shouldReturnNullWhenGetMethodNotUsed() {
    }

    @Test
    void taskShouldBeAddedToEndOfHistoryWhenGetTwoTimes() {
    }

    @Test
    void removeFromHistoryWhenRemoveTask() {
    }

    @Test
    void shouldRemoveTaskById() {
    }

    @Test
    void shouldReturnSubtaskByEpic() {
    }

    @Test
    void shouldBeEqualIfTaskSame() {
    }

    @Test
    void shouldBeEqualIfEpicSame() {
    }

    @Test
    void shouldBeEqualIfSubtaskSame() {
    }

    @Test
    void shouldNotBeAbleToPutEpicAsSubtask() {
    }

    @Test
    void shouldNotBeAbleToMakeSubtaskAsEpic() {
    }

    @Test
    void managerShouldReturnReadyToUseTaskManager() {
    }

    @Test
    void inMemoryTaskManagerShouldAddDifferentTypesOfTasks() {
    }

    @Test
    void createdIdAndGeneratedIdShouldNotConflict() {
    }

    @Test
    void historyShouldSaveOldVersionsOfTask() {
    }

    @Test
    void shouldRemoveSubtask() {
    }

    @Test
    void deletedSubtaskShouldNotStoreOldId() {
    }

    @Test
    void epicsSubtaskIdListShouldBeEmptyWhenSubtasksDeleted() {
    }

    @Test
    void settersShouldNotAffectOnManager() {
    }

    @Test
    void shouldBeEpicStatusDONEWhenAllSubtasksAreDone() {
    }

    @Test
    void shouldBeEpicStatusNewWhenAllSubtasksAreNew() {
    }

    @Test
    void shouldBeEpicStatusInProgressWhenAllSubtasksAreInProgress() {
    }

    @Test
    void shouldReturnTrueWhenTasksOverlap() {
    }

    @Test
    void shouldBeEpicStatusInProgressWhenNotAllSubtasksAreDone() {
    }
}