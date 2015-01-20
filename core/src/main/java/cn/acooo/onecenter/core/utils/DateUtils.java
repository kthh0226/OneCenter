package cn.acooo.onecenter.core.utils;

import java.text.SimpleDateFormat;

/**
 * Created by ly580914 on 15/1/20.
 */
public class DateUtils {
    private static SimpleDateFormat sdf;
    static {
        sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
    }
    public static String formatDate(long date){
        return sdf.format(date);
    }

}
