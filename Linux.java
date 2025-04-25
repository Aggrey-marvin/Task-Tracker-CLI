import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Linux {
    public static String getUserId() {
        try {
            // Execute the command to get the UID
            ProcessBuilder processBuilder = new ProcessBuilder("id", "-u");
            Process process = processBuilder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String uid = reader.readLine();

            int exitCode = process.waitFor();
            if (exitCode == 0 && uid != null) {
                return uid;
            } else {
                return "null";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "exception";
        }
    }

    public static int removeOtherReadRights(String filePath) {
        ProcessBuilder processBuilder = new ProcessBuilder("chmod", "o-r", filePath);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return exitCode;
        } catch (IOException exception) {
            System.out.println(exception);
            return -1;
        } catch (InterruptedException exception) {
            System.out.println(exception);
            return -1;
        }
    }
}
