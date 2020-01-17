package com.lizl.mydiary.mvp.presenter

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import com.lizl.mydiary.R
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.event.EventConstant
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.DiaryContentContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.FileUtil
import com.lizl.mydiary.custom.others.GlideEngine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.util.*


class DiaryContentPresenter(private var view: DiaryContentContract.View?) : DiaryContentContract.Presenter
{

    private val REQUEST_CODE_SELECT_IMAGE = 23

    override fun saveDiary(diaryBean: DiaryBean?, content: String, imageList: List<String>, createTime: Long, diaryMood: Int, diaryTag: String)
    {
        GlobalScope.launch {

            if (diaryBean != null)
            {
                if (diaryBean.content == content && diaryBean.createTime == createTime && diaryBean.mood == diaryMood
                    && isSameList(diaryBean.imageList ?: emptyList(), imageList) && diaryBean.tag == diaryTag)
                {
                    GlobalScope.launch(Dispatchers.Main) { view?.onDiarySaveSuccess() }
                    return@launch
                }
            }

            GlobalScope.launch(Dispatchers.Main) { view?.onDiarySaving() }

            var saveDiaryBean = diaryBean
            if (saveDiaryBean == null)
            {
                saveDiaryBean = DiaryBean()
                saveDiaryBean.uid = UUID.randomUUID().toString()
            }
            saveDiaryBean.createTime = createTime
            saveDiaryBean.content = content
            saveDiaryBean.mood = diaryMood
            saveDiaryBean.tag = diaryTag

            val deleteImageList = saveDiaryBean.imageList?.filter { !imageList.contains(it) }
            deleteImageList?.forEach { FileUtil.deleteFile(it) }

            val saveImageList = mutableListOf<String>()
            val systemFileDir = FileUtil.getImageFileSavePath()
            imageList.forEach {
                if (!FileUtil.isFileExists(it)) return@forEach
                if (it.contains(systemFileDir))
                {
                    saveImageList.add(it)
                }
                else
                {
                    saveImageList.add(DiaryUtil.saveDiaryImage(it))
                }
            }
            saveDiaryBean.imageList = saveImageList

            AppDatabase.instance.getDiaryDao().insert(saveDiaryBean)

            GlobalScope.launch(Dispatchers.Main) { view?.onDiarySaveSuccess() }

            EventBus.getDefault().post(UIEvent(EventConstant.UI_EVENT_DIARY_SAVE_SUCCESS, saveDiaryBean))
        }
    }

    override fun selectImage(context: Activity, maxCount: Int)
    {
        Matisse.from(context).choose(MimeType.ofImage()) //照片视频全部显示MimeType.allOf()
            .countable(true) //true:选中后显示数字;false:选中后显示对号
            .maxSelectable(maxCount) //最大选择数量为9
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) //图像选择和预览活动所需的方向
            .thumbnailScale(0.85f) //缩放比例
            .theme(R.style.MatisseStyle) //主题  暗色主题 R.style.Matisse_Dracula
            .imageEngine(GlideEngine()) //图片加载方式，Glide4需要自定义实现
            .capture(true) //是否提供拍照功能，兼容7.0系统需要下面的配置
            //参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
            .captureStrategy(CaptureStrategy(true, "com.sendtion.matisse.fileprovider")) //存储到哪里
            .forResult(REQUEST_CODE_SELECT_IMAGE) //请求码
    }

    override fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (resultCode != AppCompatActivity.RESULT_OK || data == null)
        {
            return
        }
        if (requestCode == REQUEST_CODE_SELECT_IMAGE)
        {
            handleImageSelectSuccess(data)
        }
    }

    private fun handleImageSelectSuccess(data: Intent)
    {
        GlobalScope.launch {

            val imageList = mutableListOf<String>()

            val selectImageList = Matisse.obtainResult(data)

            selectImageList.forEach { imageList.add(FileUtil.getFilePathFromUri(it)!!) }

            GlobalScope.launch(Dispatchers.Main) { view?.onImageSelectedFinish(imageList) }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {
        when (uiEvent.event)
        {
            EventConstant.UI_EVENT_DELETE_DIARY_IMAGE ->
            {
                if (uiEvent.value is String)
                {
                    view?.onImageDelete(uiEvent.value)
                }
            }
        }
    }

    override fun onDestroy()
    {
        view = null
    }

    private fun isSameList(list1: List<String>, list2: List<String>): Boolean
    {
        return list1.containsAll(list2) && list2.containsAll(list1)
    }
}