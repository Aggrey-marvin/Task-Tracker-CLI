import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;

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
            if (args.length < 2) {
                System.out.println("Please enter the task list name :)");
            } else if (args.length > 2) {
                System.out.println("Please make sure the task list name is in quotes :)");
            } else {
                // Creating a task list
                // 1. Get the current user 
                String userName = System.getProperty("user.name");
                String userId = Linux.getUserId();
                String listName = args[1];
                Linux.getUserId();
                // 2. Create the task list
                TaskList newList  = new TaskList(userId, userName, listName);
    
                // 3. Convert the task list into a json file for storage
                // a. Create the task-list folder if it does not exist
                String homeDirectory = System.getProperty("user.home");
                File taskListFolder = new File(homeDirectory, "task-list");
                
                if (!taskListFolder.exists()) {
                    taskListFolder.mkdir();
                }
    
                // b. Create the json file and give it the file name
                String fileName = homeDirectory + "/task-list/" + newList.getName() + ".json";
                File jsonFile = new File(fileName);
    
                Path filePath = Paths.get(fileName);
                if (!jsonFile.exists()) {
                    try {
                        Files.createFile(filePath);
                    } catch (IOException exception) {
                        System.out.println(exception);
                    }
                } else {
                    System.out.println("The task list already exists");
                }

                // c. Make sure that the current user is the owner of the file by checking the id and user
                FileOwnerAttributeView file = Files.getFileAttributeView(filePath, FileOwnerAttributeView.class);
                try {
                    UserPrincipal user = file.getOwner();

                    if (user.getName().equals(newList.getUserName())) {
                        System.out.println("The task list " + newList.getName() + " has been created successfully");
                    } else {
                        System.out.println("Change the user of the file");
                    }
                
                } catch (IOException exception) {
                    System.out.println(exception);
                }

                    // d. Save the file
                    // e. Display a success message for the user
                // 4. Create a folder in the user's home directory for tasklist-cli
                // 5. Store the task list in the tasklist-cli directory in the user's home directory
                // 6. Make sure that only this users can access the task list, that is only this user has rw rights to the file
                // 7. Add metadata to the file
            }

        }
    } 
}