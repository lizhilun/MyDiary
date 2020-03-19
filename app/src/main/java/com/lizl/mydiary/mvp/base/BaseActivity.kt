package com.lizl.mydiary.mvp.base

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.activity.LockActivity
import com.lizl.mydiary.mvp.activity.MainActivity
import com.lizl.mydiary.util.ActivityUtil
import com.lizl.mydiary.util.SkinUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseActivity<T : BasePresenter<*>> : AppCompatActivity()
{
    protected val TAG = this.javaClass.simpleName

    protected lateinit var presenter: T

    abstract fun getLayoutResId(): Int

    abstract fun initPresenter(): T

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        // 没开密码保护的情况下进入锁屏界面返回主界面
        if (this is LockActivity && !AppConfig.getSecurityConfig().isAppLockOn())
        {
            if (ActivityUtils.getActivityList().size == 1) ActivityUtil.turnToActivity(MainActivity::class.java)
            finish()
        }

        // Activity走onCreate()将上次应用停止时间置为0，保证onStart()会走是否显示锁定界面流程
        if (ActivityUtils.getActivityList().size == 1)
        {
            AppConfig.getSecurityConfig().setAppLastStopTime(0)
        }

        setContentView(getLayoutResId())

        SkinUtil.loadSkin()

        presenter = initPresenter()

        initView()
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()

        if (this is LockActivity)
        {
            return
        }

        // 密码保护打开并且应用超时的情况
        if (AppConfig.getSecurityConfig().isAppLockOn() && AppConfig.getSecurityConfig().getAppLockPassword()
                    .isNotBlank() && System.currentTimeMillis() - AppConfig.getSecurityConfig().getAppLastStopTime() >= AppConfig.getSecurityConfig()
                .getAppTimeoutInterval())
        {
            ActivityUtil.turnToActivity(LockActivity::class.java)
        }
        else
        {
            AppConfig.getSecurityConfig().setAppLastStopTime(Long.MAX_VALUE)
        }
    }

    override fun onRestart()
    {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onPause()
    {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()

        if (!AppUtils.isAppForeground()) AppConfig.getSecurityConfig().setAppLastStopTime(System.currentTimeMillis())
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        presenter.onDestroy()
    }

    /**
     * 点击EditText外隐藏输入法
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean
    {
        if (ev.action == MotionEvent.ACTION_DOWN)
        {
            if (isShouldHideKeyboard(currentFocus, ev))
            {
                KeyboardUtils.hideSoftInput(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean
    {
        if (v is EditText)
        {
            val location = IntArray(2)
            v.getLocationInWindow(location)
            val left = location[0]
            val top = location[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    override fun getDelegate(): AppCompatDelegate = SkinAppCompatDelegateImpl.get(this, this)
}