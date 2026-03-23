package top.tqx.workweek03.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
@RestController
@RequestMapping("/hutool")
public class HuToolController {

    @Value("${file.upload.path}")
    private String uploadPath;

    @GetMapping("/id")
    public String getId() {
        return "hutool-id:" + IdUtil.fastSimpleUUID();
    }

    @GetMapping("/md5")
    public String md5(@RequestParam String text) {
        return SecureUtil.md5(text);
    }

    @PostMapping("/upload")
    public Map<String,Object> upload(@RequestParam("file")MultipartFile file) {
        Map<String,Object> result = new HashMap<>();

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")){
                extension = FileUtil.getSuffix(originalFilename);
            }
            String uniqueFileName = IdUtil.fastSimpleUUID() + extension;
            FileUtil.mkdir(uploadPath);

            String fullPath = uploadPath + File.separator + uniqueFileName;
            FileUtil.writeBytes(file.getBytes(),fullPath);

            result.put("success",true);
            result.put("message","上传成功");
            result.put("originalFileName",originalFilename);
            result.put("uniqueFileName",uniqueFileName);
            result.put("savePath",fullPath);
            result.put("fileSize",file.getSize());
        } catch (Exception e) {
            result.put("success",false);
            result.put("message","文件上传失败"+e.getMessage());
        }

        return result;

    }
}
