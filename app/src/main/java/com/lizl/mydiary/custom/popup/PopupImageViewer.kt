package com.lizl.mydiary.custom.popup

import android.content.Context
import androidx.core.view.isVisible
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.ImagePagerAdapter
import com.lizl.mydiary.constant.EventConstant
import com.lizl.mydiary.custom.function.registerOnPageChangeCallback
import com.lxj.xpopup.enums.PopupAnimation
import com.lxj.xpopup.impl.FullScreenPopupView
import kotlinx.android.synthetic.main.popup_image_viewer.view.*

class PopupImageViewer(context: Context, private val imageList: MutableList<String>, private val position: Int, private val showDeleteBtn: Boolean) :
        FullScreenPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_image_viewer

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = true
            it.popupAnimation = PopupAnimation.ScaleAlphaFromCenter
        }

        val imagePagerAdapter = ImagePagerAdapter(imageList)
        rv_image_pager.adapter = imagePagerAdapter

        tv_delete.isVisible = showDeleteBtn

        tv_indicator.bringToFront()
        tv_delete.bringToFront()

        rv_image_pager.setCurrentItem(position, false)

        rv_image_pager.registerOnPageChangeCallback { position -> updateIndicator() }

        tv_delete.setOnClickListener {

            val currentIndex = rv_image_pager.currentItem
            val deleteImagePath = imagePagerAdapter.getItem(currentIndex)
            imagePagerAdapter.remove(currentIndex)

            LiveEventBus.get(EventConstant.EVENT_DELETE_IMAGE).post(deleteImagePath)

            if (imagePagerAdapter.data.isEmpty()) dismiss()

            updateIndicator()
        }

        updateIndicator()
    }

    private fun updateIndicator()
    {
        tv_indicator.isVisible = imageList.size > 1
        tv_indicator.text = "${rv_image_pager.currentItem + 1}/${imageList.size}"
    }
}
