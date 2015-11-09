package com.xys.shortcuthelper;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.xys.shortcut_lib.FlowEntranceUtil;
import com.xys.shortcut_lib.ShortcutActivity;
import com.xys.shortcut_lib.ShortcutSuperUtils;
import com.xys.shortcut_lib.ShortcutUtils;

public class MainActivity extends AppCompatActivity {

    // 快捷方式名
    private String mShortcutName = "Test";

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
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
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
}
