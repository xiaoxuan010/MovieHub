package space.astralbridge.spring.moviehub.controller.admin;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import space.astralbridge.spring.moviehub.common.Result;
import space.astralbridge.spring.moviehub.common.ResultCode;

@RestController
@RequestMapping("/api/admin/media")
public class AdminMediaController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    private String retrievePathPrefix = "/api/guest/media/";

    @PostMapping
    public Result<String> uploadFile(@RequestParam MultipartFile file) {
        if (file.isEmpty()) {
            return Result.fail(ResultCode.VALIDATE_FAILED, "文件不能为空");
        }

        try {
            String fileName = file.getOriginalFilename();

            File targetFile = new File(System.getProperty("user.dir") + "/" + uploadDir + "/" + fileName);

            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }

            file.transferTo(targetFile);

            return Result.success(retrievePathPrefix + fileName);
        } catch (Exception e) {
            return Result.fail(ResultCode.FAILED, "文件上传失败: " + e.getMessage());
        }
    }

    @DeleteMapping
    public Result<String> deleteFile(@RequestParam String filePath) {
        if (filePath.startsWith(retrievePathPrefix)) {
            filePath = filePath.substring(retrievePathPrefix.length());
        }
        try {
            File file = new File(System.getProperty("user.dir") + "/" + uploadDir + "/" + filePath);
            if (file.exists()) {
                file.delete();
                return Result.success("文件删除成功");
            } else {
                return Result.fail(ResultCode.VALIDATE_FAILED, "文件不存在");
            }
        } catch (Exception e) {
            return Result.fail(ResultCode.FAILED, "文件删除失败: " + e.getMessage());
        }
    }
}
