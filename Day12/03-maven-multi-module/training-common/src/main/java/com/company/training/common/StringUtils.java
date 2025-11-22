package com.company.training.common;

import org.apache.commons.lang3.StringUtils as ApacheStringUtils;

public class StringUtils {
    
    public static boolean isEmpty(String str) {
        return ApacheStringUtils.isEmpty(str);
    }
    
    public static boolean isNotEmpty(String str) {
        return ApacheStringUtils.isNotEmpty(str);
    }
    
    public static String capitalize(String str) {
        return ApacheStringUtils.capitalize(str);
    }
}

