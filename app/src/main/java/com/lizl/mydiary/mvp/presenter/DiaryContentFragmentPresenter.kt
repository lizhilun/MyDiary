package com.lizl.mydiary.mvp.presenter

import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lizl.mydiary.mvp.contract.DiaryContentFragmentContract
import com.lizl.mydiary.util.FileUtil
import com.lizl.mydiary.util.ImageUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryContentFragmentPresenter(private val view: DiaryContentFragmentContract.View) : DiaryContentFragmentContract.Presenter
{

    private val REQUEST_CODE_SELECT_IMAGE = 23

    override fun selectImage(context: Fragment)
    {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*") // 相片类型
        context.startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
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
            var imagePath = FileUtil.getFilePathFromUri(data.data)
            val bitmap = ImageUtil.getSmallBitmap(imagePath!!, UiUtil.getScreenWidth(), UiUtil.getScreenHeight())
            imagePath = FileUtil.saveToSdCard(bitmap!!)
            imageList.add(imagePath)

            GlobalScope.launch(Dispatchers.Main) {
                view.onImageSelectedFinish(imageList)
            }
        }
    }
}