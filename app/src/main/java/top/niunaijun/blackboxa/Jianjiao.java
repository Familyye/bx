package top.niunaijun.blackboxa;

import static top.niunaijun.blackboxa.MyGlobalVar.devMode;
import static top.niunaijun.blackboxa.MyGlobalVar.preferences;
import static top.niunaijun.blackboxa.MyGlobalVar.taskCount;
import static top.niunaijun.blackboxa.node.AccUtils.printLogMsg;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.context;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.isRunning;
import static top.niunaijun.blackboxa.node.GlobalVariableHolder.mainActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackboxa.node.FloatingButton;
import top.niunaijun.blackboxa.node.FloatingWindow;
import top.niunaijun.blackboxa.node.GlobalVariableHolder;
import top.niunaijun.blackboxa.node.TaskBase;
import top.niunaijun.blackboxa.node.UiObject;
import top.niunaijun.blackboxa.node.okhttp3.HttpUtils;
import top.niunaijun.blackboxa.node.utils.FileUtils;
import top.niunaijun.blackboxa.view.main.MyActivity;

public class Jianjiao {
    static String ACTIONR = "com.jianjiao.duoduo.ACTIONR";
    static String userId = "18668561044";
    public static Thread thread = null;
    private static final String TAG = MyGlobalVar.TAG;
    private static BroadcastReceiver mReceiver;

    public static void init() {//初始化配置
        Intent intent = new Intent(context, MyActivity.class);
        context.startActivity(intent);
        if (!devMode) {
            //非开发者模式下，关闭主页面
            mainActivity.finish();
        }
        /*BlackBoxCore core = BlackBoxCore.get();
        mReceiver = new MyBoard();
        // 创建IntentFilter并添加action
        IntentFilter filter = new IntentFilter("com.jianjiao.test.PDDGUANGBO");
        // 注册BroadcastReceiver
        context.registerReceiver(mReceiver, filter);
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
        //SettingActivity.start(mainActivity);*/
    }

    public static boolean checkPermission(Activity activity) {
        return Settings.canDrawOverlays(activity);
    }

    public static void test() {
        BlackBoxCore.get().launchPddVideo("com.xunmeng.pinduoduo", 0);
    }

