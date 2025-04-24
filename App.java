import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;

class App {
    private static ArrayList<String> expectedArgs = new ArrayList<String>(Arrays.asList("new-list", "task-list", "delete-task"));

    private static void printOptions() {
        System.out.println("Here are the available functions");
        System.out.println();
        System.out.println("-> task-cli new-list <name> - Create a new task list");
        System.out.println("-> task-cli task-list - List your task lists");
        System.out.println("-> task-cli delete-list <id> - List your task lists");
        System.out.println();
        System.out.println("====================Task CLI \u00A9 Pearl Tech " + Year.now().getValue() + " ============");
    }

    public static void main(String [] args) {
        // Validations
        if (args.length == 0) {
            System.out.println("====================Welcome to Task CLI====================");
            printOptions();
        } else if (!expectedArgs.contains(args[0])) {
            System.out.println("====================Task CLI====================");
            System.out.println("Invalid option :(");
            printOptions();
        }

        if (args.length > 0 && args[0].equals("new-list")) {
            if (args.length != 2) {
                System.out.println("Please enter the task list name :)");
            }

            // Creating a task list
            // 1. Get the current user 
            System.out.println(System.getProperty("user.id"));
            // 2. Create the task list
            // 3. Convert the task list into a json file for storage
            // 4. Create a folder in the user's home directory for tasklist-cli
            // 5. Store the task list in the tasklist-cli directory in the user's home directory
            // 6. Make sure that only this users can access the task list, that is only this user has rw rights to the file
            // 7. Add metadata to the file
        }
    } 
}