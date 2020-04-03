package com.lizl.mydiary.mvp.activity

import androidx.core.view.isVisible
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.ImageViewPagerAdapter
import com.lizl.mydiary.constant.AppConstant
import com.lizl.mydiary.constant.EventConstant
import com.lizl.mydiary.custom.function.registerOnPageChangeCallback
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import kotlinx.android.synthetic.main.activity_image_browser.*

class ImageBrowserActivity : BaseActivity<EmptyPresenter>()
{

    override fun getLayoutResId() = R.layout.activity_image_browser

    override fun initPresenter() = EmptyPresenter()

    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    override fun initView()
    {
        val imageList = intent?.getStringArrayListExtra(AppConstant.BUNDLE_DATA_STRING_ARRAY) ?: ArrayList()
        val selectImageUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()
        val imagePosition = imageList.indexOf(selectImageUrl)
        val editable = intent?.getBooleanExtra(AppConstant.BUNDLE_DATA_BOOLEAN, false) ?: false

        imageViewPagerAdapter = ImageViewPagerAdapter(imageList)
        vp_image_list.adapter = imageViewPagerAdapter
        vp_image_list.setCurrentItem(imagePosition, false)
        vp_image_list.offscreenPageLimit = 3

        imageViewPagerAdapter.setOnImageItemClickListener { finish() }

        tv_delete.isVisible = editable

        vp_image_list.registerOnPageChangeCallback { updateImagePositionMark() }

        tv_cur_image.bringToFront()
        tv_delete.bringToFront()

        tv_delete.setOnClickListener {

            val currentIndex = vp_image_list.currentItem
            val deleteImagePath = imageViewPagerAdapter.getItem(currentIndex)
            imageViewPagerAdapter.remove(currentIndex)

            LiveEventBus.get(EventConstant.EVENT_DELETE_IMAGE).post(deleteImagePath)

            if (imageViewPagerAdapter.data.isEmpty()) finish()

            updateImagePositionMark()
        }

        updateImagePositionMark()
    }

    private fun updateImagePositionMark()
    {
        tv_cur_image.text = "${vp_image_list.currentItem + 1}/${imageViewPagerAdapter.data.size}"
        tv_cur_image.isVisible = imageViewPagerAdapter.data.size > 1
    }
}