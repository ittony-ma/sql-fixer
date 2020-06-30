package com.tony.com;

import org.apache.commons.lang.StringUtils;

/**
 * @author ittony.ma@gmail.com
 * @date 2020/6/28
 */
public class ParamFactory {

    public static final IParamFormat getParamInstance(String value) {

        if (!StringUtils.isEmpty(value) && !value.contains("String")) {
            return new NonStringParamFormat();
        }

        return new StringParamFormat();
    }

}
