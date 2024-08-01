package top.niunaijun.blackboxa.view.main;

import static top.niunaijun.blackboxa.Jianjiao.getFloatPermission;
import static top.niunaijun.blackboxa.MyGlobalVar.getGoodsIdForUrl;
import static top.niunaijun.blackboxa.MyGlobalVar.preferences;
import static top.niunaijun.blackboxa.node.AccUtils.isAccessibilityServiceOn;
import static top.niunaijun.blackboxa.node.AccUtils.printLogMsg;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.context;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.initDisplay;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.File;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackboxa.MyGlobalVar;
import top.niunaijun.blackboxa.app.AppManager;
import top.niunaijun.blackboxa.databinding.ActivityMyBinding;
import top.niunaijun.blackboxa.node.utils.FileUtils;
import top.niunaijun.blackboxa.view.base.LoadingActivity;

public class BlankFragment extends Fragment {
    public static final String TAG = MyGlobalVar.TAG;
    private static ActivityMyBinding mBinding;
    private static BlackBoxCore core = BlackBoxCore.get();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = ActivityMyBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();//inflater.inflate(R.layout.activity_my, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mBinding.init.setOnClickListener(v -> init());
        mBinding.userId.setText(preferences.getString("userId", "18668561044"));
        mBinding.fkWait.setText(preferences.getString("fkWait", "10"));
        mBinding.taskWait.setText(preferences.getString("taskWait", "3"));
        mBinding.costomLink.setText(preferences.getString("costomLink", "https://mobile.yangkeduo.com/mall_quality_assurance.html?_t_timestamp=comm_new_person&goods_id=123456"));
        /*mBinding.start.setOnClickListener(v -> start());
        mBinding.openVideo.setOnClickListener(v -> openVideo());
        mBinding.resetApp.setOnClickListener(v -> {
            core.clearPackage("com.xunmeng.pinduoduo", 0);
        });*/
    }

    public void showLoading() {
        ((LoadingActivity) requireActivity()).showLoading();
    }

    public void hideLoading() {
        ((LoadingActivity) requireActivity()).hideLoading();
    }

    public void init() {
        //showLoading();
        core.setXPEnable(true);
        Log.d(TAG, "是否隐藏xp: " + AppManager.getMBlackBoxLoader().hideXposed());
        Log.d(TAG, "是否隐藏xposed: " + AppManager.getMBlackBoxLoader().hideXposed());
        AppManager.getMBlackBoxLoader().invalidHideXposed(true);
        AppManager.getMBlackBoxLoader().invalidHideRoot(true);
        initDisplay();//初始化屏幕信息

        if (!MyGlobalVar.devMode) {
            getFloatPermission();//初始化悬浮窗权限
            //初始化无障碍服务
            if (!isAccessibilityServiceOn()) {
                printLogMsg("请开启无障碍服务", 0);
                Toast.makeText(context, "请开启无障碍服务", Toast.LENGTH_SHORT).show();
                mainActivity.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
            if (getGoodsIdForUrl(mBinding.costomLink.getText().toString()) == null) {
                Toast.makeText(context, "自定义链接里面没有goods_id", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        preferences.edit().putString("userId", mBinding.userId.getText().toString()).apply();
        preferences.edit().putString("fkWait", mBinding.fkWait.getText().toString()).apply();
        preferences.edit().putString("taskWait", mBinding.taskWait.getText().toString()).apply();
        preferences.edit().putString("costomLink", mBinding.costomLink.getText().toString()).apply();
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
        /*ViewModelProvider.Factory factory = InjectionUtil.getAppsFactory();
        AppsViewModel viewModel = new ViewModelProvider(this, factory).get(AppsViewModel.class);
        viewModel.getInstalledApps(0);
        ((MainActivity) requireActivity()).scanUser();*/
        ((MainActivity) requireActivity()).shuaxin();
        Toast.makeText(mainActivity, "初始化完成，可以启动了:", Toast.LENGTH_SHORT).show();
    }

    public void start() {
        //Toast.makeText(MyActivity.this, "启动", Toast.LENGTH_SHORT).show();
        boolean result = core.launchApk("com.xunmeng.pinduoduo", 0);
        if (!result) {
            Toast.makeText(mainActivity, "启动失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void openVideo() {
        core.launchPddVideo("com.xunmeng.pinduoduo", 0);
    }
}