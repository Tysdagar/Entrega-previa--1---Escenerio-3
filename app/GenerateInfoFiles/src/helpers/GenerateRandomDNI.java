package app.GenerateInfoFiles.src.helpers;

import java.util.Random;

/**
 * The GenerateRandomDNI class provides a method to generate a random DNI (Documento Nacional de Identidad) number.
 */
public class GenerateRandomDNI {

    /**
     * Generates a random DNI number.
     *
     * @return The randomly generated DNI number.
     */
    public static long generate() {
        Random random = new Random();

        // Create a StringBuilder to store the random number
        StringBuilder string = new StringBuilder();

        // Generate 10 random digits
        for (int i = 0; i < 10; i++) {
            // Generate a random digit between 0 and 9 and append it to the StringBuilder
            string.append(random.nextInt(10));
        }

        // Convert the StringBuilder to a long integer
        long randomDNILong = Long.parseLong(string.toString());

        return randomDNILong;
    }
}
