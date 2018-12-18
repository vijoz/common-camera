package lib.android.timingbar.com.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtil
 * -----------------------------------------------------------------------------------------------------------------------------------
 * 字符串管理工具类
 *
 * @author rqmei on 2018/4/24
 */

public class StringUtils {
    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals (input))
            return true;

        for (int i = 0; i < input.length (); i++) {
            char c = input.charAt (i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (isEmpty (email))
            return false;
        return Pattern.compile (ConstUtils.REGEX_EMAIL).matcher (email).matches ();
    }

    /**
     * 判断一个url是否为图片url
     *
     * @param url
     * @return
     */
    public static boolean isImgUrl(String url) {
        if (isEmpty (url))
            return false;
        return Pattern.compile (ConstUtils.IMG_URL).matcher (url).matches ();
    }

    /**
     * 判断是否为一个合法的url地址
     *
     * @param str
     * @return
     */
    public static boolean isUrl(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_URL).matcher (str).matches ();
    }

    /**
     * 判断是否为汉字
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_CHZ).matcher (str).matches ();
    }

    /**
     * 判断是否为一个合法的手机号码
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_MOBILE_EXACT).matcher (str).matches ();
    }

    /**
     * 判断是否为一个合法的身份证号码
     *
     * @param str
     * @return
     */
    public static boolean isIdCard(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_IDCARD15).matcher (str).matches () || Pattern.compile (ConstUtils.REGEX_IDCARD18).matcher (str).matches ();
    }

    /**
     * 判断是否为一个合法的用户名
     *
     * @param str
     * @return
     */
    public static boolean isUserName(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_USERNAME).matcher (str).matches ();
    }

    /**
     * 判断是否为一个合法的ip地址
     *
     * @param str
     * @return
     */
    public static boolean isIp(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_IP).matcher (str).matches ();
    }

    /**
     * 判断是否为一个合法的yyyy-MM-dd格式的日期
     *
     * @param str
     * @return
     */
    public static boolean isDate(String str) {
        if (isEmpty (str))
            return false;
        return Pattern.compile (ConstUtils.REGEX_DATE).matcher (str).matches ();
    }

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        Matcher m = Pattern.compile (ConstUtils.REGEX_SPECIAL_CHAR).matcher (str);
        return m.find ();
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt (str);
        } catch (Exception e) {
        }
        return defValue;
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(Object obj) {
        if (obj == null)
            return 0;
        return toInt (obj.toString (), 0);
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong (obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔值
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean (b);
        } catch (Exception e) {
        }
        return false;
    }

    public static String getString(String s) {
        return s == null ? "" : s;
    }

    /***
     * 截取字符串
     *
     * @param start 从那里开始，0算起
     * @param num   截取多少个
     * @param str   截取的字符串
     * @return
     */
    public static String getSubString(int start, int num, String str) {
        if (str == null) {
            return "";
        }
        int leng = str.length ();
        if (start < 0) {
            start = 0;
        }
        if (start > leng) {
            start = leng;
        }
        if (num < 0) {
            num = 1;
        }
        int end = start + num;
        if (end > leng) {
            end = leng;
        }
        return str.substring (start, end);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf (".") > 0) {
            s = s.replaceAll ("0+?$", "");// 去掉多余的0
            s = s.replaceAll ("[.]$", "");// 如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 拼接字符串
     *
     * @param list  数据集合
     * @param split 拼接的标符
     * @return
     */
    public static String listToString(List<String> list, String split) {
        String str = "";
        if (list != null && list.size () > 0) {
            for (int i = 0; i < list.size (); i++) {
                if (i < list.size () - 1) {
                    if (i == 0) {
                        str = list.get (i) + split;
                    } else {
                        str = str + list.get (i) + split;
                    }
                } else {
                    str = str + list.get (i);
                }
            }
        }
        return str;
    }

    /**
     * 字符串装list
     *
     * @param str
     * @param split
     * @return
     */
    public static List<String> stringToList(String str, String split) {
        if (isSpecialChar (split)) {
            split = String.format ("\\%s", split);
            Log.i ("StringUtils","包含特殊字符" + split);
        }
        String[] s = str.split (split);
        List<String> list = Arrays.asList (s);
        return list;
    }

    /**
     * 文字高亮
     *
     * @param msg       总的字符串内容
     * @param filterStr 变色的内容
     * @param color     变后的颜色
     * @return
     */
    public static SpannableStringBuilder getSpannableStr(String msg, String filterStr, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder (msg);
        int indexOf = msg.indexOf (filterStr);
        while (indexOf != -1) {
            // CharSequence
            Log.i ("StringUtils","StringUtils getSpannableStr查询到索引位置~" + indexOf);
            builder.setSpan (new ForegroundColorSpan (color), indexOf, indexOf + filterStr.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            indexOf = msg.indexOf (filterStr, indexOf + 1);
        }
        return builder;
    }

    /**
     * 文字高亮
     *
     * @param msg        总的字符串内容
     * @param filterStrs 变色的内容
     * @param colors     变后的颜色
     * @return
     */
    public static SpannableStringBuilder getSpannableStr(String msg, String[] filterStrs, int[] colors) {
        SpannableStringBuilder builder = new SpannableStringBuilder (msg);
        for (int i = 0, len = filterStrs.length; i < len; i++) {
            String filterStr = filterStrs[i];
            int indexOf = msg.indexOf (filterStr);
            int colorIndex = colors.length > i ? i : colors.length - 1;
            while (indexOf != -1) {
                // CharSequence
                Log.i ("StringUtils","StringUtils getSpannableStr查询到索引位置~" + indexOf);
                builder.setSpan (new ForegroundColorSpan (colors[colorIndex]), indexOf, indexOf + filterStr.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                indexOf = msg.indexOf (filterStr, indexOf + 1);
            }
        }
        return builder;
    }
}
