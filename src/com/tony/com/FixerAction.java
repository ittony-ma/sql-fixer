package com.tony.com;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.ide.CopyPasteManager;
import org.apache.http.util.TextUtils;

import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ittony.ma@gmail.com
 * @date 2020/6/28
 */
public class FixerAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }
        SelectionModel model = mEditor.getSelectionModel();
        final String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }

        long count = Constant.SQL_START.stream().filter(key -> selectedText.contains(key)).count();
        if (count <= 0) {
            return;
        }

        String res = getSql(selectedText);

        CopyPasteManager.getInstance().setContents(new StringSelection(res));

    }

    public static String getSql(String selectedText) {
        // 获取sql
        String prepare_sql = getPrepareSql(selectedText);

        // 取参数
        List<String> vals = getValues(selectedText);

        // 替换参数值
        return getRealSql(prepare_sql, vals);
    }

    private static String getRealSql(String prepare_sql, List<String> vals) {
        for (String val : vals) {
            prepare_sql =  prepare_sql.replaceFirst("[?]", val);
        }
        return prepare_sql;
    }

    private static List<String> getValues(String selectedText) {
        String start = "Parameters: ";
        if (selectedText.indexOf(start) == -1 || selectedText.endsWith(start) || !selectedText.contains("(")) {
            return new ArrayList<>();
        }

        List<String> params = new ArrayList<>();
        selectedText = selectedText.substring(selectedText.indexOf(start) + start.length());
        if (selectedText.contains("\n")) {
            selectedText = selectedText.substring(0, selectedText.lastIndexOf("\n"));
        }

        // 参数行编码
        String encoderParams = encoderParams(selectedText);

        Arrays.stream(encoderParams.split(",")).forEach(val->{

            // 获取参数最终值
            String value = getFinalParamValue(val);
            // 参数值解码
            params.add(decoderParams(value));
        });

        return params;
    }

    private static String getFinalParamValue(String val) {
        String value = val ;
        if (val.contains("(")) {
            String keyType = val.substring(val.lastIndexOf("(") + 1, val.lastIndexOf(")")).trim();
            String origin_value = val.substring(0, val.lastIndexOf("(")).trim();
            value = ParamFactory.getParamInstance(keyType).getValue(origin_value);
        } else if (val.trim().equalsIgnoreCase("NULL")) {
            value = " null ";
        }
        return value;
    }

    private static String decoderParams(String value) {
        value = value.replace(Constant.COMMA_STR, Constant.COMMA);
        return value;
    }

    /**
     * 将参数中的逗号使用占位符替换
     * @param selectedText
     * @return
     */
    private static String encoderParams(String selectedText) {
        String result = "";

        while (selectedText.indexOf(",") != -1) {
            int commaIndex = selectedText.indexOf(",");
            String substr = selectedText.substring(0, commaIndex + 1);
            if (SchemeChecker.check(substr)) {
                result += substr;
                selectedText = selectedText.substring(substr.length());
            } else {
                selectedText = selectedText.replaceFirst(Constant.COMMA, Constant.COMMA_STR);
            }
        }

        // add last param
        result += selectedText;

        return result;
    }

    private static String getPrepareSql(String selectedText) {
        String sql_start = "Preparing: ";
        if (selectedText.indexOf(sql_start) != -1) {
            selectedText = selectedText.substring(selectedText.indexOf(sql_start) + sql_start.length());
        }

        Integer startIndex = getStartIndex(selectedText);
        String endFlag = "\n";
        if (selectedText.indexOf(endFlag) == -1) {
            return selectedText.substring(startIndex);
        }

        return selectedText.substring(startIndex, selectedText.indexOf(endFlag));
    }

    /**
     * 重新编码避免SQL_START顺序对sql开始下标的影响。
     * @param selectedText
     * @return
     */
    private static Integer getStartIndex(String selectedText) {
        String encoder = selectedText;
        for (String key : Constant.SQL_START) {
            encoder = encoder.replace(key, "__KEYS__");
        }
        return encoder.equals(selectedText) ? 0 : encoder.indexOf("__KEYS__");
    }
}
