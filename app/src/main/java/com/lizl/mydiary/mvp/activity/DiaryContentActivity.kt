package com.lizl.mydiary.mvp.activity

import android.Manifest
import android.content.Intent
import android.util.TypedValue
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.TimeUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.bean.DateBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.custom.others.IndentTextWatcher
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiaryContentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentPresenter
import com.lizl.mydiary.util.*
import kotlinx.android.synthetic.main.activity_diary_content.*
import kotlinx.android.synthetic.main.activity_main.ctb_title
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnNeverAskAgain
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import java.text.SimpleDateFormat
import java.util.*

@RuntimePermissions
class DiaryContentActivity : BaseActivity<DiaryContentPresenter>(), DiaryContentContract.View
{

    private lateinit var diaryImageListAdapter: DiaryImageListAdapter
    private var diaryBean: DiaryBean? = null
    private lateinit var dateBean: DateBean
    private var curDiaryMood = AppConstant.MOOD_NORMAL

    private var inEditMode = false

    private var maxContentTextHeight = 0

    override fun getLayoutResId() = R.layout.activity_diary_content

    override fun initPresenter() = DiaryContentPresenter(this)

    override fun initView()
    {
        diaryBean = intent?.getSerializableExtra(AppConstant.BUNDLE_DATA_OBJECT) as DiaryBean?
        inEditMode = diaryBean == null

        diaryImageListAdapter = DiaryImageListAdapter(inEditMode, AppConfig.getLayoutStyleConfig().getDiaryImageMaxCount(), true)
        rv_image_list.layoutManager = GridLayoutManager(this, 3)
        rv_image_list.adapter = diaryImageListAdapter

        diaryImageListAdapter.setOnAddImageBtnClickListener { selectImageWithPermissionCheck() }

        diaryImageListAdapter.setOnImageClickListener {
            ActivityUtil.turnToActivity(ImageBrowserActivity::class.java, diaryImageListAdapter.getImageList(), it, inEditMode)
        }

        ctb_title.setOnBackBtnClickListener { if (inEditMode) onFinishBtnClick() else super.onBackPressed() }

        dateBean = DateBean(if (diaryBean != null) diaryBean!!.createTime else System.currentTimeMillis())
        ctb_title.setTitleText("${dateBean.year}/${dateBean.month}/${dateBean.day} ${dateBean.getHourAndMinute()}")

        val diaryTag = diaryBean?.tag ?: getString(R.string.diary)
        tv_diary_tag.text = "#$diaryTag#"

        et_diary_content.setTextSize(TypedValue.COMPLEX_UNIT_PX, DiaryUtil.getDiaryFontSize())
        et_diary_content.setLineSpacing(0F, DiaryUtil.getDiaryLineSpace())

        showDiaryMood(diaryBean?.mood ?: AppConstant.MOOD_NORMAL)

        ctb_title.setOnTitleClickListener { if (inEditMode) showDatePickerDialog() }

        fab_edit_diary.setOnClickListener { showEditView() }

        KeyboardUtils.registerSoftInputChangedListener(this, onSoftInputChangeListener)

        if (AppConfig.getLayoutStyleConfig().isParagraphHeadIndent())
        {
            et_diary_content.addTextChangedListener(IndentTextWatcher())
        }

        tv_diary_tag.isVisible = AppConfig.getLayoutStyleConfig().isDiaryTagEnable()
        tv_diary_tag.setOnClickListener { if (inEditMode) DialogUtil.showDiaryTagListDialog(this) { tv_diary_tag.text = "#$it#" } }

        et_diary_content.post {
            if (!tv_diary_tag.isVisible)
            {
                et_diary_content.setPadding(0, resources.getDimensionPixelOffset(R.dimen.global_content_padding_edge), 0, 0)
            }
        }

        showDiaryContent(diaryBean)
        if (inEditMode) showEditView() else showReadView()
    }

