package com.lizl.mydiary.config

import com.blankj.utilcode.util.SPUtils

object LayoutStyleConfig
{
    fun isParagraphHeadIndent() = SPUtils.getInstance().getBoolean(ConfigConstant.LAYOUT_STYLE_PARAGRAPH_HEAD_INDENT,
            ConfigConstant.DEFAULT_LAYOUT_STYLE_PARAGRAPH_HEAD_INDENT)
}