package com.lizl.mydiary.mvp.activity

import androidx.core.view.isVisible
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.ImageViewPagerAdapter
import com.lizl.mydiary.constant.AppConstant
import com.lizl.mydiary.constant.EventConstant
import com.lizl.mydiary.custom.function.addOnPageChangeListener
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import kotlinx.android.synthetic.main.activity_image_browser.*

class ImageBrowserActivity : BaseActivity<EmptyPresenter>()
{
    private lateinit var imageList: ArrayList<String>

    override fun getLayoutResId() = R.layout.activity_image_browser

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        imageList = intent?.getStringArrayListExtra(AppConstant.BUNDLE_DATA_STRING_ARRAY) ?: ArrayList()
        val selectImageUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING).orEmpty()
        val imagePosition = imageList.indexOf(selectImageUrl)
        val editable = intent?.getBooleanExtra(AppConstant.BUNDLE_DATA_BOOLEAN, false) ?: false

        val imageViewPagerAdapter = ImageViewPagerAdapter(imageList)
        vp_image_list.adapter = imageViewPagerAdapter
        vp_image_list.currentItem = imagePosition
        vp_image_list.offscreenPageLimit = 3

        tv_delete.isVisible = editable

        showImagePositionMark(imagePosition + 1)

        vp_image_list.addOnPageChangeListener { showImagePositionMark(it + 1) }

        tv_cur_image.bringToFront()
        iv_back.bringToFront()
        tv_delete.bringToFront()

        iv_back.setOnClickListener { finish() }
        tv_delete.setOnClickListener {

            val currentIndex = vp_image_list.currentItem
            val deleteImagePath = imageList.removeAt(currentIndex)
            imageViewPagerAdapter.notifyDataSetChanged()

            LiveEventBus.get(EventConstant.EVENT_DELETE_IMAGE).post(deleteImagePath)

            when (imageList.size)
            {
                0            -> finish()
                currentIndex -> showImagePositionMark(currentIndex)
                else         -> showImagePositionMark(currentIndex + 1)
            }
        }
    }

    private fun showImagePositionMark(imagePosition: Int)
    {
        tv_cur_image.text = "$imagePosition/${imageList.size}"
        tv_cur_image.isVisible = imageList.size > 1
    }
}