    public static void runScript() {
        Log.d(TAG, "run: 开始运行");
        isRunning = true;
        MyGlobalVar.isWait = false;
        taskCount = 0;
        TaskBase taskBase = new TaskBase();
        userId = preferences.getString("userId", "18668561044");
        int fkWait = Integer.parseInt(preferences.getString("fkWait", "10"));
        int taskWait = Integer.parseInt(preferences.getString("taskWait", "20"));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //taskBase._text("拼多多").findOne().click();
                //打开拼多多，监听插件意图
//                BlackBoxCore.get().launchApk("com.xunmeng.pinduoduo", 0);
                /*UiObject price = taskBase._textContains("¥0").findOne();
                if(price==null){
                    taskBase._textContains("查看商品详情").findOne().click();
                }else{
                    printLogMsg("商品价格异常，跳过");
                }*/
//                isRunning = false;
                try {
                    mainTask:
                    while (isRunning) {
                        if (MyGlobalVar.isWait) {
                            //暂停五分钟
                            printLogMsg("暂停中" + fkWait + "分钟");
                            Thread.sleep(1000 * 60 * fkWait);
                            MyGlobalVar.isWait = false;
                        }
                        if (taskBase._activityName().contains("com.xunmeng.pinduoduo")) {
                            Thread.sleep(2000);
                            taskBase._print("在拼多多界面");
                            if (taskBase._text("请描述下您遇到的问题～").findOne() != null) {
                                taskBase._sleep(2000);
                                printLogMsg("开始请求获取任务");
                                String goodsId = "";
                                while (!MyGlobalVar.isWait) {
                                    String res = HttpUtils.get("http://43.248.118.77:9995/getTask?userId=" + userId);
                                    boolean isNumericAndLengthSix = res.matches("^\\d{6,}$");
                                    if (isNumericAndLengthSix) {
                                        goodsId = res;
                                        break;
                                    }
                                }
                                printLogMsg("获取到任务：" + goodsId);
                                String url = "https://mobile.yangkeduo.com/mall_quality_assurance.html?_t_timestamp=comm_new_person&goods_id=" + goodsId;
                                taskBase._text("请描述下您遇到的问题～").findOne().click();
                                Thread.sleep(2000);
                                taskBase._text("请描述下您遇到的问题～").findOne().setText(url);
                                Thread.sleep(1000);
                                taskBase._text("发送").findOne().click();
                                Thread.sleep(1000);
                                taskBase._text(url).findOne().clickPoint();
                                Thread.sleep(2000);
                                //点击查看商品详情
                                MyGlobalVar.intent = null;
                                UiObject xiangqing = taskBase._textContains("查看商品详情").findOne();
                                if (xiangqing != null) {
                                    //判断价格信息
                                    taskBase._textContains("查看商品详情").findOne().click();
                                    /*UiObject price = taskBase._textContains("¥0").findOne();
                                if(price==null){

                                }else{
                                    printLogMsg("商品价格异常，跳过");
                                    goodsId="";
                                    continue;
                                }*/
                                }
                                Thread.sleep(3000);
                                boolean jixu = true;
                                while (jixu && !MyGlobalVar.isWait) {
                                    if (MyGlobalVar.getIntentCode() == 9) {
                                        String goodsString = MyGlobalVar.getIntentData();
                                        printLogMsg("获取到商品信息：" + goodsString.length());
                                        try {
                                            JSONObject jsonObject = new JSONObject(goodsString);
                                            JSONObject goods = (JSONObject) jsonObject.get("goods");
                                            String tmpGoodsId = goods.getString("goods_id");
                                            int status = goods.getInt("status");
                                            if (status == 5) {
                                                printLogMsg("任务失败,账号风控");
                                                MyGlobalVar.intent = null;
                                                jixu = false;
                                                MyGlobalVar.isWait = true;
                                                continue;
                                            }
                                            if (goodsId.equals(tmpGoodsId)) {
                                                jsonObject.put("userId", userId);
                                                printLogMsg("开始上传数据：" + tmpGoodsId);
                                                String res = HttpUtils.post("http://43.248.118.77:9995/uploadTask", jsonObject.toString());
                                                printLogMsg("上传数据完成：" + res);
                                                if (res.contains("成功上传数据")) {
                                                    taskCount++;
                                                }
                                                printLogMsg("当前数量：" + taskCount);
                                                taskBase._sleep(taskWait * 1000);
                                            } else {
                                                printLogMsg("任务失败,任务id不匹配:" + tmpGoodsId + "|" + goodsId + "_" + "|" + goodsId.length() + "|" + tmpGoodsId.length());
                                            }
                                            jixu = false;
                                            continue;
                                        } catch (JSONException e) {
                                            printLogMsg("json解析错误:" + e.toString());
                                            jixu = false;
                                            MyGlobalVar.isWait = true;
                                            continue mainTask;
                                        }
                                    } else if (taskBase._text("手机网络有问题").findOne() != null) {
                                        UiObject back = taskBase._text("手机网络有问题").findOne();
                                        if (back != null && back.bounds() != null) {
                                            //保存界面信息
                                            Log.d(TAG, "保存xml: " + FileUtils.writeFile("/sdcard/b.xml", back.bounds().toString()));
                                            printLogMsg("手机网络有问题,账号风控");
                                            MyGlobalVar.intent = null;
                                            jixu = false;
                                            MyGlobalVar.isWait = true;
                                            break;
                                        }
                                    }
                                    Thread.sleep(1000);
                                }
                                Log.d(TAG, "run: 运行完成");
                                Thread.sleep(2000);
                                taskBase._back();
                                Thread.sleep(1000);
                                taskBase._back();
                            } else {
                                taskBase._print("等待聊天界面:");
                                Thread.sleep(3000);
                                continue;
                            }
                        } else {
                            taskBase._print("不在拼多多界面:" + taskBase._activityName());
                            BlackBoxCore.get().stopPackage("com.xunmeng.pinduoduo", 0);
                            taskBase._sleep(2000);
                            BlackBoxCore.get().launchApk("com.xunmeng.pinduoduo", 0);
                            taskBase._sleep(3000);
                            taskBase._desc("聊天").findOne().click();
                            taskBase._sleep(2000);
                            taskBase._text("拼多多官方客服").findOne().click();
                        }
                    }
                    isRunning = false;
                    printLogMsg("任务已结束");
                } catch (InterruptedException e) {
                    System.out.println("在沉睡中被停止！进入catch，线程的是否处于停止状态：" + Jianjiao.thread.isInterrupted());
                    e.printStackTrace();
                    isRunning = false;
                    printLogMsg("任务已结束");
                }
            }
        });
        thread.start();
    }


    public static void huadong() {
        Log.d(TAG, "run: 开始运行");
        isRunning = true;
        //MyGlobalVar.isWait = false;
        TaskBase taskBase = new TaskBase();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRunning) {
                        //生成200-300的随机数
                        int sx = (int) (taskBase._width * 0.3 + (int) (Math.random() * (taskBase._width * 0.7)));
                        int sy = (int) (taskBase._height * 0.7 + (int) (Math.random() * (taskBase._height * 0.9)));
                        int ex = (int) (taskBase._width * 0.3 + (int) (Math.random() * (taskBase._width * 0.7)));
                        int ey = (int) (taskBase._width * 0.1 + (int) (Math.random() * (taskBase._width * 0.3)));
                        Log.d(TAG, "滑动坐标为: " + sx + "|" + sy + "|" + ex + "|" + ey);
                        printLogMsg("滑动：" + sx + "|" + sy + "|" + ex + "|" + ey);
                        taskBase._swipe(sx, sy, ex, ey, 1000);
                        Thread.sleep(2000);
                    }
                    printLogMsg("任务已结束");
                } catch (InterruptedException e) {
                    System.out.println("在沉睡中被停止！进入catch，线程的是否处于停止状态：" + Jianjiao.thread.isInterrupted());
                    e.printStackTrace();
                    isRunning = false;
                    printLogMsg("任务已结束");
                }
            }
        });
        thread.start();
    }

    public static void getStoragePermission() {
        // 获取权限
        XXPermissions.with(mainActivity)
                //申请单个权限
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                //申请多个权限
                //.permission(Permission.READ_EXTERNAL_STORAGE)
                //.permission(Permission.WRITE_EXTERNAL_STORAGE)


                //.permission(Permission.READ_MEDIA_IMAGES)
                //.permission(Permission.READ_MEDIA_VIDEO)
                //.permission(Permission.READ_MEDIA_AUDIO)
                //.permission(Permission.SYSTEM_ALERT_WINDOW)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
//                        String config = FileUtils.readFile("/sdcard/fatjs/aaa.js");
                        //FileUtils.writeToTxt("/sdcard/fatjs/config.json", configString);
                        printLogMsg("权限通过", 0);
                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            printLogMsg("被永久拒绝授权，请手动授予权限", 0);
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            //XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            printLogMsg("获取权限失败", 0);
                        }
                    }
                });
    }

    public static void sendIntent(int code, String data) {
        /*Intent intent = new Intent();
        intent.setAction("com.jianjiao.test.PDDGUANGBO");
        intent.putExtra("code", code);
        intent.putExtra("data", data);
        Log.d("jianjiao_guangbo", "开始发送22: " + code + "|" + data);
        mActivity.sendBroadcast(intent);*/
        Intent intent = new Intent();
        intent.setAction(ACTIONR);
        intent.putExtra("code", code);
        intent.putExtra("data", data);
        mainActivity.sendBroadcast(intent);
    }


    public static void getFloatPermission() {
        // 获取权限
        XXPermissions.with(mainActivity)
                //申请单个权限
                //.permission(Permission.MANAGE_EXTERNAL_STORAGE)
                //申请多个权限
                //.permission(Permission.READ_MEDIA_IMAGES)
                //.permission(Permission.READ_MEDIA_VIDEO)
                //.permission(Permission.READ_MEDIA_AUDIO)
                .permission(Permission.SYSTEM_ALERT_WINDOW)
                // 设置权限请求拦截器（局部设置）
                //.interceptor(new PermissionInterceptor())
                // 设置不触发错误检测机制（局部设置）
                //.unchecked()
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> permissions, boolean allGranted) {
                        if (!allGranted) {
                            return;
                        }
                        // 打开悬浮窗
                        context.startService(new Intent(GlobalVariableHolder.context, FloatingWindow.class));
                        // 打开悬浮窗
                        context.startService(new Intent(GlobalVariableHolder.context, FloatingButton.class));

                    }

                    @Override
                    public void onDenied(@NonNull List<String> permissions, boolean doNotAskAgain) {
                        if (doNotAskAgain) {
                            printLogMsg("被永久拒绝授权，请手动授予权限", 0);
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions);
                        } else {
                            printLogMsg("获取权限失败", 0);
                        }
                    }
                });
    }

    public static String uploadData(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            JSONObject goods = (JSONObject) jsonObject.get("goods");
            jsonObject.put("userId", userId);
            String res = HttpUtils.post("http://43.248.118.77:9995/uploadData", jsonObject.toString());
            return res;
        } catch (JSONException e) {
            //throw new RuntimeException(e);
            return e.toString();
        }
    }
}
