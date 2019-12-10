package com.lizl.mydiary.mvp.presenter

import com.blankj.utilcode.util.ImageUtils
import com.lizl.mydiary.event.UIEvent
import com.lizl.mydiary.mvp.contract.ImageGalleryContract
import com.lizl.mydiary.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class ImageGalleryPresenter(private var view: ImageGalleryContract.View?) : ImageGalleryContract.Presenter
{
    override fun getImageList()
    {
        GlobalScope.launch {

            val imageList = mutableListOf<String>()

            val imageDir = File(FileUtil.getImageFileSavePath())
            imageDir.listFiles()?.forEach {
                if (ImageUtils.isImage(it))
                {
                    imageList.add(it.absolutePath)
                }
            }

            imageList.sortByDescending { File(it).getCreateTime() }
            GlobalScope.launch(Dispatchers.Main) { view?.showImageList(imageList) }
        }
    }

    private fun File.getCreateTime(): Long
    {
        try
        {
            return this.nameWithoutExtension.toLong()
        }
        catch (e: NumberFormatException)
        {
        }
        return this.lastModified()
    }

    override fun handleUIEvent(uiEvent: UIEvent)
    {

    }

    override fun onDestroy()
    {
        view = null
    }
}