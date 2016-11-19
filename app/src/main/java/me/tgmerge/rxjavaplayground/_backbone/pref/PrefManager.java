package me.tgmerge.rxjavaplayground._backbone.pref;

import android.content.Context;

/**
 * Created by tgmerge on 11/20.
 *
 * 管理 SharedPreference
 */
public final class PrefManager {

    interface PrefItem<T> {
        T get(Context ctx);    // 从 SharedPref 取出值
    }

    /** 首次启动 */
    public static final class FirstBoot implements PrefItem<Boolean> {
        private static final String KEY = "first_boot";

        @Override
        public Boolean get(Context ctx) {
            return PrefHelper.getPref(ctx).getBoolean(KEY, true);
        }

        public void set(Context ctx, boolean value) {
            PrefHelper.getEditor(ctx).putBoolean(KEY, value);
        }
    }

    /** 用户身份 */
    public static final class InstallHash implements PrefItem<String> {
        private static final String KEY = "install_hash";

        @Override
        public String get(Context ctx) {
            return PrefHelper.getPref(ctx).getString(KEY, "");
        }

        public void generate(Context ctx) {
            PrefHelper.getEditor(ctx).putString(KEY, "some_generated_string");
        }
    }
}
