package com.lizl.mydiary.mvp.fragment

import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.ImageGalleryContract
import com.lizl.mydiary.mvp.presenter.ImageGalleryPresenter
import kotlinx.android.synthetic.main.fragment_image_gallery.*

class ImageGalleryFragment : BaseFragment<ImageGalleryPresenter>(), ImageGalleryContract.View
{

    private lateinit var diaryImageListAdapter: DiaryImageListAdapter

    override fun getLayoutResId() = R.layout.fragment_image_gallery

    override fun initPresenter() = ImageGalleryPresenter(this)

    override fun initView()
    {
        diaryImageListAdapter = DiaryImageListAdapter(false, Int.MAX_VALUE)
        rv_image_list.layoutManager = GridLayoutManager(activity, 3)
        rv_image_list.adapter = diaryImageListAdapter

        diaryImageListAdapter.setOnImageClickListener {
            (activity as BaseActivity<*>).turnToImageBrowserActivity(ArrayList(diaryImageListAdapter.getImageList()), it, false)
        }

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        presenter.getImageList()
    }

    override fun showImageList(imageList: List<String>)
    {
        diaryImageListAdapter.addImageList(imageList)
    }

}