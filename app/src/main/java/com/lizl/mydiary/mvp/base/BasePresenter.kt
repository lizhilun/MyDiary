package com.lizl.mydiary.mvp.base

import com.lizl.mydiary.event.UIEvent

interface BasePresenter<T : BaseView>
{
    fun handleUIEvent(uiEvent: UIEvent)

    fun onDestroy()
}