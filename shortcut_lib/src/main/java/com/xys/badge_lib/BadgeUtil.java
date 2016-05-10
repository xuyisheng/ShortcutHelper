package com.xys.badge_lib;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class BadgeUtil {

    private BadgeUtil() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    /**
     * 设置Badge 目前支持Launcher:
     * <p/>
     * MIUI
     * Sony
     * Samsung
     * LG
     * HTC
     * Nova
     *
     * @param context context
     * @param count   count
     */
    public static void setBadgeCount(Context context, int count, int iconResId) {
        // TODO 生成器模式重构
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }
        if (Build.MANUFACTURER.equalsIgnoreCase("xiaomi")) {
            setBadgeOfMIUI(context, count, iconResId);
        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            setBadgeOfSony(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung") ||
                Build.MANUFACTURER.toLowerCase().contains("lg")) {
            setBadgeOfSumsung(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("htc")) {
            setBadgeOfHTC(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("nova")) {
            setBadgeOfNova(context, count);
        } else {
            Toast.makeText(context, "Not Found Support Launcher", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 设置MIUI的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfMIUI(Context context, int count, int iconResId) {
        Log.d("xys", "Launcher : MIUI");
        NotificationManager mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("title").setContentText("text").setSmallIcon(iconResId);
        Notification notification = builder.build();
        try {
            Field field = notification.getClass().getDeclaredField("extraNotification");
            Object extraNotification = field.get(notification);
            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);
            method.invoke(extraNotification, count);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNotificationManager.notify(0, notification);
    }

    /**
     * 设置索尼的Badge
     * <p/>
     * 需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" />
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfSony(Context context, int count) {
        String launcherClassName = com.xys.badge_lib.AppInfoUtil.getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", isShow);//是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", launcherClassName);//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }

    /**
     * 设置三星的Badge\设置LG的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfSumsung(Context context, int count) {
        // 获取你当前的应用
        String launcherClassName = com.xys.badge_lib.AppInfoUtil.getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    /**
     * 设置HTC的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfHTC(Context context, int count) {
        Intent intentNotification = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
        ComponentName localComponentName = new ComponentName(context.getPackageName(),
                com.xys.badge_lib.AppInfoUtil.getLauncherClassName(context));
        intentNotification.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
        intentNotification.putExtra("com.htc.launcher.extra.COUNT", count);
        context.sendBroadcast(intentNotification);

        Intent intentShortcut = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
        intentShortcut.putExtra("packagename", context.getPackageName());
        intentShortcut.putExtra("count", count);
        context.sendBroadcast(intentShortcut);
    }

    /**
     * 设置Nova的Badge
     *
     * @param context context
     * @param count   count
     */
    private static void setBadgeOfNova(Context context, int count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("tag", context.getPackageName() + "/" +
                com.xys.badge_lib.AppInfoUtil.getLauncherClassName(context));
        contentValues.put("count", count);
        context.getContentResolver().insert(Uri.parse("content://com.teslacoilsw.notifier/unread_count"),
                contentValues);
    }

    public static void setBadgeOfMadMode(Context context, int count, String packageName, String className) {
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", packageName);
        intent.putExtra("badge_count_class_name", className);
        context.sendBroadcast(intent);
    }

    /**
     * 重置Badge
     *
     * @param context context
     */
    public static void resetBadgeCount(Context context, int iconResId) {
        setBadgeCount(context, 0, iconResId);
    }
}
