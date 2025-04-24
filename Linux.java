import java.io.BufferedReader;
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
}
