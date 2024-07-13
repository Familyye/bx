package top.niunaijun.blackboxa;

import android.view.accessibility.AccessibilityEvent;

import top.niunaijun.blackboxa.node.AccUtils;

public class MyAccessibilityService extends AccUtils {

    public MyAccessibilityService() {
    }

    /**
     * 监听事件的发生
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        // 刷新当前 Activity()
        super.refreshCurrentActivity(accessibilityEvent);

        // 监听点击事件
        super.systemClickListener(accessibilityEvent);
    }
}