package com.lizl.mydiary.mvp.fragment

import android.Manifest
import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.custom.dialog.BaseDialog
import com.lizl.mydiary.custom.dialog.DialogLoading
import com.lizl.mydiary.custom.dialog.DialogOperationConfirm
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.fragment_diary_content.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions class DiaryContentFragment : BaseFragment<DiaryContentFragmentPresenter>(), DiaryContentFragmentContract.View
{
    private lateinit var diaryImageListAdapter: DiaryImageListAdapter
    private var diaryBean: DiaryBean? = null

    private var dialogLoading: DialogLoading? = null
    private var dialogOperationConfirm: DialogOperationConfirm? = null

    override fun getLayoutResId() = R.layout.fragment_diary_content

    override fun initPresenter() = DiaryContentFragmentPresenter(this)

    override fun initView()
    {
        val bundle = arguments
        diaryBean = bundle?.getSerializable(AppConstant.BUNDLE_DATA_OBJECT) as DiaryBean?

        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(R.mipmap.ic_confirm) {
            presenter.saveDiary(diaryBean, et_diary_content.text.toString(), diaryImageListAdapter.getImageList())
        })
        ctb_title.setBtnList(titleBtnList)

        diaryImageListAdapter = DiaryImageListAdapter(true, 9)
        rv_image_list.layoutManager = GridLayoutManager(context, 3)
        rv_image_list.adapter = diaryImageListAdapter

        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        diaryImageListAdapter.setOnAddImageBtnClickListener {
            selectImageWithPermissionCheck()
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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA) fun selectImage()
    {
        presenter.selectImage(this@DiaryContentFragment, 9 - diaryImageListAdapter.getImageList().size)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA) fun onPermissionDenied()
    {
        dialogOperationConfirm =
            DialogOperationConfirm(context!!, getString(R.string.notify_failed_to_get_permission), getString(R.string.notify_permission_be_refused))
        dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
        {
            override fun onConfirmButtonClick()
            {
                selectImageWithPermissionCheck()
            }
        })
        dialogOperationConfirm?.show()
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA) fun onPermissionNeverAskAgain()
    {
        Log.d(TAG, "onPermissionNeverAskAgain")

        dialogOperationConfirm = DialogOperationConfirm(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused_and_never_ask_again))
        dialogOperationConfirm?.setOnConfirmButtonClickListener(object : BaseDialog.OnConfirmButtonClickListener
        {
            override fun onConfirmButtonClick()
            {
                UiUtil.goToAppDetailPage()
            }
        })
        dialogOperationConfirm?.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDiarySaving()
    {
        if (dialogLoading == null)
        {
            dialogLoading = DialogLoading(context!!, getString(R.string.in_save))
        }
        dialogLoading?.show()
    }

    override fun onDiarySaveSuccess()
    {
        dialogLoading?.dismiss()
        backToPreFragment()
    }

    override fun onImageSelectedFinish(picList: List<String>)
    {
        diaryImageListAdapter.addImageList(picList)
    }
}