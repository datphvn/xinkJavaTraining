package com.xink.training.day13.util;

public class TestReportManager {
    private static String currentReport;
    
    public static void startReport(String reportName) {
        currentReport = reportName;
        System.out.println("Started test report: " + reportName);
    }
    
    public static void generateReport() {
        if (currentReport != null) {
            System.out.println("Generated test report: " + currentReport);
            currentReport = null;
        }
    }
}

