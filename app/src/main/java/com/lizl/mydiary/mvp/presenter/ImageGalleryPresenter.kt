package com.lizl.mydiary.mvp.presenter

import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.ImageGalleryContract
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class ImageGalleryPresenter(private var view: ImageGalleryContract.View?) : ImageGalleryContract.Presenter
{
    override fun getImageList()
    {
        GlobalScope.launch {

            val imageList = mutableListOf<String>()

            val imageDir = File(FileUtil.getImageFileSavePath())
            if (imageDir.exists())
            {
                imageDir.listFiles().forEach {
                    if (ImageUtils.isImage(it))
                    {
                        imageList.add(it.absolutePath)
                    }
                }
            }

            Collections.sort(imageList, FileComparator())
            GlobalScope.launch(Dispatchers.Main) { view?.showImageList(imageList) }
        }
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }

    inner class FileComparator : Comparator<String>
    {
        override fun compare(filePath1: String, filePath2: String): Int
        {
            return if (File(filePath1).lastModified() > File(filePath2).lastModified()) -1 else 1
        }
    }
}