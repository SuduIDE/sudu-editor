import java.util.ArrayList;
import java.util.List;

public class TaskManagerV2 {
    private List<String> tasks;
    private String description;

    public TaskManagerV2(String description) {
        tasks = new ArrayList<>();
        this.description = description;
    }

    public void addTask(String task) {
        tasks.add(task);
    }

    public void removeTask(String task) {
        tasks.remove(task);
    }

    public void printTasks() {
        for (String task : tasks) {
            System.out.println(task);
        }
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static void main(String[] args) {
        TaskManagerV2 taskManager = new TaskManagerV2("Task Manager");
        taskManager.addTask("Task 1");
        taskManager.addTask("Task 2");
        taskManager.removeTask("Task 1");
        taskManager.setDescription("Updated Task Manager");
        taskManager.printTasks();
    }
}
