package top.niunaijun.blackboxa.view.main;

import static top.niunaijun.blackboxa.Jianjiao.getFloatPermission;
import static top.niunaijun.blackboxa.Jianjiao.getStoragePermission;
import static top.niunaijun.blackboxa.MyGlobalVar.devMode;
import static top.niunaijun.blackboxa.MyGlobalVar.preferences;
import static top.niunaijun.blackboxa.node.AccUtils.isAccessibilityServiceOn;
import static top.niunaijun.blackboxa.node.AccUtils.printLogMsg;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.context;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.initDisplay;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.mainActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.preference.PreferenceManager;

import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.entity.pm.InstallResult;
import top.niunaijun.blackboxa.MyBoard;
import top.niunaijun.blackboxa.MyGlobalVar;
import top.niunaijun.blackboxa.R;
import top.niunaijun.blackboxa.app.AppManager;
import top.niunaijun.blackboxa.databinding.ActivityMyBinding;
import top.niunaijun.blackboxa.node.GlobalVariableHolder;
import top.niunaijun.blackboxa.node.utils.FileUtils;
import top.niunaijun.blackboxa.view.base.LoadingActivity;

public class MyActivity extends LoadingActivity {
    public static final String TAG = MyGlobalVar.TAG;
    private static ActivityMyBinding mBinding;
    private static BlackBoxCore core = BlackBoxCore.get();
    private static BroadcastReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 实现从SharedPreferences中读取布尔值的逻辑
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mBinding = ActivityMyBinding.inflate(getLayoutInflater());
        //ui初始化
        initToolbar(mBinding.toolbarLayout.toolbar, R.string.app_name, false, () -> null);
        setContentView(mBinding.getRoot());
        mBinding.init.setOnClickListener(v -> init());
        mBinding.start.setOnClickListener(v -> start());
        mBinding.resetApp.setOnClickListener(v -> {
            core.clearPackage("com.xunmeng.pinduoduo", 0);
        });
        mBinding.userId.setText(preferences.getString("userId", "18668561044"));
        mBinding.fkWait.setText(preferences.getString("fkWait", "10"));
        mBinding.taskWait.setText(preferences.getString("taskWait", "20"));

        //全局初始化
        GlobalVariableHolder.context = this;
        GlobalVariableHolder.mainActivity = this;
        mReceiver = new MyBoard();
        // 创建IntentFilter并添加action
        IntentFilter filter = new IntentFilter("com.jianjiao.test.PDDGUANGBO");
        // 注册BroadcastReceiver
        registerReceiver(mReceiver, filter);
    }

    public void init() {
        showLoading();
        core.setXPEnable(true);
        AppManager.getMBlackBoxLoader().invalidHideXposed(true);
        AppManager.getMBlackBoxLoader().invalidHideRoot(true);
        initDisplay();//初始化屏幕信息
        getFloatPermission();//初始化悬浮窗权限
        //初始化无障碍服务
        if (!isAccessibilityServiceOn()) {
            printLogMsg("请开启无障碍服务", 0);
            Toast.makeText(context, "请开启无障碍服务", Toast.LENGTH_SHORT).show();
            mainActivity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
        //初始化拼多多
        if (!core.isInstalled("com.xunmeng.pinduoduo", 0)) {
            //core.installPackageAsUser("com.xunmeng.pinduoduo", 0);
            File duoduo = FileUtils.getApkFileFromAssets("duoduo.apk");
            core.installPackageAsUser(duoduo, 0);
        }

        //初始化xposed模块
        if (!core.isInstalledXposedModule("com.jianjiao.duoduo")) {
            //core.installXPModule("com.jianjiao.duoduo");
            File xpmodule = FileUtils.getApkFileFromAssets("xpmodule.apk");
            core.installXPModule(xpmodule);

        }
        core.setModuleEnable("com.jianjiao.duoduo", true);
        hideLoading();
    }

    public Boolean init1() {
        showLoading();
        if (mBinding.userId.getText().length() > 0) {
            //保存用户信息
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userId", mBinding.userId.getText().toString());
            editor.apply();
        } else {
            mBinding.userId.setText(preferences.getString("userId", "18668561044"));
        }
        if (mBinding.fkWait.getText().length() > 0) {
            //保存用户信息
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("fkWait", mBinding.fkWait.getText().toString());
            editor.apply();
        } else {
            mBinding.fkWait.setText(preferences.getString("fkWait", "10"));
        }
        if (mBinding.taskWait.getText().length() > 0) {
            //保存用户信息
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("taskWait", mBinding.taskWait.getText().toString());
            editor.apply();
        } else {
            mBinding.taskWait.setText(preferences.getString("taskWait", "20"));
        }
        Toast.makeText(MyActivity.this, "开始初始化", Toast.LENGTH_SHORT).show();
        //检测存储权限
        getStoragePermission();
        //初始化拼多多
        if (!core.isInstalled("com.xunmeng.pinduoduo", 0)) {
            InstallResult installResult = core.installPackageAsUser("com.xunmeng.pinduoduo", 0);
            if (!installResult.success) {
                //安装失败
                Toast.makeText(MyActivity.this, "多多克隆失败:" + installResult.msg, Toast.LENGTH_SHORT).show();
                hideLoading();
                return false;
            }
        }
        core.setXPEnable(true);
        //初始化xposed模块
        if (!core.isInstalledXposedModule("com.jianjiao.duoduo")) {
            File file = FileUtils.getApkFileFromAssets("xpmodule.apk");
            InstallResult installResult = core.installXPModule(file);
            if (!installResult.success) {
                //安装失败
                Toast.makeText(MyActivity.this, "插件克隆失败:" + installResult.msg, Toast.LENGTH_SHORT).show();
                hideLoading();
                return false;
            }
        }
        //启用模块
        core.setModuleEnable("com.jianjiao.duoduo", true);
        if (devMode) {
            //开发者模式

        } else {

        }
        initDisplay();//初始化屏幕信息
        getFloatPermission();//初始化悬浮窗权限
        //初始化无障碍服务
        if (!isAccessibilityServiceOn()) {
            printLogMsg("请开启无障碍服务", 0);
            Toast.makeText(context, "请开启无障碍服务", Toast.LENGTH_SHORT).show();
            mainActivity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            hideLoading();
            return false;
        }
        Toast.makeText(MyActivity.this, "初始化完成，可以启动了:", Toast.LENGTH_SHORT).show();
        hideLoading();
        return true;
    }

    public void start() {
        //Toast.makeText(MyActivity.this, "启动", Toast.LENGTH_SHORT).show();
        boolean result = core.launchApk("com.xunmeng.pinduoduo", 0);
        if (!result) {
            Toast.makeText(MyActivity.this, "启动失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onRestart() {
        super.onDestroy();
        Log.d(TAG, "onRestart: 界面显示");
        //判断无障碍权限，判断悬浮窗权限
        if (!isAccessibilityServiceOn()) {
            printLogMsg("请开启无障碍服务", 0);
            Toast.makeText(context, "请开启无障碍服务", Toast.LENGTH_SHORT).show();
            mainActivity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
        if(XXPermissions.isGranted(context, Permission.SYSTEM_ALERT_WINDOW)){
            getFloatPermission();
        };
    }
}
