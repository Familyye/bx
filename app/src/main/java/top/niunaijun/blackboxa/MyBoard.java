package top.niunaijun.blackboxa;

import static top.niunaijun.blackboxa.node.AccUtils.printLogMsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import top.niunaijun.blackboxa.node.okhttp3.HttpUtils;

public class MyBoard extends BroadcastReceiver {
    public static String tempData = "";
    private static final String TAG = MyGlobalVar.TAG;
    private String receivedData = "";
    private int sliceSize = 1024 * 100;

    public static void uploadData(String data) {
        if (tempData.equals(data)) {
            return;
        }
        tempData = data;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (data.length() > 0) {
                    String res = HttpUtils.get("http://39.107.121.128:9998/up?t=" + data);
                    printLogMsg("上传数据:" + res);
                    Log.d(TAG, "uploadData: " + res);
                }
            }
        }).start();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // 检查接收到的Intent的action是否是我们期望的
        //Log.d(TAG + "|接收广播:", intent.getAction() + "|" + intent + "|" + intent.getStringExtra("data"));
        if (intent != null && "com.jianjiao.test.PDDGUANGBO".equals(intent.getAction())) {
            // 获取广播中的数据
            int index = intent.getIntExtra("index", -1);
            int total = intent.getIntExtra("total", -1);
            int code = intent.getIntExtra("code", 0);
            String sliceData = intent.getStringExtra("data");
            Log.d(TAG, "接收数据: " + sliceData.length() + "|" + index + "|" + total + "|" + code + "_" + sliceData);
            if (index == -1 || index == 0) {
                receivedData = "";
            }
            // 处理接收到的切片
            if (index >= 0 && total > 0 && sliceData != null) {
                // 将切片添加到 StringBuilder 中
                receivedData += sliceData;
                // 检查是否已经收到了所有切片
                //Log.d(TAG, "接收数据1: " + receivedData.length() + "|" + total * sliceSize);
                if (index == total - 1) {
                    // 重组完成，可以进行后续操作
                    String originalData = receivedData.toString();
                    // 在这里处理原始数据
                    //processData(originalData);
                    Log.d(TAG, "接收到的数据: " + originalData.length() + "|" + originalData);
                    Intent i = new Intent();
                    i.putExtra("code", code);
                    i.putExtra("userId", 0);
                    i.putExtra("data", originalData);
                    MyGlobalVar.intent = i;
                }
            }

            /*// 从Intent中获取额外的数据
            int code = intent.getIntExtra("code", 0);
            int userId = intent.getIntExtra("userId", 0);
            String data = intent.getStringExtra("data");
            MyGlobalVar.intent = intent;
            Log.d(TAG, "接收到的数据: " + code + "|" + data + "|" + userId);
            switch (code) {
                case 10:
                    //上传信息
                    uploadData(data);
            }*/
        }
    }

    private void processData(String originalData) {
        // 在这里处理原始数据
        // 例如打印出来或者保存到文件等
        Log.d(TAG, "原始数据: " + originalData);
    }
}