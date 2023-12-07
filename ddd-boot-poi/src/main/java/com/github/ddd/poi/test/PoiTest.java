package com.github.ddd.poi.test;

import com.github.ddd.poi.excel.annotation.ExcelField;
import com.github.ddd.poi.excel.core.ExcelTool;
import lombok.Data;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 研发中心-彭幸园
 */
public class PoiTest {

    @Data
    public static class User {
        @ExcelField(name = "ID")
        private Integer id;
        @ExcelField(name = "name")
        private String name;
        @ExcelField(name = "height")
        private BigDecimal height;
    }

    public static void main(String[] args) throws Exception {
        testImport();
    }

    public static void testImport() throws IOException {
        List<User> users = ExcelTool.parseData("D:/1.xlsx", User.class);
        System.out.println(users.size());
        for (User user : users) {
            System.out.println(user);
        }
    }

    public static void testExport() throws IOException {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < 500000; i++) {
            User user = new User();
            user.setId(i);
            user.setName("测试用户" + i);
            user.setHeight(new BigDecimal("1.15"));
            userList.add(user);
        }
        Workbook workbook = ExcelTool.exportWorkbook(userList, User.class);
        workbook.write(new FileOutputStream("D:/1.xlsx"));
    }
}
