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
                                                                                        "delete-list",
                                                                                        "rename-list"));

    private static void printOptions() {
        System.out.println("Here are the available functions");
        System.out.println();
        System.out.println("-> task-cli new-list <name> - Create a new task list");
        System.out.println("-> task-cli task-list - List your task lists");
        System.out.println("-> task-cli delete-list <name> - Delete a task list");
        System.out.println("-> task-cli rename-list <current name> <new name> - Rename a task lists");
        System.out.println("-> task-cli add-task <task list name> <task name> - Add a task to a task list");
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
            System.out.println(counter++ + ". " + file.getName().replace(".json", ""));
        }
        App.printFooter();
    }

    private static boolean checkForFile(File [] taskLists, String fileName) {
        boolean fileExists = false;
        
        for (File taskList: taskLists) {
            if (taskList.getName().equals(fileName)) {
                fileExists = true;
                break;
            }
        }

        return fileExists;
    }

    private static boolean checkForFile(String fileName) {
        File taskListFolder = new File(App.getUserHomeFolder(), "task-list");
        if (!fileName.contains(".json")) {
            fileName = fileName + ".json";
        }
        boolean fileExists = checkForFile(getTaskLists(taskListFolder), fileName);

        return fileExists;
    }

    private static String processFileName(String rawFileName) {
        if (!rawFileName.contains(".json")) {
            return rawFileName + ".json";
        } 
        return rawFileName;
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
                    boolean fileExists = checkForFile(taskLists, args[1]);

                    if (fileExists) {
                        String fileName = args[1];
                        if (!fileName.contains(".json")) {
                            fileName = fileName + ".json";
                        }
                        File fileToDelete = new File(taskListFolder.getPath() + "/" + fileName);

                        System.out.print("Are you sure you want to delete this task list " + "(" + args[0] +"): -- Y/N: ");
                        String response = scan.nextLine();

                        if (response.toLowerCase().equals("y")) {
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
        } else if (args.length > 0 && args[0].equals("rename-list")) {
            if (args.length < 3) {
                System.out.println("Please ensure to add both the old list name and the new list name :)");
            } else if (args.length > 3) {
                System.out.println("Please make sure the task list names are in quotes :)");
            } else {
                // Check if the list exists
                File taskListFolder = new File(App.getUserHomeFolder(), "task-list");
                String fileName = args[1];
                if (!fileName.contains(".json")) {
                    fileName = fileName + ".json";
                }

                String newFileName = args[2];
                if (!newFileName.contains(".json")) {
                    newFileName = newFileName + ".json";
                }
            
                if (taskListFolder.exists()) {
                    boolean fileExists = checkForFile(getTaskLists(taskListFolder), fileName);
                    if (fileExists) {
                        File currentFile = new File(getUserHomeFolder() + "/task-list/" + fileName);
                        File newFile = new File(getUserHomeFolder() + "/task-list/" + newFileName);

                        boolean result = currentFile.renameTo(newFile);

                        if (result) {
                            System.out.println("The file " + args[1] + " has been successfully renamed to " + args[2]);
                        } else {
                            System.out.println("The operation failed :(");
                        }
                    } else {
                        System.out.println("The file does not exist");
                        File [] taskLists = getTaskLists(taskListFolder);
                        printTaskLists(taskLists);
                    }
                } else {
                    System.out.println("You do not have any task created");
                }
                // Rename the list
            }
        } else if (args.length > 0 && args[0].equals("add-task")) {
            if (args.length < 3) {
                System.out.println("Please ensure to add both the old list name and the new list name :)");
            } else if (args.length > 3) {
                System.out.println("Please make sure the task list names are in quotes :)");
            } else {
                // Check whether the task list exists
                boolean fileExists = checkForFile(args[1]);
                String fileName = processFileName(args[1]);
                File currentFile = new File(getUserHomeFolder() + "/task-list/" + fileName);
                // Check for the last item's id in the the list

                // Create and new task
                // Write the new task to the file
            }
        }

        scan.close();
    } 
}