package com.lizl.mydiary.mvp.presenter

import android.net.Uri
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.mvp.contract.DiaryContentContract
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.DiaryUtil
import com.lizl.mydiary.util.FileUtil
import isSameList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class DiaryContentPresenter(private var view: DiaryContentContract.View?) : DiaryContentContract.Presenter
{

    override fun saveDiary(diaryBean: DiaryBean?, content: String, imageList: List<String>, createTime: Long, diaryMood: Int, diaryTag: String)
    {
        GlobalScope.launch {

            if (diaryBean != null)
            {
                if (diaryBean.content == content && diaryBean.createTime == createTime && diaryBean.mood == diaryMood
                    && diaryBean.imageList.isSameList(imageList) && diaryBean.tag == diaryTag
                )
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
                } else
                {
                    saveImageList.add(DiaryUtil.saveDiaryImage(it))
                }
            }
            saveDiaryBean.imageList = saveImageList

            AppDatabase.instance.getDiaryDao().insert(saveDiaryBean)

            GlobalScope.launch(Dispatchers.Main) { view?.onDiarySaveSuccess() }
        }
    }

    override fun handleImageSelectSuccess(imageList: List<Uri>)
    {
        GlobalScope.launch {

            val saveImageList = mutableListOf<String>()

            imageList.forEach { saveImageList.add(FileUtil.getFilePathFromUri(it)!!) }

            GlobalScope.launch(Dispatchers.Main) { view?.onImageSelectedFinish(saveImageList) }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}