package top.niunaijun.blackboxa.node.utils;

import static top.niunaijun.blackboxa.node.GlobalVariableHolder.context;

import android.content.Context;
import android.os.Vibrator;

public class VibratorUtil {
    private VibratorUtil() {
    }

    /**
     * 震动
     */
    public static void startVibrator() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(15);
    }
}