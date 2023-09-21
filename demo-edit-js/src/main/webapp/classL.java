import java.util.ArrayList;
import java.util.List;

public class TaskManagerV1 {
    private List<String> tasks;

    public TaskManagerV1() {
        tasks = new ArrayList<>();
    }

    public void addTask(String task) {
        tasks.add(task);
    }

    public void printTasks() {
        for (String task : tasks) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {
        TaskManagerV1 taskManager = new TaskManagerV1();
        taskManager.addTask("Task 1");
        taskManager.addTask("Task 2");
        taskManager.printTasks();
    }

     public void markTaskAsCompleted(String task) {
        System.out.println("Task '" + task + "' marked as completed.");
    }
}
