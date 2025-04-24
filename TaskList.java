import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> [] tasks;
    private int userId;
    private String userName;
    private String name;

    public TaskList(int userId, String userName, String name) {
        this.userId = userId;
        this.userName = userName;
        this.name = name;
    }

    public ArrayList<Task>[] getTasks() {
        return tasks;
    }
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
