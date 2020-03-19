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
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class BaseFragment<T : BasePresenter<*>> : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    protected lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this)
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
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
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

        presenter.onDestroy()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        EventBus.getDefault().unregister(this)
    }

    abstract fun getLayoutResId(): Int

    abstract fun initPresenter(): T

    abstract fun initView()

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

    private fun turnToFragment(fragmentId: Int, bundle: Bundle?)
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onUiEvent(uiEvent: UIEvent)
    {
        presenter.handleUIEvent(uiEvent)
    }
}