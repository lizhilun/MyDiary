package com.lizl.mydiary.mvp.base

interface BasePresenter<T : BaseView>
{
    fun onDestroy()
}