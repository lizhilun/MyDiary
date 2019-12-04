package com.lizl.mydiary.mvp.base

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.activity.LockActivity
import com.lizl.mydiary.util.ActivityUtil
import com.lizl.mydiary.util.SkinUtil
import com.lizl.mydiary.util.UiUtil
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
        setContentView(getLayoutResId())

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        if (ActivityUtils.getActivityList().size == 1)
        {
            UiApplication.appConfig.setAppLastStopTime(0)
        }

        EventBus.getDefault().register(this)

        BarUtils.addMarginTopEqualStatusBarHeight(findViewById<ViewGroup>(android.R.id.content).getChildAt(0))

        SkinUtil.loadSkin(this)

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
        if (UiApplication.appConfig.isAppLockOn() && !TextUtils.isEmpty(UiApplication.appConfig.getAppLockPassword())
            && System.currentTimeMillis() - UiApplication.appConfig.getAppLastStopTime() >= UiApplication.appConfig.getAppTimeoutInterval())
        {
            ActivityUtil.turnToActivity(LockActivity::class.java)
        }
        else
        {
            UiApplication.appConfig.setAppLastStopTime(Long.MAX_VALUE)
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

        if (!AppUtils.isAppForeground()) UiApplication.appConfig.setAppLastStopTime(System.currentTimeMillis())
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        EventBus.getDefault().unregister(this)

        presenter.onDestroy()
    }

    /**
     * 点击EditText外隐藏输入法
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean
    {
        val view = currentFocus
        if (view is EditText)
        {
            val w = currentFocus
            val location = IntArray(2)
            w!!.getLocationOnScreen(location)
            val x = ev.rawX + w.left - location[0]
            val y = ev.rawY + w.top - location[1]

            if (ev.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y > w.bottom))
            {
                UiUtil.hideInputKeyboard(view)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun getDelegate(): AppCompatDelegate = SkinAppCompatDelegateImpl.get(this, this)

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUiEvent(uiEvent: UIEvent)
    {
        when (uiEvent.event)
        {
            EventConstant.UI_EVENT_NIGHT_MODE_CHANGE -> SkinUtil.loadSkin(this)
            else                                     -> presenter.handleUIEvent(uiEvent)
        }
    }
}