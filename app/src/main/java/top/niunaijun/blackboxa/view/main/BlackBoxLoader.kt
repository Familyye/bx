package top.niunaijun.blackboxa.view.main

import android.app.Activity
import android.app.Application
import android.content.Context
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackbox.app.configuration.AppLifecycleCallback
import top.niunaijun.blackbox.app.configuration.ClientConfiguration
import top.niunaijun.blackboxa.app.App
import top.niunaijun.blackboxa.biz.cache.AppSharedPreferenceDelegate
import java.io.File


/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/5/6 23:38
 */
class BlackBoxLoader {
    private var mHideRoot by AppSharedPreferenceDelegate(App.getContext(), true)
    private var mHideXposed by AppSharedPreferenceDelegate(App.getContext(), true)
    private var mDaemonEnable by AppSharedPreferenceDelegate(App.getContext(), true)
    private var mShowShortcutPermissionDialog by AppSharedPreferenceDelegate(App.getContext(), true)


    fun hideRoot(): Boolean {

        return mHideRoot
    }

    fun invalidHideRoot(hideRoot: Boolean) {
        this.mHideRoot = hideRoot
    }

    fun hideXposed(): Boolean {
        return mHideXposed
    }

    fun invalidHideXposed(hideXposed: Boolean) {
        this.mHideXposed = hideXposed
    }

    fun daemonEnable(): Boolean {
        return mDaemonEnable
    }

    fun invalidDaemonEnable(enable: Boolean) {
        this.mDaemonEnable = enable
    }

    fun showShortcutPermissionDialog(): Boolean {
        return mShowShortcutPermissionDialog
    }

    fun invalidShortcutPermissionDialog(show: Boolean) {
        this.mShowShortcutPermissionDialog = show
    }

    fun getBlackBoxCore(): BlackBoxCore {
        return BlackBoxCore.get()
    }

    fun addLifecycleCallback() {
        BlackBoxCore.get().addAppLifecycleCallback(object : AppLifecycleCallback() {
            override fun beforeCreateApplication(
                packageName: String?,
                processName: String?,
                context: Context?,
                userId: Int
            ) {
                /*Log.d(
                    Const.TAG,
                    "beforeCreateApplication: pkg $packageName, processName $processName,userID:${BActivityThread.getUserId()}"
                )*/
            }

            override fun beforeApplicationOnCreate(
                packageName: String?,
                processName: String?,
                application: Application?,
                userId: Int
            ) {
                /*Log.d(
                    Const.TAG,
                    "beforeApplicationOnCreate: pkg $packageName, processName $processName"
                )*/
            }

            override fun afterApplicationOnCreate(
                packageName: String?,
                processName: String?,
                application: Application?,
                userId: Int
            ) {
                /*Log.d(
                    Const.TAG,
                    "afterApplicationOnCreate: pkg $packageName, processName $processName"
                )*/
            }

            override fun onActivityStarted(activity: Activity) {
                var str: String = activity.javaClass.name
                /*if (str.contains("com.xunmeng.pinduoduo")) {
                    Log.d(Const.TAG + "__", "onActivityStarted: $str")
                    //toast("拼多多启动了")
                    if (str.contains("com.xunmeng.pinduoduo.ui.activity.HomeActivity")) {
                        toast("拼多多启动了")
                        //showFloatingWindow()
                    }
                }*/
            }

            override fun onActivityResumed(activity: Activity) {
            }
        })
    }

    fun attachBaseContext(context: Context) {
        BlackBoxCore.get().doAttachBaseContext(context, object : ClientConfiguration() {
            override fun getHostPackageName(): String {
                return context.packageName
            }

            override fun isHideRoot(): Boolean {
                return mHideRoot
            }

            override fun isHideXposed(): Boolean {
                return mHideXposed
            }

            override fun isEnableDaemonService(): Boolean {
                return mDaemonEnable
            }

            override fun requestInstallPackage(file: File?): Boolean {
                val packageInfo =
                    context.packageManager.getPackageArchiveInfo(file!!.absolutePath, 0)
                return false
            }
        })
    }

    fun doOnCreate(context: Context) {
        BlackBoxCore.get().doCreate()
    }


    companion object {
        val TAG: String = BlackBoxLoader::class.java.simpleName

    }
}