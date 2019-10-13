package com.lizl.mydiary.custom.view

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.DiaryImageListAdapter
import com.lizl.mydiary.bean.DiaryBean
import com.lizl.mydiary.util.AppDatabase
import com.lizl.mydiary.util.FileUtil
import com.lizl.mydiary.util.ImageUtil
import com.lizl.mydiary.util.UiUtil
import kotlinx.android.synthetic.main.layout_diary_content.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DiaryContentView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : NestedScrollView(context, attrs, defStyleAttr)
{
    private val REQUEST_CODE_SELECT_IMAGE = 23
    private var diaryBean: DiaryBean? = null

    private lateinit var diaryImageListAdapter: DiaryImageListAdapter

    private var parentFragment: Fragment? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        val diaryContentView = LayoutInflater.from(context).inflate(R.layout.layout_diary_content, this, false)
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(diaryContentView, layoutParams)

        diaryImageListAdapter = DiaryImageListAdapter(true, 9)
        rv_image_list.layoutManager = GridLayoutManager(context, 3)
        rv_image_list.adapter = diaryImageListAdapter

        diaryImageListAdapter.setOnAddImageBtnClickListener {
            selectImage()
        }

        diaryImageListAdapter.setOnImageClickListener {

        }
    }

    fun bindParentFragment(parentFragment: Fragment)
    {
        this.parentFragment = parentFragment
    }

    fun bindDiaryBean(bean: DiaryBean?)
    {
        this.diaryBean = bean
        et_diary_content.setText(bean?.content)
    }

    fun saveDiary()
    {
        if (diaryBean == null)
        {
            diaryBean = DiaryBean()
            diaryBean!!.createTime = System.currentTimeMillis()
        }
        diaryBean!!.content = et_diary_content.text.toString()
        AppDatabase.instance.getDiaryDao().insert(diaryBean!!)
    }

    private fun selectImage()
    {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*") // 相片类型
        parentFragment?.startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    {
        if (resultCode != AppCompatActivity.RESULT_OK || data == null)
        {
            return false
        }
        if (requestCode == REQUEST_CODE_SELECT_IMAGE)
        {
            handleImageSelectSuccess(data)
            return true
        }
        return false
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
                diaryImageListAdapter.addImageList(imageList)
            }
        }
    }
}