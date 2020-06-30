package com.tony.com;

/**
 * @author ittony.ma@gmail.com
 * @date 2020/6/28
 */
public class NonStringParamFormat implements IParamFormat {
    @Override
    public String getValue(String value) {
        return " " + value+ " ";
    }
}
