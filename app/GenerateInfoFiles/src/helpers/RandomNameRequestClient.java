package app.GenerateInfoFiles.src.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The RandomNameRequestClient class provides functionality to request random names from an external API.
 */
public class RandomNameRequestClient {

    // URL for the random name request
    private String url = "https://randommer.io/api/Name?nameType=fullname&quantity=";

    // API key for authentication
    private String APIKey = "c4c1c67008d644ecb768895d595aec1f";

    /**
     * Requests random names from an external API.
     *
     * @param nameCount The number of names to request.
     * @return An array of random names.
     */
    public String[] request(int nameCount) {
        try {
            // Construct the URL for the request
            URL url = new URL(this.url + nameCount);

            // Open HTTP connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Api-Key", this.APIKey);

            // Read the response as a string
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
            reader.close();

            // Remove brackets, quotes, and split the names
            String response = responseBuilder.toString();
            response = response.replace("[", "").replace("]", "").replace("\"", "");
            return response.split(",");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0]; // Return an empty array in case of error
        }
    }
}
