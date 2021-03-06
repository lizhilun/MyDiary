package com.lizl.mydiary.mvp.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.util.TypedValue
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.TimeUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.bean.DateBean
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.config.AppConfig
import com.lizl.mydiary.constant.AppConstant
import com.lizl.mydiary.constant.EventConstant
import com.lizl.mydiary.custom.others.GlideEngine
import com.lizl.mydiary.custom.others.IndentTextWatcher
import com.lizl.mydiary.mvp.base.BaseActivity
import com.lizl.mydiary.mvp.contract.DiaryContentContract
import com.lizl.mydiary.mvp.presenter.DiaryContentPresenter
import com.lizl.mydiary.util.*
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
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
    companion object
    {
        const val REQUEST_CODE_SELECT_IMAGE = 23
    }

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

        diaryImageListAdapter.setOnAddImageBtnClickListener { turnToImageSelectActivityWithPermissionCheck() }

        ctb_title.setOnBackBtnClickListener { if (inEditMode) onFinishBtnClick() else super.onBackPressed() }

        dateBean = DateBean(diaryBean?.createTime ?: System.currentTimeMillis())
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
        tv_diary_tag.setOnClickListener { if (inEditMode) PopupUtil.showDiaryTagListPopup { tv_diary_tag.text = "#$it#" } }

        et_diary_content.post {
            if (!tv_diary_tag.isVisible)
            {
                et_diary_content.setPadding(0, resources.getDimensionPixelOffset(R.dimen.global_content_padding_edge), 0, 0)
            }
        }

        LiveEventBus.get(EventConstant.EVENT_DELETE_IMAGE, String::class.java).observe(this, Observer {
            diaryImageListAdapter.deleteImage(it)
        })

        showDiaryContent(diaryBean)
        if (inEditMode) showEditView() else showReadView()
    }

    private fun showDiaryMood(mood: Int)
    {
        curDiaryMood = mood
        val titleBtnList = mutableListOf<TitleBarBtnBean.BaseBtnBean>()
        titleBtnList.add(TitleBarBtnBean.ImageBtnBean(DiaryUtil.getMoodResByMood(mood)) {
            if (inEditMode) PopupUtil.showMoodSelectPopup(false) { showDiaryMood(it) }
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
        PopupUtil.showDatePickerDialog(dateBean.year, dateBean.month - 1, dateBean.day) { _, year, month, dayOfMonth ->
            run {
                PopupUtil.showTimePickerDialog(dateBean.hour, dateBean.minute) { _, hourOfDay, minute ->
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

        if (resultCode != Activity.RESULT_OK) return

        when (requestCode)
        {
            REQUEST_CODE_SELECT_IMAGE ->
            {
                val selectImageUriList = Matisse.obtainResult(data) ?: emptyList()
                val selectImagePathList = mutableListOf<String>()
                selectImageUriList.forEach {
                    val imagePath = FileUtil.getFilePathFromUri(it) ?: return@forEach
                    selectImagePathList.add(imagePath)
                }
                diaryImageListAdapter.addImageList(selectImagePathList)
            }
        }
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun onPermissionDenied()
    {
        PopupUtil.showOperationConfirmPopup(getString(R.string.notify_failed_to_get_permission),
                getString(R.string.notify_permission_be_refused)) { turnToImageSelectActivityWithPermissionCheck() }
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun onPermissionNeverAskAgain()
    {
        PopupUtil.showOperationConfirmPopup(getString(R.string.notify_failed_to_get_permission),
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
        PopupUtil.showLoadingPopup(getString(R.string.in_save))
    }

    override fun onDiarySaveSuccess()
    {
        PopupUtil.dismissAll()
        if (diaryBean == null) super.onBackPressed() else showReadView()
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
            PopupUtil.showOperationConfirmPopup(getString(R.string.notify), getString(R.string.notify_diary_has_not_save_sure_to_quit)) {
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

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
    fun turnToImageSelectActivity()
    {
        Matisse.from(this).choose(MimeType.ofImage()) //照片视频全部显示MimeType.allOf()
            .countable(true) //true:选中后显示数字;false:选中后显示对号
            .maxSelectable(AppConfig.getLayoutStyleConfig().getDiaryImageMaxCount() - diaryImageListAdapter.getImageList().size) //最大选择数量
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //图像选择和预览活动所需的方向
            .thumbnailScale(0.85f) //缩放比例
            .theme(if (SkinUtil.isNightModeOn()) R.style.Matisse_Dracula else R.style.MatisseStyle) //主题  暗色主题 R.style.Matisse_Dracula
            .imageEngine(GlideEngine()) //图片加载方式，Glide4需要自定义实现
            .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
            //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
            .captureStrategy(CaptureStrategy(true, "com.sendtion.matisse.fileprovider")) //存储到哪里
            .forResult(REQUEST_CODE_SELECT_IMAGE) //请求码
    }
}


