package com.xys.shortcut_lib;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by xuyisheng on 15/10/30.
 * Version 1.0
 */
public final class FlowEntranceUtil {

    private FlowEntranceUtil() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    /**
     * 显示\隐藏Launcher入口
     *
     * @param context       context
     * @param launcherClass launcherClass
     */
    public static void toggleFlowEntrance(Context context, Class launcherClass) {
        PackageManager packageManager = context.getPackageManager();
        ComponentName componentName = new ComponentName(context, launcherClass);
        int res = packageManager.getComponentEnabledSetting(componentName);
        if (res == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT ||
                res == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            // 隐藏应用图标
            packageManager.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
        } else {
            // 显示应用图标
            packageManager.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                    PackageManager.DONT_KILL_APP);
        }
    }
}
