package com.tony.com;

/**
 * @author ittony.ma@gmail.com
 * @date 2020/6/28
 */
public class StringParamFormat implements IParamFormat {
    @Override
    public String getValue(String value) {
        return " '"+value+"' ";
    }
}
