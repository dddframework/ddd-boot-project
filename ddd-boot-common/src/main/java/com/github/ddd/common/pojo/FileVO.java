package com.github.ddd.common.pojo;

import lombok.Data;

/**
 * @author ranger
 */
@Data
public class FileVO {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String fileUrl;
}