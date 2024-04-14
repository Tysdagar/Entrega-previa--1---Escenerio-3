package app.Reports;

import app.Reports.src.ReportClient;

/**
 * This class represents the application for generating reports.
 */
public class ReportsApplication {

    /**
     * Main method to start the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Create an instance of the ReportClient class
        ReportClient reportClient = new ReportClient();

        // Generate the report using the ReportClient instance
        reportClient.generateReport();
    }
}
