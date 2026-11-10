import * as editorApi from "../src/codereview.js";

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const textL =
`import java.util.ArrayList;
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
`;

const textR =
`import java.util.ArrayList;
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
`;

const model = editorApi.newDiffModel(
    threadPool, textL, textR,
    "classL.java", "classR.java",
    "java"
);

const leftView = editorApi.newSideDiff({
    containerId: "left",
    workers: threadPool
}, true);

const rightView = editorApi.newSideDiff({
    containerId: "right",
    workers: threadPool
}, false);

leftView.setModel(model);
rightView.setModel(model);
leftView.focus();

console.log("SideDiff left.left =", leftView.left, "right.left =", rightView.left);
