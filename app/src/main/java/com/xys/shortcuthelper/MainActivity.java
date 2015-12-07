package com.xys.shortcuthelper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xys.badge_lib.BadgeUtil;
import com.xys.shortcut_lib.FlowEntranceUtil;
import com.xys.shortcut_lib.ShortcutActivity;
import com.xys.shortcut_lib.ShortcutSuperUtils;
import com.xys.shortcut_lib.ShortcutUtils;

import java.util.List;

public class MainActivity extends Activity {

    // 快捷方式名
    private String mShortcutName = "学习工具";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void addShortcutTest(View view) {
        // 系统方式创建
        // ShortcutUtils.addShortcut(this, getShortCutIntent(), mShortcutName);

        // 创建前判断是否存在
        if (!ShortcutSuperUtils.isShortCutExist(this, mShortcutName, getShortCutIntent())) {
            ShortcutUtils.addShortcut(this, getShortCutIntent(), mShortcutName, false,
                    BitmapFactory.decodeResource(getResources(), com.xys.shortcut_lib.R.drawable.ocsplayer));
            finish();
        } else {
            Toast.makeText(this, "Shortcut is exist!", Toast.LENGTH_SHORT).show();
        }

        // 为某个包创建快捷方式
        // ShortcutSuperUtils.addShortcutByPackageName(this, this.getPackageName());
    }

    public void removeShortcutTest(View view) {
        ShortcutUtils.removeShortcut(this, getShortCutIntent(), mShortcutName);
    }

    public void updateShortcutTest(View view) {
        ShortcutSuperUtils.updateShortcutIcon(this, mShortcutName, getShortCutIntent(),
                BitmapFactory.decodeResource(getResources(), com.xys.shortcut_lib.R.mipmap.ic_launcher));
    }

    public void toggleFlowEntrance(View view) {
        FlowEntranceUtil.toggleFlowEntrance(this, ShortcutActivity.class);
    }

    private Intent getShortCutIntent() {
        // 使用MAIN，可以避免部分手机(比如华为、HTC部分机型)删除应用时无法删除快捷方式的问题
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setClass(MainActivity.this, ShortcutActivity.class);
        return intent;
    }

    public void addBadgeInIcon(View view) {
        // 添加角标测试
//        ShortcutBadger.with(getApplicationContext()).count(9);
        BadgeUtil.setBadgeCount(getApplicationContext(), 4);
    }

    public void delBadgeInIcon(View view) {
        BadgeUtil.resetBadgeCount(getApplicationContext());
    }

    /**
     * Bug利用测试,请勿滥用
     *
     * @param view view
     */
    public void madMode(View view) {
        madMode(99);
    }

    /**
     * 清除Bug角标
     *
     * @param view view
     */
    public void cleanMadMode(View view) {
        madMode(0);
    }

    /**
     * 获取所有App的包名和启动类名
     *
     * @param count count
     */
    private void madMode(int count) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < list.size(); i++) {
            ActivityInfo activityInfo = list.get(i).activityInfo;
            String activityName = activityInfo.name;
            String packageName = activityInfo.applicationInfo.packageName;
            BadgeUtil.setBadgeOfMadMode(getApplicationContext(), count, packageName, activityName);
        }
    }
}
