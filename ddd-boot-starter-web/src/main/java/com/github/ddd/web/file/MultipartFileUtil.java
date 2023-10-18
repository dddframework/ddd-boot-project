package com.github.ddd.web.file;

import com.github.ddd.common.exception.SystemException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author ranger
 */
public class MultipartFileUtil {

    public static MultipartFile to(File file) {
        boolean f = file.isFile();
        if (!f){
            throw new SystemException("必须是文件");
        }
        try {
            String fileName = file.getName();
            return new MockMultipartFile(fileName, fileName, null, FileCopyUtils.copyToByteArray(new FileInputStream(file)));
        } catch (IOException e) {
            throw new SystemException("转换 MultipartFile error", e);
        }
    }

    public static MultipartFile to(String fileName, InputStream stream) {
        try {
            return new MockMultipartFile(fileName, fileName, null, FileCopyUtils.copyToByteArray(stream));
        } catch (IOException e) {
            throw new SystemException("转换 MultipartFile error", e);
        }
    }

    public static MultipartFile to(String fileName, byte[] bytes) {
        return new MockMultipartFile(fileName, fileName, null, bytes);
    }
}
