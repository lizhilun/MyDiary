package com.lizl.mydiary.mvp.activity

import androidx.navigation.Navigation
import com.lizl.mydiary.R
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
}
