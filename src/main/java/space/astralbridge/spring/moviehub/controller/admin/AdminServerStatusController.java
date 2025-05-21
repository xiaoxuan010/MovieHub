package space.astralbridge.spring.moviehub.controller.admin;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import space.astralbridge.spring.moviehub.common.Result;

@RestController
public class AdminServerStatusController {

    @GetMapping("/api/admin/server/status")
    public Result<Map<String, Object>> getServerStatus() {
        Map<String, Object> status = new HashMap<>();

        // 运行时间
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        long uptimeMillis = runtimeMXBean.getUptime();
        status.put("uptimeMillis", uptimeMillis);

        // 操作系统信息
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        status.put("osName", osBean.getName());
        status.put("osVersion", osBean.getVersion());
        status.put("osArch", osBean.getArch());
        status.put("availableProcessors", osBean.getAvailableProcessors());

        // 内存信息
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        status.put("memory", Map.of(
                "used", usedMemory,
                "total", totalMemory,
                "max", maxMemory));

        // 存储信息（以GB为单位）
        File root = new File(System.getProperty("user.dir")).getAbsoluteFile();
        File partition = root;
        long totalSpaceGB = partition.getTotalSpace() / (1024 * 1024 * 1024);
        long freeSpaceGB = partition.getFreeSpace() / (1024 * 1024 * 1024);
        long usedSpaceGB = totalSpaceGB - freeSpaceGB;
        status.put("storage", Map.of(
                "used", usedSpaceGB,
                "total", totalSpaceGB));

        return Result.success(status);
    }
}
