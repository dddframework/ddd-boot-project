package com.github.ddd.poi.excel.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.github.ddd.common.exception.SystemException;
import com.github.ddd.poi.excel.annotation.ExcelField;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author ranger
 */
public class ExcelTool {

    /**
     * 基于文件流
     *
     * @param file
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseData(String file, Class<T> tClass) {
        try {
            return parseData(new FileInputStream(file), tClass);
        } catch (FileNotFoundException e) {
            throw new SystemException("FileNotFoundException: " + file);
        }
    }

    /**
     * 基于输入流
     *
     * @param inputStream
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseData(InputStream inputStream, Class<T> tClass) {
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        List<Map<String, Object>> maps = reader.readAll();
        Map<String, Field> fieldMap = new HashMap<>();
        Map<String, ExcelField> excelFieldMap = new HashMap<>();
        Field[] fields = ReflectUtil.getFields(tClass);
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField != null) {
                String value = excelField.name();
                if (fieldMap.containsKey(value)) {
                    throw new RuntimeException("导入时一个类中不能出现相同的ExcelField Value:" + value);
                }
                fieldMap.put(value, field);
                excelFieldMap.put(value, excelField);
            }
        }
        List<T> result = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            T t = ReflectUtil.newInstanceIfPossible(tClass);
            for (Map.Entry<String, Field> f : fieldMap.entrySet()) {
                Field field = f.getValue();
                Object o = map.get(f.getKey());
                ExcelField excelField = excelFieldMap.get(f.getKey());
                String dateTimeFormat = excelField.dateTimeFormat();
                if (o != null && Date.class.isAssignableFrom(field.getType()) && !Date.class.isAssignableFrom(o.getClass()) && StrUtil.isNotBlank(dateTimeFormat)) {
                    try {
                        o = DateUtil.parse(o.toString(), dateTimeFormat);
                    } catch (Exception e) {
                        o = null;
                        throw new SystemException("无法解析时间字段: " + o);
                    }
                }
                ReflectUtil.setFieldValue(t, field, o);
            }
            result.add(t);
        }
        return result;
    }

    /**
     * 导出Excel
     *
     * @param collection
     * @param tClass
     * @param <T>
     */
    public static <T> Workbook exportWorkbook(List<T> collection, Class<T> tClass) {
        List<T> dataList = collection;
        if (CollUtil.isEmpty(dataList)) {
            dataList = new ArrayList<>();
            dataList.add(ReflectUtil.newInstance(tClass));
        }
        ExcelWriter excelWriter = ExcelUtil.getWriter(true);
        List<String> head = new ArrayList<>();
        List<Field> fieldList = new ArrayList<>();
        List<ExcelField> excelFieldList = new ArrayList<>();
        Field[] fields = ReflectUtil.getFields(tClass);
        for (Field field : fields) {
            ExcelField excelField = field.getAnnotation(ExcelField.class);
            if (excelField != null) {
                fieldList.add(field);
                excelFieldList.add(excelField);
                head.add(excelField.name());
            }
        }
        Map<String, CellStyle> cellStyleMap = new HashMap<>();
        Workbook workbook = excelWriter.getWorkbook();
        excelWriter.writeHeadRow(head);
        int headHeight = 1;
        for (int y = 0; y < dataList.size(); y++) {
            T t = dataList.get(y);
            for (int x = 0; x < fieldList.size(); x++) {
                Field field = fieldList.get(x);
                ExcelField excelField = excelFieldList.get(x);
                Object fieldValue = ReflectUtil.getFieldValue(t, field);
                int trueY = y + headHeight;
                excelWriter.writeCellValue(x, trueY, fieldValue);
                String format = StrUtil.isNotBlank(excelField.dataFormat()) ? excelField.dataFormat() : excelField.dateTimeFormat();
                if (StrUtil.isNotBlank(format)) {
                    CellStyle cellStyle;
                    if (cellStyleMap.get(format) != null) {
                        cellStyle = cellStyleMap.get(format);
                    } else {
                        cellStyle = workbook.createCellStyle();
                        StyleUtil.setAlign(cellStyle, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
                        StyleUtil.setBorder(cellStyle, BorderStyle.THIN, IndexedColors.BLACK);
                        DataFormat dataFormat = workbook.createDataFormat();
                        cellStyle.setDataFormat(dataFormat.getFormat(format));
                        cellStyleMap.put(format, cellStyle);
                    }
                    excelWriter.setStyle(cellStyle, x, trueY);
                }
            }
        }
        excelWriter.autoSizeColumnAll();
        return workbook;
    }


}
