package top.tqx.week04.utils;

import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import top.tqx.week04.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

public class FileUploadUtil {
    private static final String UPLOAD_DIR = initUploadDir();

    /**
     * 允许上传的文件类型白名单
     */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp",
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            ".txt", ".md", ".csv",
            ".zip", ".rar", ".7z",
            ".json", ".xml"
    );

    /**
     * 初始化上传目录
     *
     * @return 上传目录
     */
//    private static String initUploadDir() {
//        try {
//            String baseUrl = ResourceUtils.getURL("classpath:").getPath();
//            Path uploadPath = Paths.get(baseUrl, "static/upload/");
//            Files.createDirectories(uploadPath);
//            String uploadDir = uploadPath.toAbsolutePath() + "/";
//            System.out.println("上传目录：" + uploadDir);
//            return uploadDir;
//        } catch (IOException e) {
//            throw new RuntimeException("创建上传目录失败", e);
//        }
//    }
    private static String initUploadDir() {
        try {
            // 获取项目根目录
            String userDir = System.getProperty("user.dir");
            Path uploadPath = Paths.get(userDir, "static/upload/");
            Files.createDirectories(uploadPath);
            String uploadDir = uploadPath.toAbsolutePath().toString() + File.separator;
            System.out.println("上传目录：" + uploadDir);
            return uploadDir;
        } catch (IOException e) {
            throw new RuntimeException("创建上传目录失败", e);
        }
    }

    /**
     * 文件上传
     *
     * @param file 上传文件
     * @return 文件名
     */
    public static String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new BusinessException("400", "文件名不能为空");
        }

        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex == -1 || lastDotIndex == originalFilename.length() - 1) {
            throw new BusinessException("400", "文件必须包含后缀名");
        }

        String suffix = originalFilename.substring(lastDotIndex).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(suffix)) {
            throw new BusinessException("400", "不支持的文件类型：" + suffix);
        }

        String fileName = UUID.randomUUID() + suffix;
        File dest = new File(UPLOAD_DIR, fileName);

        File parentDir = dest.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        file.transferTo(dest);
        System.out.println("文件上传成功：" + fileName);
        return fileName;
    }
}
