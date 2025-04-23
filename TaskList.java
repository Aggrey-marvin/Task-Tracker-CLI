import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> [] tasks;
    private int userId;
    private String userName;
    private String alias;

    public ArrayList<Task>[] getTasks() {
        return tasks;
    }
    public int getUserId() {
        return userId;
    }
    public String getUserName() {
        return userName;
    }
    public String getAlias() {
        return alias;
    }
}
