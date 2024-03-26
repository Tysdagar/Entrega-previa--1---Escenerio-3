/**
 * The GenerateInfoFilesApplication class contains the main method to demonstrate the functionality of the GenerateInfoFiles class.
 * It creates an instance of GenerateInfoFiles, generates sample product, vendor, and sales information, and demonstrates their usage.
 */
package app.GenerateInfoFiles;

import app.GenerateInfoFiles.src.GenerateInfoFiles;

public class GenerateInfoFilesApplication {
    /**
     * The main method serves as the entry point to the application.
     * It creates an instance of GenerateInfoFiles, generates sample product, vendor, and sales information,
     * and demonstrates their usage.
     *
     * @param args The command line arguments (not used in this application).
     */
    public static void main(String[] args) {
        // Create an instance of GenerateInfoFiles
        GenerateInfoFiles infoFilesClient = new GenerateInfoFiles();

        // Generate sample product information
        infoFilesClient.createProductsFile(10);

        // Generate sample vendor information
        infoFilesClient.createSalesManInfoFile(10);

        // Generate sample sales information for a specific vendor
        infoFilesClient.createSalesMenFile(8, "Aura", 2371218292L);
    }
}
