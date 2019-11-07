package com.lizl.mydiary.mvp.base

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.SkinAppCompatDelegateImpl
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.BarUtils
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.activity.ImageBrowserActivity
import com.lizl.mydiary.mvp.activity.LockActivity
import com.lizl.mydiary.mvp.activity.SettingActivity
import com.lizl.mydiary.util.AppConstant
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

        BarUtils.addMarginTopEqualStatusBarHeight(findViewById<ViewGroup>(android.R.id.content).getChildAt(0))

        SkinUtil.instance.loadSkin(this)

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

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)

        // 密码保护打开并且应用超时的情况
        if (this !is LockActivity && UiApplication.appConfig.isAppLockOn() && !TextUtils.isEmpty(
                        UiApplication.appConfig.getAppLockPassword()) && System.currentTimeMillis() - UiApplication.appConfig.getAppLastStopTime() >= ConfigConstant.APP_TIMEOUT_PERIOD)
        {
            turnToLockActivity()
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

    fun turnToImageBrowserActivity(imageUrlList: ArrayList<String>, selectImageUrl: String, editable: Boolean)
    {
        val intent = Intent(this, ImageBrowserActivity::class.java)
        val bundle = Bundle()
        bundle.putStringArrayList(AppConstant.BUNDLE_DATA_STRING_ARRAY, imageUrlList)
        bundle.putString(AppConstant.BUNDLE_DATA_STRING, selectImageUrl)
        bundle.putBoolean(AppConstant.BUNDLE_DATA_BOOLEAN, editable)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun turnToLockActivity()
    {
        val intent = Intent(this, LockActivity::class.java)
        startActivity(intent)
    }

    fun turnToSettingActivity()
    {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    override fun getDelegate(): AppCompatDelegate
    {
        return SkinAppCompatDelegateImpl.get(this, this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUiEvent(uiEvent: UIEvent)
    {
        when (uiEvent.event)
        {
            EventConstant.UI_EVENT_NIGHT_MODE_CHANGE -> SkinUtil.instance.loadSkin(this)
            else                                     -> presenter.handleUIEvent(uiEvent)
        }
    }
}