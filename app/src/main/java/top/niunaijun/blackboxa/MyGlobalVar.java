package top.niunaijun.blackboxa;

import android.content.Intent;
import android.content.SharedPreferences;

public class MyGlobalVar {
    public static String currentActivity = "";
    public static String TAG = "__尖叫__";
    public static Intent intent = null;
    public static Boolean devMode = false;
    public static Boolean isWait = false;
    public static int taskCount = 0;



    public static SharedPreferences preferences;
    public static int getIntentCode() {
        if (intent != null) {
            return intent.getIntExtra("code", 0);
        } else {
            return 0;
        }
    }

    public static String getIntentData() {
        if (intent != null) {
            return intent.getStringExtra("data");
        } else {
            return "";
        }
    }

}
