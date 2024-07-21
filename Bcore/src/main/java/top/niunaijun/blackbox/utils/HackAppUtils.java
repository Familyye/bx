package top.niunaijun.blackbox.utils;

import android.app.Activity;
import android.util.Log;

public class HackAppUtils {
    public static final String TAG = "callActivityOnCreate";

    public static void enableQQLogOutput(String packageName, ClassLoader classLoader) {
        if ("com.tencent.mobileqq".equals(packageName)) {
            try {
                Reflector.on("com.tencent.qphone.base.util.QLog", true, classLoader)
                        .field("UIN_REPORTLOG_LEVEL")
                        .set(100);
            } catch (Exception e) {
                e.printStackTrace();
                // ignore
            }
        }
    }

    public static void startInject(Activity activity, ClassLoader classLoader) {

        /*String className = activity.getClass().getName();
        String time= System.currentTimeMillis()+"";
        Log.d(TAG+"|"+time, "加载类：" + className);
        if (className.contains("com.xunmeng.pinduoduo")) {
            if (className.contains("com.xunmeng.pinduoduo.goods.entity.GoodsResponse")) {
                Log.d(TAG+"|", "到达拼多多login界面");
                //创建一个子线程，1s之后输出12
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            Log.d(TAG+"|", "开始点击");
                            ShellUtils.execCommand("adb shell input tap 450 1280", true);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).run();
            }else{
                Log.d(TAG, "不是login");
            }
        }*/
    }
}
