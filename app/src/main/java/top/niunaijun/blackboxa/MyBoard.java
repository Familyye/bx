package top.niunaijun.blackboxa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyBoard extends BroadcastReceiver {
    private static final String TAG = MyGlobalVar.TAG;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 检查接收到的Intent的action是否是我们期望的
        if (intent != null && "com.jianjiao.test.PDDGUANGBO".equals(intent.getAction())) {
            // 从Intent中获取额外的数据
            int code = intent.getIntExtra("code", 0);
            int userId = intent.getIntExtra("userId", 0);
            String data = intent.getStringExtra("data");
            MyGlobalVar.intent = intent;
            Log.d(TAG, "接收到的数据: " + code + "|" + data + "|" + userId);
        }
        /*int code = intent.getIntExtra("code", -1);
        String data = intent.getStringExtra("data");
        String action = intent.getAction();
        Log.d("jianjiao_guangbo1", "接收到数据: " + code + "|" + data + "|" + action);*/
    }
}