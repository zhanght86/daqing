package com.midas.uitls.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.midas.constant.ErrorConstant;
import com.midas.exception.ServiceException;

/**
 * 读取ini配置文件
 * 
 * @author arron
 *
 */
public class ConfigReader {
    private Map<String, Map<String, String>> map            = null;
    private String                           currentSection = null;

    public ConfigReader(String path) {
        map = new HashMap<String, Map<String, String>>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            read(reader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("IO Exception:" + e);
        }
    }

    public static ConfigReader getInstance() {
        try {
            Resource resource = new ClassPathResource("/conf/data.ini");
            ConfigReader cr = new ConfigReader(resource.getURI().getPath());
            return cr;
        } catch (Exception e) {
            throw new ServiceException(ErrorConstant.CODE1000, "文件data.ini未找到");
        }
    }

    private void read(BufferedReader reader) throws IOException {
        String line = null;
        while ((line = reader.readLine()) != null) {
            parseLine(line);
        }
    }

    private void parseLine(String line) {
        line = line.trim();
        // 此部分为注释
        if (line.matches("^\\#.*$")) {
            return;
        } else if (line.matches("^\\[\\S+\\]$")) {
            // section
            String section = line.replaceFirst("^\\[(\\S+)\\]$", "$1");
            addSection(map, section);
        } else if (line.matches("^\\S+=.*$")) {
            // key ,value
            int i = line.indexOf("=");
            String key = line.substring(0, i).trim();
            String value = line.substring(i + 1).trim();
            addKeyValue(map, currentSection, key, value);
        }
    }

    private void addKeyValue(Map<String, Map<String, String>> map, String currentSection, String key, String value) {
        if (!map.containsKey(currentSection)) {
            return;
        }
        Map<String, String> childMap = map.get(currentSection);
        childMap.put(key, value);
    }

    /**
     * * 增加Section * @param map * @param section
     */

    private void addSection(Map<String, Map<String, String>> map, String section) {
        if (!map.containsKey(section)) {
            currentSection = section;
            Map<String, String> childMap = new HashMap<String, String>();
            map.put(section, childMap);
        }
    }

    /**
     * 获取配置文件指定Section和指定子键的值
     * 
     * @param section
     * @param key
     * @return
     */

    public String get(String section, String key) {
        if (map.containsKey(section)) {
            return get(section).containsKey(key) ? get(section).get(key) : null;
        }
        return null;
    }

    public Map<String, String> get(String section) {
        return map.containsKey(section) ? map.get(section) : null;
    }

    public Map<String, Map<String, String>> get() {
        return map;
    }

    public static void main(String[] args) throws Exception {
        Resource resource = new ClassPathResource("/conf/data.ini");
        System.out.println(resource.getURI().getPath());
        ConfigReader cr = new ConfigReader(resource.getURI().getPath());
        Map<String, String> map = cr.get("R");
        System.out.println(map);
    }

}