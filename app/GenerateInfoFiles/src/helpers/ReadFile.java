package app.GenerateInfoFiles.src.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

/**
 * The ReadFile class provides functionality to read lines from a file.
 */
public class ReadFile {

    /**
     * Reads all lines from the specified file.
     *
     * @param filePath The path to the file to read.
     * @return A list of strings containing the lines read from the file, or an empty list if an error occurs.
     */
    public static List<String> read(Path filePath) {
        try {
            // Read all lines from the file and return them
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            // Return an empty list if an error occurs
            return Collections.emptyList();
        }
    }
}
