package app.Reports.src;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This client handles the generation of reports.
 */
public class ReportClient {

    // Define the root directory for all data-related files
    private final Path dataFolder = Paths.get(System.getProperty("user.dir"), "app", "data");

    // Define the folder for storing reports within the data directory
    private final Path reportFolder = dataFolder.resolve("reports");

    // Define the file path for the current date's report CSV file within the
    // reports folder
    private final Path reportsFile = reportFolder.resolve("report_" + LocalDate.now() + ".csv");

    // Define the folder for storing sales data within the data directory
    private final Path salesFolder = dataFolder.resolve("sales");

    // Define the file path for the vendors data within the data directory
    private final Path vendorsFile = dataFolder.resolve("vendors.txt");

    // Define the file path for the products data within the data directory
    private final Path productsFile = dataFolder.resolve("products.txt");

    /**
     * Constructor for the ReportsSales class.
     * Initializes the report data folder if it doesn't exist.
     */
    public ReportClient() {
        try {
            // Check if the report folder exists
            if (!Files.exists(reportFolder)) {
                // If not, create the report folder
                System.out.println("Creating report data folder...");
                Files.createDirectories(reportFolder);
                System.out.println("Report folder created.");
            } else {
                // If the report folder already exists, print a message
                System.out.println("Report folder already exists.");
            }
        } catch (Exception e) {
            // If an exception occurs during folder creation, handle the error
            handleError("Error creating report folder", e);
        }
    }

    /**
     * Handles error messages by printing them to the standard error stream.
     *
     * @param message The error message to be displayed.
     * @param e       The exception causing the error.
     */
    private void handleError(String message, Exception e) {
        // Print the error message along with the exception message to the standard
        // error stream
        System.err.println(message + ": " + e.getMessage());
    }

    /**
     * Reads the lines of a file and returns them as a list of strings.
     *
     * @param filePath The path of the file to read.
     * @return A list of strings representing the lines of the file.
     */
    private List<String> readFile(Path filePath) {
        // Create a list to store the lines of the file
        List<String> fileLineStrings = new ArrayList<>();
        try {
            // Attempt to read all lines of the file and store them in the list
            fileLineStrings = Files.readAllLines(filePath);
        } catch (IOException e) {
            // If an IOException occurs during file reading, handle the error
            handleError("Error reading file: ", e);
        }
        // Return the list of file lines, which may be empty if an error occurred
        return fileLineStrings;
    }

    /**
     * Generates a CSV file from the provided data.
     *
     * @param reportData The list of vendorSales representing the content of the CSV
     *                   file.
     */
    public void generateCSV(List<String> reportData) {
        try {
            // Write the provided data to the CSV file specified by reportsFile
            Files.write(reportsFile, reportData, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            // If an IOException occurs during file writing, handle the error
            handleError("Error generating CSV file", e);
        }
    }

    /**
     * Generates a vendors sales report by processing sales data and calculating
     * total sales for each vendor.
     * The report is then sorted by total sales in descending order and saved as a
     * CSV file.
     */
    public void generateReport() {
        // Inform the start of the report generation process
        System.out.println("Generating vendors sales report...");

        try {
            // Initialize lists to store sales data, vendors, and products
            List<String[]> allSalesData = new ArrayList<>();
            List<String> vendors = readFile(vendorsFile); // Read vendor data from file
            List<String> products = readFile(productsFile); // Read product data from file

            // Read sales data from files and store it in allSalesData list
            try (DirectoryStream<Path> salesStream = Files.newDirectoryStream(salesFolder, "*.txt")) {
                for (Path salesFile : salesStream) {
                    // Read lines from sales file and convert them to string arrays
                    List<String> saleLines = Files.readAllLines(salesFile);
                    allSalesData.add(saleLines.toArray(new String[0]));
                }
            }

            // Initialize list to store total sales for each vendor
            List<Map<String, Integer>> vendorsTotalSalesList = new ArrayList<>();

            // Process each sale data
            for (String[] saleData : allSalesData) {
                // Extract the DNI from the sale data
                long DNI = Long.parseLong(saleData[0].split(";")[1]);
                String vendorName = null; // Initialize variable to store vendor name
                int totalSales = 0; // Initialize variable to store total sales for the vendor

                // Find the vendor name corresponding to the DNI
                for (String vendor : vendors) {
                    String[] vendorData = vendor.split(";");
                    if (Long.parseLong(vendorData[1]) == DNI) {
                        vendorName = vendorData[2] + "_" + vendorData[3];
                        break;
                    }
                }

                // Process each sale made by the vendor
                for (String sale : Arrays.copyOfRange(saleData, 1, saleData.length)) {
                    String[] saleComponents = sale.split(";");
                    String productID = saleComponents[0];
                    int productQuantity = Integer.parseInt(saleComponents[1]);

                    // Find the price of the product and calculate the total sales for the vendor
                    for (String product : products) {
                        String[] productData = product.split(";");
                        if (productData[0].equals(productID)) {
                            totalSales += Integer.parseInt(productData[2]) * productQuantity;
                            break;
                        }
                    }
                }

                // Add vendor's total sales to the list
                Map<String, Integer> vendorTotalSales = new HashMap<>();
                vendorTotalSales.put(vendorName, totalSales);
                vendorsTotalSalesList.add(vendorTotalSales);
            }

            // Sort vendors by total sales
            vendorsTotalSalesList.sort(Comparator.comparingInt(o -> o.values().iterator().next()));

            // Reverse the list to get it in descending order
            Collections.reverse(vendorsTotalSalesList);

            // Generate sales report lines
            List<String> vendorsSalesLines = new ArrayList<>();
            for (Map<String, Integer> vendorTotalSales : vendorsTotalSalesList) {
                Map.Entry<String, Integer> entry = vendorTotalSales.entrySet().iterator().next();
                vendorsSalesLines.add(entry.getKey() + ";" + entry.getValue() + ",");
            }

            // Generate CSV file
            generateCSV(vendorsSalesLines);

            // Inform the successful generation of the report
            System.out.println("Vendors sales report generated successfully at " + reportsFile + "!");

        } catch (IOException e) {
            // Handle IOException during report generation
            handleError("Error while generating vendors sales report: ", e);
        }
    }

}
