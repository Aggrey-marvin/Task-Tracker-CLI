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
    } 
}