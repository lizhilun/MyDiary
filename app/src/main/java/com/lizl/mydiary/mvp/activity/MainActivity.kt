package com.lizl.mydiary.mvp.activity

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.blankj.utilcode.util.KeyboardUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.UiApplication
import com.lizl.mydiary.config.ConfigConstant
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.presenter.EmptyPresenter

class MainActivity : BaseActivity<EmptyPresenter>()
{
    override fun getLayoutResId() = R.layout.activity_main

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {

    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")

        super.onCreate(savedInstanceState)

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        UiApplication.appConfig.setAppLastStopTime(0)
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")

        super.onStart()

        // 密码保护打开并且应用超时的情况
        if (UiApplication.appConfig.isAppLockOn() && !TextUtils.isEmpty(
                        UiApplication.appConfig.getAppLockPassword()) && System.currentTimeMillis() - UiApplication.appConfig.getAppLastStopTime() >= ConfigConstant.APP_TIMEOUT_PERIOD)
        {
            turnToFragment(R.id.lockFragment)
        }
    }

    override fun onBackPressed()
    {
        val topFragment = getTopFragment()
        if (topFragment != null && topFragment.onBackPressed())
        {
            return
        }
        if (!Navigation.findNavController(this, R.id.fragment_container).navigateUp())
        {
            super.onBackPressed()
        }
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")

        super.onStop()

        UiApplication.appConfig.setAppLastStopTime(System.currentTimeMillis())
    }

    private fun getTopFragment(): BaseFragment<*>?
    {
        if (supportFragmentManager.primaryNavigationFragment == null)
        {
            return null
        }

        if (supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments.isEmpty())
        {
            return null
        }

        return supportFragmentManager.primaryNavigationFragment!!.childFragmentManager.fragments[0] as BaseFragment<*>
    }

    private fun turnToFragment(fragmentId: Int)
    {
        val options = NavOptions.Builder().setEnterAnim(R.anim.slide_right_in).setExitAnim(R.anim.slide_left_out).setPopEnterAnim(R.anim.slide_left_in)
            .setPopExitAnim(R.anim.slide_right_out).build()
        Navigation.findNavController(this, R.id.fragment_container).navigate(fragmentId, null, options)
    }
}
