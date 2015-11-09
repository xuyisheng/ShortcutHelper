# ShortcutLib使用指南

本项目目前还在测试阶段，请大家多提issue，共同完善。

## 项目意义

快速使用shortcut，避免各种ROM适配导致的各种问题。

## 项目可用功能API

### 增加快捷方式

``` java
    /**
     * 添加快捷方式
     *
     * @param context      context
     * @param actionIntent 要启动的Intent
     * @param name         name
     * @param allowRepeat  是否允许重复
     * @param iconBitmap   快捷方式图标
     */
    public static void addShortcut(Context context, Intent actionIntent, String name,
                                   boolean allowRepeat, Bitmap iconBitmap)
```

### 判断快捷方式是否存在

基础方式

``` java
    /**
     * 判断快捷方式是否存在
     * <p/>
     * 检查快捷方式是否存在 <br/>
     * <font color=red>注意：</font> 有些手机无法判断是否已经创建过快捷方式<br/>
     * 因此，在创建快捷方式时，请添加<br/>
     * shortcutIntent.putExtra("duplicate", false);// 不允许重复创建<br/>
     * 最好使用{@link #isShortCutExist(Context, String, Intent)}
     * 进行判断，因为可能有些应用生成的快捷方式名称是一样的的<br/>
     *
     * @param context context
     * @param title   快捷方式名
     * @return 是否存在
     */
    public static boolean isShortCutExist(Context context, String title)
```

严格方式（增加Intent的检查）

``` java
    /**
     * 判断快捷方式是否存在
     * <p/>
     * 不一定所有的手机都有效，因为国内大部分手机的桌面不是系统原生的<br/>
     * 更多请参考{@link #isShortCutExist(Context, String)}<br/>
     * 桌面有两种，系统桌面(ROM自带)与第三方桌面，一般只考虑系统自带<br/>
     * 第三方桌面如果没有实现系统响应的方法是无法判断的，比如GO桌面<br/>
     *
     * @param context context
     * @param title   快捷方式名
     * @param intent  快捷方式Intent
     * @return 是否存在
     */
    public static boolean isShortCutExist(Context context, String title, Intent intent)
```

### 更新快捷方式

``` java
    /**
     * 更新桌面快捷方式图标，不一定所有图标都有效(有可能需要系统权限)
     *
     * @param context context
     * @param title   快捷方式名
     * @param intent  快捷方式Intent
     * @param bitmap  快捷方式Icon
     */
    public static void updateShortcutIcon(Context context, String title, Intent intent, Bitmap bitmap)
```

> 需要注意的是，更新快捷方式在很多手机上都不能生效，需要系统权限。可以通过先删除、再新增的方式来实现。

### 为任意PackageName的App添加快捷方式

``` java
    /**
     * 为任意PackageName的App添加快捷方式
     *
     * @param context context
     * @param pkg     待添加快捷方式的应用包名
     * @return 返回true为正常执行完毕
     */
    public static boolean addShortcutByPackageName(Context context, String pkg)
```

### 移除快捷方式

``` java
    /**
     * 移除快捷方式
     *
     * @param context      context
     * @param actionIntent 要启动的Intent
     * @param name         name
     */
    public static void removeShortcut(Context context, Intent actionIntent, String name)
```

### 显示隐藏Launcher入口

``` java
    /**
     * 显示\隐藏Launcher入口
     *
     * @param context       context
     * @param launcherClass launcherClass
     */
    public static void toggleFlowEntrance(Context context, Class launcherClass)
```

使用Launcher入口需要在AndroidMainifest文件中注册新增的入口Activity，例如：

``` xml
<activity android:name=".MainActivity">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>

<activity
    android:name="com.xxx.xxxxx"
    android:theme="@style/Base.Theme.AppCompat.Dialog">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## 使用示例

``` java
    public void addShortcutTest(View view) {
        // 系统方式创建
        // ShortcutUtils.addShortcut(this, getShortCutIntent(), mShortcutName);

        // 创建前判断是否存在
        if (!ShortcutSuperUtils.isShortCutExist(this, mShortcutName, getShortCutIntent())) {
            ShortcutUtils.addShortcut(this, getShortCutIntent(), mShortcutName, false,
                    BitmapFactory.decodeResource(getResources(), com.hujiang.hj_shortcut_lib.R.drawable.ocsplayer));
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
                BitmapFactory.decodeResource(getResources(), com.hujiang.hj_shortcut_lib.R.mipmap.ic_launcher));
    }

    public void toggleFlowEntrance(View view) {
        FlowEntranceUtil.toggleFlowEntrance(this, HJShortcutActivity.class);
    }

    private Intent getShortCutIntent() {
        // 使用MAIN，可以避免部分手机(比如华为、HTC部分机型)删除应用时无法删除快捷方式的问题
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setClass(MainActivity.this, HJShortcutActivity.class);
        return intent;
    }
```