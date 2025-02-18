package top.niunaijun.blackboxa.view.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import top.niunaijun.blackboxa.R
import top.niunaijun.blackboxa.databinding.ActivitySettingBinding
import top.niunaijun.blackboxa.util.inflate
import top.niunaijun.blackboxa.view.base.BaseActivity

class SettingActivity : BaseActivity() {

    private val viewBinding: ActivitySettingBinding by inflate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initToolbar(viewBinding.toolbarLayout.toolbar, R.string.setting, true)
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment, SettingFragment())
                .commit()
    }

    companion object{

        @JvmStatic
        fun start(mainActivity: Activity) {
            val intent = Intent(mainActivity,SettingActivity::class.java)
            intent.action = Intent.ACTION_OPEN_DOCUMENT
            mainActivity.startActivity(intent)
        }
    }

}