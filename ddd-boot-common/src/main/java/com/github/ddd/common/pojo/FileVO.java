package com.github.ddd.common.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ranger
 */
@Data
public class FileVO implements Serializable {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件路径
     */
    private String fileUrl;
}
