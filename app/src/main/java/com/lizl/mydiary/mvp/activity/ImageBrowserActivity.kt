package com.lizl.mydiary.mvp.activity

import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.ImageViewPagerAdapter
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.presenter.EmptyPresenter
import com.lizl.mydiary.util.AppConstant
import kotlinx.android.synthetic.main.activity_image_browser.*
import org.greenrobot.eventbus.EventBus

class ImageBrowserActivity : BaseActivity<EmptyPresenter>()
{
    private lateinit var imageList: ArrayList<String>

    override fun getLayoutResId() = R.layout.activity_image_browser

    override fun initPresenter() = EmptyPresenter()

    override fun initView()
    {
        imageList = intent?.getStringArrayListExtra(AppConstant.BUNDLE_DATA_STRING_ARRAY)!!
        val selectImageUrl = intent?.getStringExtra(AppConstant.BUNDLE_DATA_STRING)
        val imagePosition = imageList.indexOf(selectImageUrl!!)
        val editable = intent?.getBooleanExtra(AppConstant.BUNDLE_DATA_BOOLEAN, false)!!

        val imageViewPagerAdapter = ImageViewPagerAdapter(imageList)
        vp_image_list.adapter = imageViewPagerAdapter
        vp_image_list.currentItem = imagePosition

        tv_delete.isVisible = editable

        showImagePositionMark(imagePosition + 1)

        vp_image_list.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(p0: Int)
            {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
            {
            }

            override fun onPageSelected(position: Int)
            {
                showImagePositionMark(position + 1)
            }
        })

        tv_cur_image.bringToFront()
        iv_back.bringToFront()
        tv_delete.bringToFront()

        iv_back.setOnClickListener { finish() }
        tv_delete.setOnClickListener {

            val currentIndex = vp_image_list.currentItem
            val imagePath = imageList.removeAt(currentIndex)
            imageViewPagerAdapter.notifyDataSetChanged()
            EventBus.getDefault().post(UIEvent(EventConstant.UI_EVENT_DELETE_DIARY_IMAGE, imagePath))

            when (imageList.size)
            {
                0            -> finish()
                currentIndex -> showImagePositionMark(currentIndex)
                else         -> showImagePositionMark(currentIndex + 1)
            }
        }
    }

    fun showImagePositionMark(imagePosition: Int)
    {
        tv_cur_image.text = "$imagePosition/${imageList.size}"
        tv_cur_image.isVisible = imageList.size > 1
    }
}