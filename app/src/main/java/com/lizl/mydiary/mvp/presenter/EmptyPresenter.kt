package com.lizl.mydiary.mvp.presenter

import com.lizl.mydiary.mvp.contract.EmptyContract

class EmptyPresenter : EmptyContract.Presenter
{
    override fun onDestroy()
    {
    }
}