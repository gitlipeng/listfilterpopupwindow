package com.link.support.util;

import android.content.Context;

/**
 * Created by user on 2015/12/9.
 */
public class Util {
    /**
     * 字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0 || ("null").equals(value) || ("Null").equals(value);
    }

    /**
     * 判断字符串是否为空
     *
     * @param value
     * @return
     */
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static int getImgIdByImgName(Context paramContext, String imgName) {
        int imgId = 0;
        try {
            imgId = paramContext.getResources().getIdentifier(imgName,
                    "drawable", paramContext.getPackageName());
        } catch (Exception ex) {
        }
        return imgId;
    }
}
