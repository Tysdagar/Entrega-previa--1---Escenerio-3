package app.GenerateInfoFiles.src;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.GenerateInfoFiles.src.helpers.GenerateRandomDNI;
import app.GenerateInfoFiles.src.helpers.RandomNameRequestClient;

/**
 * This class generates information files for a business, including files for
 * sales, products, and vendors.
 */
public class GenerateInfoFiles {
    /**
     * Path to the folder where business data is stored.
     */
    private final Path dataFolderPath = Paths.get(System.getProperty("user.dir"), "app", "data");

    /**
     * Path to the folder where sales data is stored.
     */
    private final Path salesFolder = dataFolderPath.resolve("sales");

    /**
     * Path to the file containing information about products.
     */
    private final Path productsFile = dataFolderPath.resolve("products.txt");

    /**
     * Path to the file containing information about vendors.
     */
    private final Path vendorsFile = dataFolderPath.resolve("vendors.txt");

    /**
     * Constructor for the GenerateInfoFiles class.
     * It creates necessary folders for storing business data if they do not exist.
     */
    public GenerateInfoFiles() {
        try {
            // Check if the data folder exists, if not, create it
            if (!Files.exists(dataFolderPath)) {
                System.out.println("Creating business data folder...");
                Files.createDirectories(dataFolderPath);
            }
            // Check if the sales folder exists, if not, create it
            if (!Files.exists(salesFolder)) {
                System.out.println("Creating sales data folder...");
                Files.createDirectories(salesFolder);
            }
        } catch (IOException e) {
            // Handle any exceptions occurred during the process
            handleError("Error creating data folders", e);
        }
    }

    /**
     * Handles error messages by printing them to the standard error stream.
     *
     * @param message The error message to be displayed.
     * @param e       The exception causing the error.
     */
    private void handleError(String message, Exception e) {
        System.err.println(message + ": " + e.getMessage());
    }

    /**
     * Creates a file containing information about salesmen for the business.
     *
     * @param salesMenCount The number of salesmen to generate information for.
     */
    public void createSalesManInfoFile(int salesMenCount) {
        try {

            // Display message indicating the process of generating salesman file
            System.out.println("Generating salesman file for the business...");

            // Check if the vendors file already exists
            if (Files.notExists(vendorsFile)) {

                List<String> vendors = new ArrayList<>();
                RandomNameRequestClient client = new RandomNameRequestClient();

                // Request random names for salesmen
                String[] names = client.request(salesMenCount);

                // Generate information for each salesman
                for (String name : names) {

                    // Generate a random document ID for the salesman
                    long documento = GenerateRandomDNI.generate();
                    String[] nameParts = name.split(" ");
                    String firstName = nameParts[0];
                    String lastName = nameParts.length > 1 ? nameParts[1] : "";

                    // Construct the line containing salesman information
                    String line = "Cedula;" + documento + ";" + firstName + ";" + lastName;
                    vendors.add(line);
                }
                // Write the salesman information to the vendors file
                Files.write(vendorsFile, vendors, StandardOpenOption.CREATE_NEW);

                // Display success message
                System.out.println("Salesman file created successfully");
            } else {
                // Display message indicating the existence of vendors file
                System.out.println(
                        "The salesman file already exists. If you want to generate a new one, delete the previous one.");
            }
        } catch (IOException e) {
            // Handle IO exception
            handleError("Error generating salesman file", e);
        }
    }

    /**
     * Creates a file containing information about products for the business.
     *
     * @param numProducts The number of products to generate information for.
     */
    public void createProductsFile(int numProducts) {
        try {
            // Display message indicating the process of generating products file
            System.out.println("Creating business products file...");

            // Check if the products file already exists
            if (Files.notExists(productsFile)) {

                // Initialazing list of products and utility Random
                Random rand = new Random();

                final String[] PRODUCT_NAMES = { "Shirt", "Pants", "Shoes", "Hat", "Scarf", "Gloves",
                        "Shorts", "Jacket", "Dress", "Tie", "Socks", "Skirt", "Coat",
                        "Blouse", "Sweatshirt", "Suit", "Pyjamas", "Blazer", "Cap", "Boots" };

                // Final list of products
                List<String> products = new ArrayList<>();

                // Generate information for each product
                for (int i = 0; i < numProducts; i++) {

                    // Generate a random product ID and price
                    long productId = Math.round((Math.random() * 1000) * 100) / 100;
                    long price = Math.round((Math.random() * 100000) * 100) / 100;

                    // Select a random product name
                    String randomProduct = PRODUCT_NAMES[rand.nextInt(PRODUCT_NAMES.length)];

                    // Construct the line containing product information
                    String line = productId + ";" + randomProduct + ";" + price;
                    products.add(line);
                }
                // Write the product information to the products file
                Files.write(productsFile, products, StandardOpenOption.CREATE_NEW);
                System.out.println("Products file created successfully");

            } else {
                // Display message indicating the existence of vendors file
                System.out.println(
                        "The products file already exists. If you want to generate a new one, delete the previous one.");
            }
        } catch (IOException e) {
            // Handle IOException
            handleError("Error generating products file", e);
        }
    }

    /**
     * Creates a file containing information about sales made by a specific
     * salesman.
     *
     * @param randomSalesCount The number of random sales to generate.
     * @param name             The name of the salesman.
     * @param id               The ID of the salesman.
     */
    public void createSalesMenFile(int randomSalesCount, String name, long id) {
        try {
            // Read all lines from the vendors file
            List<String> vendors = Files.readAllLines(vendorsFile);
            String vendorFound = null;
            Random rand = new Random();

            // Find the vendor with the specified ID and name
            for (String vendor : vendors) {
                String[] vendorParts = vendor.split(";");
                long idVendor = Long.parseLong(vendorParts[1]);
                String nameVendor = vendorParts[2];

                if (idVendor == id && nameVendor.equals(name)) {
                    vendorFound = vendor;
                    break;
                }
            }

            // Throw exception if the specified vendor is not found
            if (vendorFound == null) {
                throw new Exception("No vendor found with the specified ID and name.");
            }

            // Print message indicating the process of creating sales file for the salesman
            System.out.println("Creating sales file for salesman " + name + "...");

            // Split the information of the found vendor
            String[] vendorFoundParts = vendorFound.split(";");
            String vendorIDType = vendorFoundParts[0];
            String vendorID = vendorFoundParts[1];
            String vendorFullNameSales = vendorFoundParts[2] + "_" + vendorFoundParts[3] + "_" + "Sales";

            // Create a list to store the sales information
            List<String> vendorSales = new ArrayList<>();
            vendorSales.add(vendorIDType + ";" + vendorID);

            // Read all lines from the products file
            List<String> products = Files.readAllLines(productsFile);

            // Generate random sales information
            for (int i = 0; i < randomSalesCount; i++) {
                String randomProduct = products.get(rand.nextInt(products.size()));
                String[] randomProductParts = randomProduct.split(";");
                String idProduct = randomProductParts[0];
                int randomQuantityProductSold = rand.nextInt(500);
                String productSoldLine = idProduct + ";" + randomQuantityProductSold;
                vendorSales.add(productSoldLine);
            }

            // Write the sales information to the sales file
            Files.write(salesFolder.resolve(vendorFullNameSales + ".txt"), vendorSales, StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE);

            // Print success message
            System.out.println("Sales file for salesman " + name + " created successfully");

        } catch (Exception e) {
            // Handle any exceptions occurred during the process
            handleError("Error generating sales file", e);
        }
    }

}
