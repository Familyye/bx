package top.niunaijun.blackboxa.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.viewpager2.widget.ViewPager2
import top.niunaijun.blackbox.BlackBoxCore
import top.niunaijun.blackboxa.Jianjiao
import top.niunaijun.blackboxa.MyBoard
import top.niunaijun.blackboxa.MyGlobalVar
import top.niunaijun.blackboxa.R
import top.niunaijun.blackboxa.app.App
import top.niunaijun.blackboxa.app.AppManager
import top.niunaijun.blackboxa.databinding.ActivityMainBinding
import top.niunaijun.blackboxa.node.GlobalVariableHolder
import top.niunaijun.blackboxa.util.Resolution
import top.niunaijun.blackboxa.util.inflate
import top.niunaijun.blackboxa.view.apps.AppsFragment
import top.niunaijun.blackboxa.view.base.LoadingActivity
import top.niunaijun.blackboxa.view.list.ListActivity
import top.niunaijun.blackboxa.view.setting.SettingActivity


class MainActivity : LoadingActivity() {

    private val viewBinding: ActivityMainBinding by inflate()

    private lateinit var mViewPagerAdapter: ViewPagerAdapter

    private val fragmentList = mutableListOf<AppsFragment>()

    private var currentUser = 0
    private lateinit var mReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar(viewBinding.toolbarLayout.toolbar, R.string.app_name)
        initViewPager()
        initFab()
        initToolbarSubTitle()
        //Jianjiao.activity = this;
        initJianjiao()
    }

    private fun initJianjiao() {
        GlobalVariableHolder.context = this
        GlobalVariableHolder.mainActivity = this
        /*var myBoard = MyBoard()
        val filter = IntentFilter() //创建IntentFilter对象
        filter.addAction("com.jianjiao.test.PDDGUANGBO")
        registerReceiver(myBoard, filter) //注册Broadcast Receiver
        */
        mReceiver = MyBoard()
        // 创建IntentFilter并添加action
        val filter = IntentFilter("com.jianjiao.test.PDDGUANGBO")
        // 注册BroadcastReceiver
        registerReceiver(mReceiver, filter)
        Jianjiao.init()
    }

    private fun initToolbarSubTitle() {
        updateUserRemark(0)
        //hack code
        /*viewBinding.toolbarLayout.toolbar.getChildAt(1).setOnClickListener {
            MaterialDialog(this).show {
                title(res = R.string.userRemark)
                input(
                    hintRes = R.string.userRemark,
                    prefill = viewBinding.toolbarLayout.toolbar.subtitle
                ) { _, input ->
                    AppManager.mRemarkSharedPreferences.edit {
                        putString("Remark$currentUser", input.toString())
                        viewBinding.toolbarLayout.toolbar.subtitle = input
                    }
                }
                positiveButton(res = R.string.done)
                negativeButton(res = R.string.cancel)
            }
        }*/
    }

    private fun initViewPager() {
        Log.d(MyGlobalVar.TAG, "initViewPager: 初始化界面显示")
        val userList = BlackBoxCore.get().users
        userList.forEach {
            fragmentList.add(AppsFragment.newInstance(it.id))
        }

        currentUser = userList.firstOrNull()?.id ?: 0
        fragmentList.add(AppsFragment.newInstance(userList.size))
        mViewPagerAdapter = ViewPagerAdapter(this)
        mViewPagerAdapter.replaceData(fragmentList)
        viewBinding.viewPager.adapter = mViewPagerAdapter
        viewBinding.dotsIndicator.setViewPager2(viewBinding.viewPager)
        viewBinding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentUser = fragmentList[position].userID
                updateUserRemark(currentUser)
                showFloatButton(true)
            }
        })

    }

    private fun initFab() {
        viewBinding.fab.setOnClickListener {
            //showFloatingWindow()
            //Pdd.activity = this

            val userId = 0//viewBinding.viewPager.currentItem
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("userID", userId)
            apkPathResult.launch(intent)
            //Jianjiao.test();
            //Jianjiao.runScript()
        }
    }

    fun showFloatButton(show: Boolean) {
        val tranY: Float = Resolution.convertDpToPixel(120F, App.getContext())
        val time = 200L
        if (show) {
            viewBinding.fab.animate().translationY(0f).alpha(1f).setDuration(time)
                .start()
        } else {
            viewBinding.fab.animate().translationY(tranY).alpha(0f).setDuration(time)
                .start()
        }
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d(MyGlobalVar.TAG, "onKeyDown: " + keyCode)
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            Log.d(MyGlobalVar.TAG, "任务状态:"+GlobalVariableHolder.isRunning)
            if (GlobalVariableHolder.isRunning) {
                GlobalVariableHolder.isRunning = false
                toast("已停止运行")
                Log.d(MyGlobalVar.TAG, "停止运行:"+GlobalVariableHolder.isRunning)
                return true;
            }
        }
        return super.onKeyDown(keyCode, event)
    }*/

    fun scanUser() {
        val userList = BlackBoxCore.get().users

        if (fragmentList.size == userList.size) {
            fragmentList.add(AppsFragment.newInstance(fragmentList.size))
        } else if (fragmentList.size > userList.size + 1) {
            fragmentList.removeLast()
        }

        mViewPagerAdapter.notifyDataSetChanged()

    }

    private fun updateUserRemark(userId: Int) {
        var remark = AppManager.mRemarkSharedPreferences.getString("Remark$userId", "User $userId")
        if (remark.isNullOrEmpty()) {
            remark = "User $userId"
        }
        viewBinding.toolbarLayout.toolbar.subtitle = ""// = remark
    }

    private val apkPathResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                it.data?.let { data ->
                    val userId = data.getIntExtra("userID", 0)
                    val source = data.getStringExtra("source")
                    if (source != null) {
                        fragmentList[userId].installApk(source)
                    }
                }
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(MyGlobalVar.TAG, "onRestart: 界面重新显示")
        //startActivity(Intent(this, MyActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            /*R.id.main_git -> {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/jianjiao007"))
                startActivity(intent)
            }*/

            R.id.main_setting -> {
                SettingActivity.start(this)
            }

            /*R.id.main_tg -> {
                val intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/fvblackbox"))
                startActivity(intent)
            }*/

            /*R.id.fake_location -> {
//                toast("Still Developing")
                val intent = Intent(this, FakeManagerActivity::class.java)
                intent.putExtra("userID", 0)
                startActivity(intent)
            }*/
        }

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
