package top.niunaijun.blackboxa.util

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import top.niunaijun.blackboxa.Const
import top.niunaijun.blackboxa.Pdd
import top.niunaijun.blackboxa.R
import top.niunaijun.blackboxa.app.App

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/5/2 0:13
 */
var toastImpl: Toast? = null

fun Context.toast(msg: String) {
    toastImpl?.cancel()
    toastImpl = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
    toastImpl?.show()
}

fun toast(msg: String) {
    App.getContext().toast(msg)
}

fun toast(@StringRes msgID: Int) {
    toast(getString(msgID))
}

//通过EasyFloat显示一个全局悬浮窗，布局为左上角0,0。内容为2个textView，一个显示1，一个显示2
fun showFloatingWindow() {
    val context = App.getContext()
    var TAG="testFloat"

    if(EasyFloat.isShow(TAG)){
        Log.d(
            Const.TAG + "__",
            "隐藏浮窗")
        EasyFloat.hide(TAG)
        return;
    }
    EasyFloat.show(TAG)
    Log.d(
        Const.TAG + "__",
        "创建浮窗")
    EasyFloat.with(context)
        // 设置浮窗xml布局文件/自定义View，并可设置详细信息
        .setLayout(R.layout.float_view) { }
        // 设置浮窗显示类型，默认只在当前Activity显示，可选一直显示、仅前台显示
        .setShowPattern(ShowPattern.ALL_TIME)
        // 设置吸附方式，共15种模式，详情参考SidePattern
        .setSidePattern(SidePattern.RESULT_HORIZONTAL)
        // 设置浮窗的标签，用于区分多个浮窗
        .setTag(TAG)
        // 设置浮窗是否可拖拽
        .setDragEnable(true)
        // 浮窗是否包含EditText，默认不包含
        .hasEditText(false)
        // 设置浮窗固定坐标，ps：设置固定坐标，Gravity属性和offset属性将无效
        .setLocation(0, 0)
        // 设置浮窗的对齐方式和坐标偏移量
        .setGravity(Gravity.END or Gravity.CENTER_VERTICAL, 0, 200)
        // 设置当布局大小变化后，整体view的位置对齐方式
        .setLayoutChangedGravity(Gravity.END)
        // 设置拖拽边界值
        //.setBorder(100, 100, 800, 800)
        // 设置宽高是否充满父布局，直接在xml设置match_parent属性无效
        .setMatchParent(widthMatch = false, heightMatch = false)
        // 设置浮窗的出入动画，可自定义，实现相应接口即可（策略模式），无需动画直接设置为null
        .setAnimator(DefaultAnimator())
        // 浮窗的一些状态回调，如：创建结果、显示、隐藏、销毁、touchEvent、拖拽过程、拖拽结束。
        // ps：通过Kotlin DSL实现的回调，可以按需复写方法，用到哪个写哪个
        .registerCallback {
            createResult { isCreated, msg, view ->
                var startTv = view?.findViewById<TextView>(R.id.start)
                startTv?.setOnClickListener {
                    Pdd.start()
                }



                var stopTv = view?.findViewById<TextView>(R.id.stop)
                stopTv?.setOnClickListener {
                    //EasyFloat.dismiss(TAG)
                    Pdd.stop()
                }

            }
            show { }
            hide { }
            dismiss { }
            touchEvent { view, motionEvent -> }
            drag { view, motionEvent -> }
            dragEnd { }
        }
        // 创建浮窗（这是关键哦😂）
        .show()
}


//通过windowManage显示一个全局悬浮窗，布局为左上角0,0。内容为2个textView，一个显示1，一个显示2
val floatingWindows = mutableMapOf<String, View>()
fun showFloatingWindow1() {
    val context = App.getContext()
    // 假设此部分代码在某个Activity内被调用
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val tag = "myUniqueFloatingWindowTag"
    // 检查是否存在具有相同Tag的悬浮窗
    if (!floatingWindows.containsKey(tag)) {
        Log.d(Const.TAG + "__", "创建悬浮窗")
        // 不存在，则创建并添加悬浮窗
        val linearLayout = LinearLayout(context)
        linearLayout.background = ContextCompat.getDrawable(context, android.R.color.white)
        // 添加TextView到LinearLayout中
        val textView1 = TextView(context).apply {
            text = "1"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
        }
        linearLayout.addView(textView1, 200, 200)

        val textView2 = TextView(context).apply {
            text = "2"
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setOnClickListener {
                Log.d(Const.TAG + "__", "移除悬浮窗")
                windowManager.removeView(linearLayout)
                floatingWindows.remove(tag)
            }
        }
        linearLayout.addView(textView2, 200, 200)

        // 将LinearLayout添加到WindowManager，并记录到集合中
        var lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            },
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        lp.x = 0
        lp.y = 0
        lp.gravity = LinearLayout.VERTICAL
        windowManager.addView(linearLayout, lp)
        floatingWindows[tag] = linearLayout
    } else {
        Log.d(Const.TAG + "__", "悬浮窗已存在")
    }
    // 如果悬浮窗已存在，这里可以根据需要处理，比如更新内容等
}
