package com.tony.com;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang.StringUtils;

/**
 * @author ittony.ma@gmail.com
 * @desc :
 * @date 2020/6/29
 */
public class SchemeChecker {

    /**
     * 参数格式检查
     * @param content
     * @return
     */
    public static boolean check(String content) {
        String test_str = content;

        //检测是否空串
        if (StringUtils.isEmpty(test_str.trim())) {
            return true;
        }
        //检测是否String类型的参数
        if (checkStringParam(test_str)) {
            return true;
        }
        //检测是否Integer类型的参数
        if (checkIntegerParam(test_str)) {
            return true;
        }
        //检测是否Long类型的参数
        if (checkLongParam(test_str)) {
            return true;
        }
        //检测是否Boolean类型的参数
        if (checkBooleanParam(test_str)) {
            return true;
        }
        //检测是否Null参数
        if (checkNullParam(test_str)) {
            return true;
        }
        //检测是否Json类型的参数
        if (checkJsonParam(test_str)) {
            return true;
        }

        return false;
    }

    private static boolean checkBooleanParam(String test_str) {
        return test_str.endsWith("(Boolean),");
    }

    private static boolean checkNullParam(String test_str) {
        return test_str.endsWith("null,") || test_str.endsWith("NULL,");
    }

    private static boolean checkJsonParam(String test_str) {
        boolean result;

        test_str = test_str.replace(Constant.COMMA_STR, Constant.COMMA);

        if (test_str.endsWith(",")) {
            test_str = test_str.substring(0, test_str.length() - 1);
        }

        JsonElement jsonElement = null;
        try {
            jsonElement = JsonParser.parseString(test_str);
            result = true;
        } catch (Exception e) {
            result =  false;
        }
        if (jsonElement == null) {
            result =  false;
        }
        if (null != jsonElement && !jsonElement.isJsonObject()) {
            result =  false;
        }

        return result;
    }

    private static boolean checkIntegerParam(String test_str) {
        return test_str.endsWith("(Integer),");
    }

    private static boolean checkLongParam(String test_str) {
        return test_str.endsWith("(Long),");
    }

    private static boolean checkStringParam(String test_str) {
        return test_str.endsWith("(String),");
    }

}
