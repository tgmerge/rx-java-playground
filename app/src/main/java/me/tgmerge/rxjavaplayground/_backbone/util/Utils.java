package me.tgmerge.rxjavaplayground._backbone.util;

import android.content.Context;
import android.content.pm.PackageManager;

import me.tgmerge.rxjavaplayground.BuildConfig;

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
}
