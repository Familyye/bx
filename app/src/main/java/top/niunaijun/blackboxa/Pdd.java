package top.niunaijun.blackboxa;

import static top.niunaijun.blackboxa.util.ToastExKt.toast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

public class Pdd{
    public static Thread thread;
    public static Context context;
    public static Activity activity;
    private static int i = 0;
    private static final String serviceName = "top.niunaijun.blackboxa.MyAccessibilityService";
    // 添加一个静态标记，用于控制线程是否应该停止
    private static volatile boolean isStopped = false;

    public static void start() {
        context = activity.getApplicationContext();
        //Log.d(Const.TAG + "__", "无障碍权限:" + isAccessibilityServiceEnabled(context, serviceName));
        if (!isAccessibilityServiceEnabled(context, serviceName)) {
            toast("请打开无障碍权限");
            // 如果无障碍服务未启用，启动系统设置界面
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            activity.startActivity(intent);
            return;
        }
        Log.d(Const.TAG + "__", "开始执行任务:" + i);
        toast("开始执行任务");
        /*thread = new Thread(
                () -> {
                    while (!isStopped) {
                        try {
                            Thread.sleep(1000);
                            i++;
                            Log.d(Const.TAG + "__", "任务执行中:" + i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            // 当线程被中断时，设置isStopped为true
                            isStopped = true;
                        }
                    }
                    Log.d(Const.TAG + "__", "任务已停止:" + i);
                    Looper.prepare();
                    toast("任务已停止");
                    Looper.loop();
                }
        );
        thread.start();*/
    }

    //关闭线程
    public static void stop() {
        Log.d(Const.TAG + "__", "准备停止任务");
        thread.interrupt();
    }

    //检测无障碍权限
    public static boolean isAccessibilityServiceEnabled(Context context, String serviceName) {
        String enabledServicesSetting = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting != null) {
            Log.d(Const.TAG + "__", "无障碍服务" + serviceName + "|" + enabledServicesSetting);
            return enabledServicesSetting.contains(serviceName);
            /*TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
            colonSplitter.setString(enabledServicesSetting);
            Log.d(Const.TAG + "__", "无障碍服务111：:" + colonSplitter+"|"+serviceName);
            while (colonSplitter.hasNext()) { // 遍历所有已启用的服务
                String accessibilityService = colonSplitter.next();
                Log.d(Const.TAG + "__", "无障碍服务：:" + accessibilityService);
                if (accessibilityService.equalsIgnoreCase(serviceName)) {
                    return true;
                }
            }*/
        }
        return false;
    }

}
