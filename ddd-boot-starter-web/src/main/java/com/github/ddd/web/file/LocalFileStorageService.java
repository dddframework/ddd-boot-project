package com.github.ddd.web.file;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.common.pojo.FileVO;
import com.github.ddd.web.spring.boot.autoconfigure.LocalFileConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author ranger
 */
@RequiredArgsConstructor
public class LocalFileStorageService {
    private static final String GZ_FILE_SUFFIX = "tar.gz";
    private final LocalFileConfig localFileConfig;

    /**
     * 组装目标路径 ext/2021/11/04/uuid.text
     *
     * @param name 文件名
     * @return String
     */
    private String buildTargetPath(String name) {
        String ext = FileNameUtil.extName(name);
        if (StrUtil.endWithIgnoreCase(name, GZ_FILE_SUFFIX)) {
            ext = GZ_FILE_SUFFIX;
        }
        String dateDir = DateUtil.today().replace("-", "/");
        String path = ext + "/" + dateDir;
        String newFileName = IdUtil.fastSimpleUUID() + "." + ext;
        String s = path + "/" + newFileName;
        return s.toLowerCase();
    }

    /**
     * 保存本地文件
     *
     * @param file MultipartFile
     * @return FileVO
     */
    public FileVO save(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String newName = this.buildTargetPath(originalFilename);
        FileVO vo = new FileVO();
        vo.setFileName(originalFilename);
        String targetPath = localFileConfig.getRoot() + newName;
        try {
            File dest = new File(targetPath);
            String parent = dest.getParent();
            FileUtil.mkdir(parent);
            file.transferTo(dest);
            String url = localFileConfig.getDomain() + "/" + newName;
            vo.setFileUrl(url);
            return vo;
        } catch (Exception e) {
            throw new SystemException("文件上传失败", e);
        }
    }
}