    private fun showDiaryMood(mood: Int)
    {
        curDiaryMood = mood
        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(DiaryUtil.getMoodResByMood(mood)) {
            if (inEditMode) DialogUtil.showMoodSelectDialog(this, false) { showDiaryMood(it) }
        })
        ctb_title.setBtnList(titleBtnList)
    }

    private fun showDiaryContent(diaryBean: DiaryBean?)
    {
        val diaryShowContent = diaryBean?.content ?: if (AppConfig.getLayoutStyleConfig().isParagraphHeadIndent()) '\u3000'.toString() else ""
        et_diary_content.setText(diaryShowContent)
        if (diaryBean?.imageList != null)
        {
            diaryImageListAdapter.addImageList(diaryBean.imageList!!)
        }
    }

    private fun onFinishBtnClick()
    {
        if (isEmptyDiary()) return

        val tagText = tv_diary_tag.text.toString()
        val tag = tagText.substring(1, tagText.length - 1)
        presenter.saveDiary(diaryBean, et_diary_content.text.toString(), diaryImageListAdapter.getImageList(), dateBean.time, curDiaryMood, tag)
    }

    private fun showDatePickerDialog()
    {
        DialogUtil.showDatePickerDialog(this, dateBean.year, dateBean.month - 1, dateBean.day) { _, year, month, dayOfMonth ->
            run {
                DialogUtil.showTimePickerDialog(this, dateBean.hour, dateBean.minute) { _, hourOfDay, minute ->
                    run {
                        val simpleDateFormat = SimpleDateFormat("yyyy MM dd HH mm", Locale.getDefault())
                        val date = simpleDateFormat.parse("$year ${month + 1} $dayOfMonth $hourOfDay $minute")
                        dateBean = DateBean(TimeUtils.date2Millis(date))
                        ctb_title.setTitleText("${dateBean.year}/${dateBean.month}/${dateBean.day} ${dateBean.getHourAndMinute()}")
                    }
                }
            }
        }
    }

    private val onSoftInputChangeListener: (Int) -> Unit = {
        et_diary_content.maxHeight = if (it > 0)
        {
            if (maxContentTextHeight == 0)
            {
                val navBarHeight = if (BarUtils.isNavBarVisible(this)) BarUtils.getNavBarHeight() else 0
                val statusBarHeight = BarUtils.getStatusBarHeight()
                val titleBarHeight = resources.getDimensionPixelOffset(R.dimen.global_toolbar_height)
                val padding = resources.getDimensionPixelOffset(R.dimen.global_content_padding_content)
                val tagTextHeight = if (tv_diary_tag.isVisible) tv_diary_tag.height else padding
                maxContentTextHeight = UiUtil.getScreenHeight() - it - statusBarHeight - navBarHeight - titleBarHeight - tagTextHeight - padding
            }
            maxContentTextHeight
        }
        else
        {
            Int.MAX_VALUE
        }
        et_diary_content.setScrollEnable(it > 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.handleActivityResult(requestCode, resultCode, data)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun selectImage()
    {
        presenter.selectImage(this, AppConfig.getLayoutStyleConfig().getDiaryImageMaxCount() - diaryImageListAdapter.getImageList().size)
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun onPermissionDenied()
    {
        DialogUtil.showOperationConfirmDialog(this, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) { selectImageWithPermissionCheck() }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun onPermissionNeverAskAgain()
    {
        DialogUtil.showOperationConfirmDialog(this, getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused_and_never_ask_again)) { UiUtil.goToAppDetailPage() }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // NOTE: delegate the permission handling to generated function
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onImageDelete(imagePath: String)
    {
        diaryImageListAdapter.deleteImage(imagePath)
    }

    override fun onDiarySaving()
    {
        DialogUtil.showLoadingDialog(this, getString(R.string.in_save))
    }

    override fun onDiarySaveSuccess()
    {
        DialogUtil.dismissDialog()
        if (diaryBean == null) super.onBackPressed() else showReadView()
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
        ctb_title.setBackBtnRedId(R.drawable.ic_confirm)

        et_diary_content.setSelection(et_diary_content.text.toString().length)
        UiUtil.showInputKeyboard(et_diary_content)
    }

    private fun showReadView()
    {
        inEditMode = false
        et_diary_content.isEnabled = false
        diaryImageListAdapter.setEditable(false)
        fab_edit_diary.isVisible = true
        ctb_title.setBackBtnRedId(R.drawable.ic_back)
    }

    override fun onBackPressed()
    {
        if (inEditMode && isDiaryModified(diaryBean))
        {
            DialogUtil.showOperationConfirmDialog(this, getString(R.string.notify), getString(R.string.notify_diary_has_not_save_sure_to_quit)) {
                super.onBackPressed()
            }
            return
        }
        super.onBackPressed()
    }

    private fun isEmptyDiary() = et_diary_content.text.toString().isBlank() && diaryImageListAdapter.getImageList().isNullOrEmpty()

    private fun isDiaryModified(diaryBean: DiaryBean?): Boolean
    {
        return if (diaryBean == null)
        {
            et_diary_content.text.toString().isNotBlank() || diaryImageListAdapter.getImageList().isNotEmpty()
        }
        else
        {
            diaryBean.content != et_diary_content.text.toString() || diaryBean.imageList != diaryImageListAdapter.getImageList()
        }
    }
}