package cpit305.fcit.kau.edu.sa.performance;


import cpit305.fcit.kau.edu.sa.MultiThreadedWordCounter;
import cpit305.fcit.kau.edu.sa.SingleThreadedWordCounter;

import java.io.*;
import java.nio.file.*;
import java.net.URL;

public class PerformanceComparison {
    /**
     * Downloads text files for benchmarking and returns their local paths
     */
    public static String[] downloadTestFiles() throws IOException {
        System.out.println("Downloading the following books:\n1. Pride and Prejudice\n2. Frankenstein\n"+
                "3. Moby Dick\n4. A Tale of Two Cities");
        String[] urls = {
                "https://www.gutenberg.org/files/1342/1342-0.txt", // Pride and Prejudice
                "https://www.gutenberg.org/files/84/84-0.txt",     // Frankenstein
                "https://www.gutenberg.org/files/2701/2701-0.txt", // Moby Dick
                "https://www.gutenberg.org/files/98/98-0.txt"      // A Tale of Two Cities
        };

        String[] localPaths = new String[urls.length];

        for (int i = 0; i < urls.length; i++) {
            String fileName = "book" + (i + 1) + ".txt";
            Path filePath = Paths.get(fileName);

            // Download the file
            URL url = new URL(urls[i]);
            try (InputStream in = url.openStream()) {
                Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
                localPaths[i] = filePath.toString();
            }
        }

        return localPaths;
    }

    /**
     * Runs performance tests for single-threaded vs multi-threaded word counting
     */
    public static void runPerformanceComparison() throws IOException, InterruptedException {
        // 1. Download test files
        System.out.println("Downloading test files...");
        String[] filePaths = downloadTestFiles();

        // 2. Measure single-threaded performance
        System.out.println("\nRunning single-threaded tests...");
        long startTimeSingle = System.currentTimeMillis();
        long beforeMemorySingle = PerformanceMetrics.getMemoryUsage();
        double beforeCpuSingle = PerformanceMetrics.getCpuUsage();

        long[] singleThreadedResults = SingleThreadedWordCounter.countWordsInFiles(filePaths);

        long endTimeSingle = System.currentTimeMillis();
        long afterMemorySingle = PerformanceMetrics.getMemoryUsage();
        double afterCpuSingle = PerformanceMetrics.getCpuUsage();

        // 3. Measure multi-threaded performance
        System.out.println("\nRunning multi-threaded tests...");
        long startTimeMulti = System.currentTimeMillis();
        long beforeMemoryMulti = PerformanceMetrics.getMemoryUsage();
        double beforeCpuMulti = PerformanceMetrics.getCpuUsage();

        long[] multiThreadedResults = MultiThreadedWordCounter.countWordsInFiles(filePaths);

        long endTimeMulti = System.currentTimeMillis();
        long afterMemoryMulti = PerformanceMetrics.getMemoryUsage();
        double afterCpuMulti = PerformanceMetrics.getCpuUsage();

        // 4. Print comparison results
        System.out.println("\nPerformance Comparison Results:");
        System.out.println("--------------------------------");
        System.out.println("Single-Threaded:");
        System.out.println("Total Time: " + (endTimeSingle - startTimeSingle) + " ms");
        System.out.println("Average Time per File: " + ((endTimeSingle - startTimeSingle) / filePaths.length) + " ms");
        System.out.println("Memory Usage: " + (afterMemorySingle - beforeMemorySingle) + " MB");
        System.out.println("CPU Usage: " + afterCpuSingle + "%");

        System.out.println("\nMulti-Threaded:");
        System.out.println("Total Time: " + (endTimeMulti - startTimeMulti) + " ms");
        System.out.println("Average Time per File: " + ((endTimeMulti - startTimeMulti) / filePaths.length) + " ms");
        System.out.println("Memory Usage: " + (afterMemoryMulti - beforeMemoryMulti) + " MB");
        System.out.println("CPU Usage: " + afterCpuMulti + "%");

        // Calculate and print accuracy
        System.out.println("\nAccuracy Check:");
        boolean accurate = true;
        for (int i = 0; i < filePaths.length; i++) {
            if (singleThreadedResults[i] != multiThreadedResults[i]) {
                accurate = false;
                System.out.println("Mismatch in file " + filePaths[i]);
                System.out.println("Single-threaded count: " + singleThreadedResults[i]);
                System.out.println("Multi-threaded count: " + multiThreadedResults[i]);
            }
        }
        System.out.println("Word count accuracy: " + (accurate ? "100%" : "Mismatch detected"));
    }
}
