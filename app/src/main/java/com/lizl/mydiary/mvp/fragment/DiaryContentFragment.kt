package com.lizl.mydiary.mvp.fragment

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.event.DeleteImageEvent
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.base.BaseFragment
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentFragmentPresenter
import com.lizl.mydiary.util.AppConstant
import com.lizl.mydiary.util.DialogUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.fragment_diary_content.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class DiaryContentFragment : BaseFragment<DiaryContentFragmentPresenter>(), DiaryContentFragmentContract.View
{

    private lateinit var diaryImageListAdapter: DiaryImageListAdapter
    private var diaryBean: DiaryBean? = null

    private var inEditMode = false

    override fun getLayoutResId() = R.layout.fragment_diary_content

    override fun initPresenter() = DiaryContentFragmentPresenter(this)

    override fun initTitleBar()
    {
        ctb_title.setOnBackBtnClickListener {
            if (inEditMode)
            {
                if (isEmptyDiary())
                {
                    DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify), getString(R.string.notify_empty_diary_cannot_save)) {
                        backToPreFragment()
                    }
                    return@setOnBackBtnClickListener
                }
                presenter.saveDiary(diaryBean, et_diary_content.text.toString(), diaryImageListAdapter.getImageList())
            }
            else
            {
                backToPreFragment()
            }
        }
    }

    override fun initView()
    {
        val bundle = arguments
        diaryBean = bundle?.getSerializable(AppConstant.BUNDLE_DATA_OBJECT) as DiaryBean?
        inEditMode = diaryBean == null

        diaryImageListAdapter = DiaryImageListAdapter(inEditMode, 9)
        rv_image_list.layoutManager = GridLayoutManager(context, 3)
        rv_image_list.adapter = diaryImageListAdapter

        diaryImageListAdapter.setOnAddImageBtnClickListener { selectImageWithPermissionCheck() }

        diaryImageListAdapter.setOnImageClickListener {
            val imageList = arrayListOf<String>()
            imageList.addAll(diaryImageListAdapter.getImageList())
            (activity as BaseActivity<*>).turnToImageBrowserActivity(imageList, it)
        }

        fab_edit_diary.setOnClickListener { showEditView() }

        showDiaryContent(diaryBean)
        if (inEditMode) showEditView() else showReadView()
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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun selectImage()
    {
        presenter.selectImage(this@DiaryContentFragment, 9 - diaryImageListAdapter.getImageList().size)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun onPermissionDenied()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) { selectImageWithPermissionCheck() }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun onPermissionNeverAskAgain()
    {
        DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused_and_never_ask_again)) { UiUtil.goToAppDetailPage() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDiarySaving()
    {
        DialogUtil.showLoadingDialog(context!!, getString(R.string.in_save))
    }

    override fun onDiarySaveSuccess()
    {
        DialogUtil.dismissDialog()
        if (diaryBean == null)
        {
            backToPreFragment()
        }
        else
        {
            showReadView()
        }
    }

    override fun onImageSelectedFinish(picList: List<String>)
    {
        diaryImageListAdapter.addImageList(picList)
    }

    private fun showEditView()
    {
        inEditMode = true
        et_diary_content.isEnabled = true
        diaryImageListAdapter.setEditable(true)
        fab_edit_diary.isVisible = false
        ctb_title.setBackBtnRedId(R.mipmap.ic_confirm)

        et_diary_content.setSelection(et_diary_content.text.toString().length)
        UiUtil.showInputKeyboard(et_diary_content)
    }

    private fun showReadView()
    {
        inEditMode = false
        et_diary_content.isEnabled = false
        diaryImageListAdapter.setEditable(false)
        fab_edit_diary.isVisible = true
        ctb_title.setBackBtnRedId(R.mipmap.ic_back)
    }

    override fun onBackPressed(): Boolean
    {
        if (inEditMode && isDiaryModified(diaryBean))
        {
            DialogUtil.showOperationConfirmDialog(context!!, getString(R.string.notify), getString(R.string.notify_diary_has_not_save_sure_to_quit)) {
                backToPreFragment()
            }
            return true
        }
        return false
    }

    private fun isEmptyDiary() = TextUtils.isEmpty(et_diary_content.text.toString()) && diaryImageListAdapter.getImageList().isNullOrEmpty()

    private fun isDiaryModified(diaryBean: DiaryBean?): Boolean
    {
        return if (diaryBean == null)
        {
            !TextUtils.isEmpty(et_diary_content.text.toString()) || diaryImageListAdapter.getImageList().isNotEmpty()
        }
        else
        {
            diaryBean.content != et_diary_content.text.toString() || diaryBean.imageList != diaryImageListAdapter.getImageList()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onImageDelete(deleteImageEvent: DeleteImageEvent)
    {
        diaryImageListAdapter.deleteImage(deleteImageEvent.imagePath)
    }

    override fun needRegisterEvent() = true
}