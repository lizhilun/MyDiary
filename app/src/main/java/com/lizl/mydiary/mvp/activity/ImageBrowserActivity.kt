package com.lizl.mydiary.mvp.activity

import androidx.core.view.isVisible
import androidx.viewpager.widget.ViewPager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.ImageViewPagerAdapter
import com.lizl.mydiary.event.DeleteImageEvent
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
        val bundle = intent.extras!!

        imageList = bundle.getStringArrayList(AppConstant.BUNDLE_DATA_STRING_ARRAY)!!
        val selectImageUrl = bundle.getString(AppConstant.BUNDLE_DATA_STRING)
        val imagePosition = imageList.indexOf(selectImageUrl!!)

        val imageViewPagerAdapter = ImageViewPagerAdapter(imageList)
        vp_image_list.adapter = imageViewPagerAdapter
        vp_image_list.currentItem = imagePosition

        showImagePositionMark(imagePosition + 1, imageList.size)

        vp_image_list.addOnPageChangeListener(object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrollStateChanged(p0: Int)
            {
                // do nothing
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int)
            {
                // do nothing
            }

            override fun onPageSelected(position: Int)
            {
                showImagePositionMark(position + 1, imageList.size)
            }
        })

        tv_cur_image.bringToFront()
        iv_back.bringToFront()
        tv_delete.bringToFront()

        iv_back.setOnClickListener { finish() }
        tv_delete.setOnClickListener {
            val imagePath = imageList.removeAt(vp_image_list.currentItem)
            imageViewPagerAdapter.notifyDataSetChanged()
            EventBus.getDefault().post(DeleteImageEvent(imagePath))

            if (imageList.isEmpty())
            {
                finish()
            }
        }
    }

    fun showImagePositionMark(imagePosition: Int, imageSize: Int)
    {
        tv_cur_image.text = "$imagePosition/$imageSize"
        tv_cur_image.isVisible = imageSize > 1
    }
}