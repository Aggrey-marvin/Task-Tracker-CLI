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
import java.util.Scanner;

class App {
    private static ArrayList<String> expectedArgs = new ArrayList<String>(Arrays.asList("new-list", 
                                                                                        "task-list", 
                                                                                        "delete-list"));

    private static void printOptions() {
        System.out.println("Here are the available functions");
        System.out.println();
        System.out.println("-> task-cli new-list <name> - Create a new task list");
        System.out.println("-> task-cli task-list - List your task lists");
        System.out.println("-> task-cli delete-list <name> - Delete a task list");
        System.out.println("-> task-cli rename-list <name> - Rename a task lists");
        System.out.println();
        System.out.println("====================Task CLI \u00A9 Pearl Tech " + Year.now().getValue() + " ============");
    }

    private static void printHeader() {
        System.out.println("==================== Task CLI ==============================");
    }

    private static void printFooter() {
        System.out.println("==================== Task CLI \u00A9 Pearl Tech " + Year.now().getValue() + " ============");
    }

    private static String getUserHomeFolder() {
        String homeDirectory = System.getProperty("user.home");
        return homeDirectory;
    }

    private static File [] getTaskLists(File taskListFolder) {
        File[] taskLists = taskListFolder.listFiles();
        return taskLists;
    }

    private static void printTaskLists(File [] taskLists) {
        printHeader();
        System.out.println("Here are your task lists:");

        int counter = 1;
        for (File file: taskLists) {
            System.out.println(counter++ + ". " + file.getName());
        }
        App.printFooter();
    }

    public static void main(String [] args) {
        Scanner scan = new Scanner(System.in);

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
                String fileName = App.getUserHomeFolder() + "/task-list/" + newList.getName() + ".json";
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
                        int result = Linux.removeOtherReadRights(fileName);
                        if (result == 0) {
                            System.out.println("The task list " + newList.getName() + " has been created successfully");
                        }
                    } else {
                        System.out.println("Change the user of the file");
                        // This maybe added a later on, fingers crossed for now, hope it never happens :)
                    }
                
                } catch (IOException exception) {
                    System.out.println(exception);
                }
            } 
        } else if (args.length > 0 && args[0].equals("task-list")) {
            // Check if the user has a tasklist directory in their home directory
            File taskListFolder = new File(App.getUserHomeFolder(), "task-list");
            
            if (taskListFolder.exists()) {
                File[] taskLists = getTaskLists(taskListFolder);
                printTaskLists(taskLists);
            } else {
                System.out.println("You do not have any task lists");
            }
        } else if (args.length > 0 && args[0].equals("delete-list")) {
            if (args.length < 2) {
                System.out.println("Please enter the task list name :)");
            } else if (args.length > 2) {
                System.out.println("Please make sure the task list name is in quotes :)");
            } else {
                File taskListFolder = new File(App.getUserHomeFolder(), "task-list");
            
                if (taskListFolder.exists()) {
                    File [] taskLists = getTaskLists(taskListFolder);
                    boolean fileExists = false;

                    for (File taskList: taskLists) {
                        if (taskList.getName().equals(args[1] + ".json")) {
                            fileExists = true;
                            break;
                        }
                    }

                    if (fileExists) {
                        File fileToDelete = new File(taskListFolder.getPath() + "/" + args[1] + ".json");

                        System.out.print("Are you sure you want to delete this task list " + "(" + args[0] +"): -- Y/N: ");
                        String response = scan.nextLine();

                        if (response.toLowerCase().equals("y")) {
                            System.out.println(fileToDelete.getPath());
                            fileToDelete.delete();
                        }

                    } else {
                        System.out.println("The task list " + args[1] + " doesn't exist");
                        printTaskLists(taskLists);
                    }
                } else {
                    System.out.println("You do not have any tasks");
                }
            }
        }

        scan.close();
    } 
}