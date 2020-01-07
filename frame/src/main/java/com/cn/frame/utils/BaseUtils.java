package com.cn.frame.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.TypedValue;

import com.cn.frame.R;
import com.cn.frame.data.BaseData;
import com.cn.frame.data.bean.DoctorBean;
import com.cn.frame.data.bean.PatientBean;
import com.github.promeg.pinyinhelper.Pinyin;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author dundun
 */
public class BaseUtils {
    private static final String TAG = BaseUtils.class.getSimpleName();
    public static String filterImoji = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff" +
            "]|[\ud83e\udc00-\ud83e\udfff]|[\u2600-\u27ff]";
    private static final String REGEX_PHONE = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|" +
            "(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
    private static final String REGEX_CARD_NUM = "(^[1-8][0-7]{2}\\d{3}([12]\\d{3})" +
            "(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}([0-9Xx])$)";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM = "HH:mm";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String INIT_TIME = "00:00:00";
    private static final int BUFFER_SIZE = 1024 * 2;

    /**
     * 手机号 星号处理    asterisk（星号）
     */
    public static String asteriskUserPhone(String mobiles)
            throws NullPointerException, StringIndexOutOfBoundsException {
        if (TextUtils.isEmpty(mobiles) || mobiles.length() < BaseData.BASE_PHONE_DEFAULT_LENGTH) {
            return "";
        }
        return mobiles.substring(0, 3) + "****" + mobiles.substring(7, mobiles.length());
    }

    /**
     * 身份证 星号处理    asterisk（星号）
     */
    public static String asteriskUserCard(String idCard) throws NullPointerException,
            StringIndexOutOfBoundsException {
        if (TextUtils.isEmpty(idCard) || idCard.length() < BaseData.BASE_ID_CARD_LENGTH) {
            return "";
        }
        return idCard.substring(0, 3) + "************" + idCard.substring(15, idCard.length());
    }

    /**
     * 手机号 空格    asterisk（星号）
     */
    public static String spaceUserCard(String idCard) throws NullPointerException,
            StringIndexOutOfBoundsException {
        if (TextUtils.isEmpty(idCard) || idCard.length() < BaseData.BASE_ID_CARD_LENGTH) {
            return "";
        }
        return idCard.substring(0, 6) + " " + idCard.substring(6, 14) + " " + idCard.substring(14
                , idCard.length());
    }

    /**
     * 产生6位随机数
     */
    private static int random() {
        return (int) ((Math.random() * 9 + 1) * 100000);
    }

    /**
     * 随机数
     */
    public static String signSpan(Context context, String value1, String value2) {
        return String.format(context.getString(R.string.txt_sign), value1, random(), value2);
    }

    /**
     * session id
     */
    public static String signSpan(Context context, String value1, String value2, String value3) {
        return String.format(context.getString(R.string.txt_sign_by_session), value1, value2,
                value3);
    }

    /**
     * 网络是否可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return null != networkInfo && networkInfo.isConnected();
    }

    /**
     * 将dp值转换为像素值
     */
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据时间戳返回时间范围
     */
    public static String timeFormat(long start, long end) {
        String date = formatDate(start, YYYY_MM_DD);
        String startHour = formatDate(start, HH_MM);
        String endHour = formatDate(end, HH_MM);
        return date + " " + startHour + "-" + endHour;
    }

