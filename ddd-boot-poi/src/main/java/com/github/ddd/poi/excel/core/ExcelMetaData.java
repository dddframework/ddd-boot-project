package com.github.ddd.poi.excel.core;

import lombok.Data;

import java.util.Collection;

/**
 * @author ranger
 */
@Data
public class ExcelMetaData<T> {

    private String sheetName;
    private Collection<T> collection;
    private Class<T> tClass;
}
