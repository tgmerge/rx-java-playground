package me.tgmerge.rxjavaplayground._backbone.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.tgmerge.rxjavaplayground.BuildConfig;
import me.tgmerge.rxjavaplayground._backbone.network.JsonElementTypeAdapter;

/**
 * 通用静态工具类
 */
public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    /**
     * 检查是否是 debug build
     * @return 如果是 debug 版本，返回 True
     */
    public static boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * 检查应用版本号(VersionCode)
     * see: https://developer.android.com/studio/publish/versioning.html
     * @return 在 build.gradle 中的版本号。如果出错，返回 0
     */
    public static int getVersionCode(Context ctx) {
        int v = 0;
        try {
            v = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return v;
    }

    // Json 转换

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(JsonObject.class, new JsonElementTypeAdapter())
            .create();

    /** 对象 -> json字符串 */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }

    /** json字符串 -> 对象 */
    public static <T> T fromJson(String jsonStr, Class<T> cls) {
        T obj;
        try {
            obj = gson.fromJson(jsonStr, cls);
        } catch (Exception e) {
            throw new RuntimeException("Exception in fromJson(String, cls): " + e.getMessage(), e);
        }
        return obj;
    }

    /**
     * 用“先序列化成json再反序列化”的方式获取一个对象的拷贝
     * 低效，在不需要考虑性能的时候可以用
     */
    public static <T> T objCopy(Object obj, Class<T> cls) {
        return fromJson(toJson(obj), cls);
    }

    // 字符串转换

    /** 将Unix格式的时间戳（秒）转换为Date对象 */
    public static Date timestampToDate(long unixTimeStamp) {
        return new java.util.Date(unixTimeStamp*1000);
    }

    /** 将Unix格式的时间戳（秒）转换为YYYY/MM/DD格式的字符串 */
    public static String timestampToYYMMDD(long unixTimeStamp) {
        return new SimpleDateFormat("yyyy/MM/dd", Locale.SIMPLIFIED_CHINESE).format(timestampToDate(unixTimeStamp));
    }

    /** 将字符串转换为float，异常时返回默认值(defaultVal) */
    public static float parseFloatWithDefault(String number, float defaultVal) {
        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /** 将字符串转换为int，异常时返回默认值(defaultVal) */
    public static int parseIntWithDefault(String number, int defaultVal) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /** 将字符串转换为long，异常时返回默认值(defaultVal) */
    public static long parseLongWithDefault(String number, long defaultVal) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    // 单位换算

    /** PX -> DP */
    public static float px2dp(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    /** DP -> PX */
    public static int dp2px(final Context context, final float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    // 弱引用

    /**
     * 稍微不那么麻烦地从 WeakReference 中获取强引用
     *
     * 当 ref 为 null、或 ref 中存放的对象为 null 时，都会返回 null
     * 不需要再对 ref 本身做是否为空判断
     * 当然使用了 fromWeakRef(ref, cls) 之后，还需要对这个函数是否返回了 null 判断一次
     */
    @Nullable
    public static <T> T fromWeakRef(WeakReference<T> ref, Class T) {
        if (ref == null) {
            return null;
        }
        return ref.get();
    }
}
