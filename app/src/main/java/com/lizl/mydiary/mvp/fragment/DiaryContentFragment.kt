package com.lizl.mydiary.mvp.fragment

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.custom.dialog.DialogLoading
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import kotlinx.android.synthetic.main.fragment_diary_content.*


class DiaryContentFragment : BaseFragment<DiaryContentFragmentPresenter>(), DiaryContentFragmentContract.View
{

    private lateinit var diaryImageListAdapter: DiaryImageListAdapter
    private var diaryBean: DiaryBean? = null

    private var dialogLoading: DialogLoading? = null

    override fun getLayoutResId() = R.layout.fragment_diary_content

    override fun initPresenter() = DiaryContentFragmentPresenter(this)

    override fun initView()
    {
        val bundle = arguments
        diaryBean = bundle?.getSerializable(AppConstant.BUNDLE_DATA_OBJECT) as DiaryBean?

        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_confirm) {
            if (dialogLoading == null)
            {
                dialogLoading = DialogLoading(context!!, getString(R.string.in_save))
            }
            presenter.saveDiary(diaryBean, et_diary_content.text.toString(), diaryImageListAdapter.getImageList())
        })
        ctb_title.setBtnList(titleBtnList)

        diaryImageListAdapter = DiaryImageListAdapter(true, 9)
        rv_image_list.layoutManager = GridLayoutManager(context, 3)
        rv_image_list.adapter = diaryImageListAdapter

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        diaryImageListAdapter.setOnAddImageBtnClickListener {
            presenter.selectImage(this@DiaryContentFragment, 9 - diaryImageListAdapter.getImageList().size)
        }

        diaryImageListAdapter.setOnImageClickListener {
            val imageList = arrayListOf<String>()
            imageList.addAll(diaryImageListAdapter.getImageList())
            (activity as BaseActivity<*>).turnToImageBrowserActivity(imageList, it)
        }

        showDiaryContent(diaryBean)
    }

    private fun showDiaryContent(diaryBean: DiaryBean?)
    {
        et_diary_content.setText(diaryBean?.content)
        if (diaryBean?.imageList != null)
        {
            diaryImageListAdapter.addImageList(diaryBean.imageList!!)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleActivityResult(requestCode, resultCode, data)
    }

    override fun onDiarySaveSuccess()
    {
        backToPreFragment()
    }

    override fun onImageSelectedFinish(picList: List<String>)
    {
        diaryImageListAdapter.addImageList(picList)
    }
}