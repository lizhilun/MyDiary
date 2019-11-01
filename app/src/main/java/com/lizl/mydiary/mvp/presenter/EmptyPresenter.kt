package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.EmptyContract

class EmptyPresenter : EmptyContract.Presenter
{
    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
    }
}