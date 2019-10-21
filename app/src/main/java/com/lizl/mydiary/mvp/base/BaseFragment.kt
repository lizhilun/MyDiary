package com.lizl.mydiary.mvp.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.lizl.mydiary.R
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment<T : BasePresenter<*>> : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    protected lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        Log.d(TAG, "onCreateView")
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onActivityCreated")
        super.onActivityCreated(savedInstanceState)

        presenter = initPresenter()

        initView()
        initTitleBar()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()

        if (needRegisterEvent() && !EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
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
    }

    override fun onDestroyView()
    {
        Log.d(TAG, "onDestroyView")
        super.onDestroyView()

        if (needRegisterEvent()) EventBus.getDefault().unregister(this)
        presenter.onDestroy()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    abstract fun getLayoutResId(): Int

    abstract fun initPresenter(): T

    abstract fun initView()

    abstract fun initTitleBar()

    abstract fun onBackPressed(): Boolean

    abstract fun needRegisterEvent(): Boolean

    protected fun backToPreFragment()
    {
        try
        {
            Navigation.findNavController(checkNotNull(view)).navigateUp()
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
        }
    }

    protected fun turnToFragment(fragmentId: Int)
    {
        turnToFragment(fragmentId, null)
    }

    protected fun turnToFragment(fragmentId: Int, bundle: Bundle?)
    {
        try
        {
            val options = NavOptions.Builder().setEnterAnim(R.anim.slide_right_in).setExitAnim(R.anim.slide_left_out).setPopEnterAnim(R.anim.slide_left_in)
                .setPopExitAnim(R.anim.slide_right_out).build()
            Navigation.findNavController(checkNotNull(view)).navigate(fragmentId, bundle, options)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
        }
    }
}