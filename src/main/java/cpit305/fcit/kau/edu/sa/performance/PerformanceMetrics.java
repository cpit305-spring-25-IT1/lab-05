package cpit305.fcit.kau.edu.sa.performance;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

public class PerformanceMetrics {
    // Get memory usage in MB
    public static long getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return (totalMemory - freeMemory) / (1024 * 1024);
    }

    // Get CPU usage in percentage
    public static double getCpuUsage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            return ((com.sun.management.OperatingSystemMXBean) osBean).getProcessCpuLoad() * 100;
        }
        return -1;
    }
}