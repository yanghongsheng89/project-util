package com.yang.util;

import java.util.*;

public class ResourceUtil {

    public static Properties getProperty(String baseName){

        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle(baseName);
        Properties properties = new Properties();
        Set<String> keySet = resourceBundle.keySet();
        for (String s : keySet) {
            properties.setProperty(s,resourceBundle.getString(s));
        }
        return properties;
    }
}