package org.kodebetter.concurrent.performance;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import com.sun.management.OperatingSystemMXBean;

public class PerformanceMonitor {
    private final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    private final OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    public long measureTime(Runnable task) {
        long startTime = System.currentTimeMillis();
        task.run();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    public double measureCpuUsage(Runnable task) {
        double startCpuLoad = getProcessCpuLoad();
        task.run();
        double endCpuLoad = getProcessCpuLoad();
        return (endCpuLoad - startCpuLoad) * 100; // Convert to percentage
    }

    public long measureMemoryUsage(Runnable task) {
        long beforeUsedMem = getUsedMemory();
        task.run();
        long afterUsedMem = getUsedMemory();
        return afterUsedMem - beforeUsedMem;
    }

    private double getProcessCpuLoad() {
        return osBean.getProcessCpuLoad(); // Value between 0.0 and 1.0
    }

    private long getUsedMemory() {
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        return heapMemoryUsage.getUsed();
    }
}