    public static String timeStringFormat(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            return "";
        }
        return timeFormat(date2TimeStamp(startTime, YYYY_MM_DD_HH_MM), date2TimeStamp(endTime,
                YYYY_MM_DD_HH_MM));
    }

    /**
     * 字符串转换为java.util.Date<br>
     */
    public static Date formatDate(String time, String format) {
        SimpleDateFormat formatter;
        time = time.trim();
        formatter = new SimpleDateFormat(format, Locale.getDefault());
        return formatter.parse(time, new ParsePosition(0));
    }

    /**
     * 时间戳转为北京时间
     */
    public static String formatDate(long time, String format) {
        if (format != null) {
            return new SimpleDateFormat(format, Locale.getDefault()).format(new Date(time));
        }
        return "";
    }

    /**
     * 时间戳转为北京时间
     */
    public static String formatDate(Date time, String format) {
        if (format != null) {
            return new SimpleDateFormat(format, Locale.getDefault()).format(time);
        }
        return "";
    }

    /**
     * 日期格式转为时间戳
     */
    public static long date2TimeStamp(String date, String format) {
        if (TextUtils.isEmpty(date)) {
            return 0;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断是否是手机号码
     */
    public static boolean isMobileNumber(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher phoneMatcher = Pattern.compile(REGEX_PHONE).matcher(str);
            return phoneMatcher.matches();
        }
        return false;
    }

    /**
     * 判断验证码是否合规
     */
    public static boolean isCorrectVerifyCode(String str) {
        return !TextUtils.isEmpty(str) && str.length() == BaseData.BASE_VERIFY_CODE_DEFAULT_LENGTH;
    }

    /**
     * 判断是否身份证号
     */
    public static boolean isCardNum(String str) {
        if (!TextUtils.isEmpty(str)) {
            Matcher phoneMatcher = Pattern.compile(REGEX_CARD_NUM).matcher(str);
            return phoneMatcher.matches();
        }
        return false;
    }

    public static int copy(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                SweetLog.e(TAG, e.getMessage());
            }
            try {
                in.close();
            } catch (IOException e) {
                SweetLog.e(TAG, e.getMessage());
            }
        }
        return count;
    }

    /**
     * 对数据进行排序
     *
     * @param list 要进行排序的数据源
     */
    public static void sortPatientData(List<PatientBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            PatientBean bean = list.get(i);
            String tag = Pinyin.toPinyin(bean.getName().substring(0, 1).charAt(0)).substring(0, 1);
            if (tag.matches("[A-Za-z]")) {
                bean.setIndexTag(tag.toUpperCase());
            } else {
                bean.setIndexTag("#");
            }
        }
        Collections.sort(list, (o1, o2) -> {
            if ("#".equals(o1.getIndexTag())) {
                return 1;
            } else if ("#".equals(o2.getIndexTag())) {
                return -1;
            } else {
                return o1.getIndexTag().compareTo(o2.getIndexTag());
            }
        });
    }

    /**
     * 对数据进行排序
     *
     * @param list 要进行排序的数据源
     */
    public static void sortDoctorData(List<DoctorBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            DoctorBean bean = list.get(i);
            String tag =
                    Pinyin.toPinyin(bean.getDoctorName().substring(0, 1).charAt(0)).substring(0, 1);
            if (tag.matches("[A-Za-z]")) {
                bean.setIndexTag(tag.toUpperCase());
            } else {
                bean.setIndexTag("#");
            }
        }
        Collections.sort(list, (o1, o2) -> {
            if ("#".equals(o1.getIndexTag())) {
                return 1;
            } else if ("#".equals(o2.getIndexTag())) {
                return -1;
            } else {
                return o1.getIndexTag().compareTo(o2.getIndexTag());
            }
        });
    }


    /**
     * 根据身份证的号码算出当前身份证持有者的年龄 18位身份证
     */
    public static String getAgeByCard(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() != BaseData.BASE_ID_CARD_LENGTH) {
            return "";
        }
        // 得到年份
        String year = idCard.substring(6).substring(0, 4);
        // 得到当前的系统时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        // 当前年份
        String fyear = format.format(date);
        int age = Integer.parseInt(fyear) - Integer.parseInt(year);
        if (age < 0) {
            age = 0;
        }
        return String.valueOf(age);
    }

    /**
     * 根据身份证号判断性别
     */
    public static int getSexByCard(String idCard) {
        if (TextUtils.isEmpty(idCard) || idCard.length() != BaseData.BASE_ID_CARD_LENGTH) {
            return 0;
        }
        int sex;
        if (Integer.parseInt(idCard.substring(16).substring(0, 1)) % 2 == 0) {
            sex = 2;
        } else {
            sex = 1;
        }
        return sex;
    }

    /**
     * 将秒数转为   字符串时间模式   "hh:mm:ss"
     */
    public static String getTimeMode(long time) {
        if (time <= 0) {
            return INIT_TIME;
        }
        long hour, min, sec;
        String hourStr, minStr, secStr;
        hour = time / 3600;
        min = time % 3600 / 60;
        sec = time % 3600 % 60;
        if (hour < 10) {
            hourStr = "0" + hour;
        } else {
            hourStr = "" + hour;
        }
        if (min < 10) {
            minStr = "0" + min;
        } else {
            minStr = "" + min;
        }
        if (sec < 10) {
            secStr = "0" + sec;
        } else {
            secStr = "" + sec;
        }
        return hourStr + ":" + minStr + ":" + secStr;
    }

    /**
     * 保留两位小数
     */
    public static String getPrice(long price) {
        try {
            //#.00 表示两位小数
            DecimalFormat df = new DecimalFormat("#0.00");
            return df.format(price / 100f);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
