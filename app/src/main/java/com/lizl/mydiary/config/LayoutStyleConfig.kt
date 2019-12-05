package com.lizl.mydiary.config

import com.blankj.utilcode.util.SPUtils

class LayoutStyleConfig
{
    private object Singleton
    {
        val singleton = LayoutStyleConfig()
    }

    companion object
    {
        val instance = Singleton.singleton
    }

    fun isParagraphHeadIndent() = SPUtils.getInstance().getBoolean(ConfigConstant.LAYOUT_STYLE_PARAGRAPH_HEAD_INDENT,
            ConfigConstant.DEFAULT_LAYOUT_STYLE_PARAGRAPH_HEAD_INDENT)

    fun getDiaryImageMaxCount() = SPUtils.getInstance().getInt(ConfigConstant.LAYOUT_STYLE_IMAGE_COUNT, ConfigConstant.DEFAULT_LAYOUT_STYLE_IMAGE_COUNT)

    fun getImageSaveQuality() = SPUtils.getInstance().getInt(ConfigConstant.IMAGE_SAVE_QUALITY, ConfigConstant.DEFAULT_IMAGE_SAVE_QUALITY)
}