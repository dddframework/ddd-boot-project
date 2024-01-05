package com.github.ddd.jdbc.core;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.XmlUtil;
import com.github.ddd.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ranger
 */
@Slf4j
public class AdvancedSqlFactory {

    private static final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private static final Map<String, String> SQL_MAP = new HashMap<>();

    public AdvancedSqlFactory(String[] advancedSqlLocations) {
        Resource[] resources = new Resource[0];
        if (advancedSqlLocations != null) {
            for (String advancedSqlLocation : advancedSqlLocations) {
                resources = ArrayUtil.addAll(resources, getResources(advancedSqlLocation));
            }
        }
        if (ObjectUtils.isEmpty(resources)) {
            log.warn("没有加载到 advanced sql");
        } else {
            for (Resource resource : resources) {
                if (resource == null) {
                    continue;
                }
                try {
                    parseSqlFromXml(resource);
                    log.info("解析成功 advanced sql resource: '" + resource + "'");
                } catch (Exception e) {
                    throw new SystemException("解析失败 advanced sql resource: '" + resource + "'", e);
                }
            }
        }
    }

    public String getSql(String id) {
        return SQL_MAP.get(id);
    }

    private void parseSqlFromXml(Resource resource) throws Exception {
        Document document = XmlUtil.readXML(resource.getInputStream());
        NodeList sqlNodes = document.getElementsByTagName("sql");
        for (int i = 0; i < sqlNodes.getLength(); i++) {
            Element element = (Element) sqlNodes.item(i);
            String id = element.getAttribute("id");
            String sql = element.getTextContent().trim();
            String filename = resource.getFilename();
            assert filename != null;
            String tag = filename.substring(0, filename.lastIndexOf(".")) + "#" + id;
            String target = SQL_MAP.get(tag);
            if (target != null) {
                throw new RuntimeException("检测到冲突, 标识是 " + tag);
            }
            SQL_MAP.put(tag, sql);
        }
    }

    private Resource[] getResources(String location) {
        try {
            return resourceResolver.getResources(location);
        } catch (IOException e) {
            return new Resource[0];
        }
    }
}
