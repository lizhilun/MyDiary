package com.lizl.mydiary.util

import java.util.ArrayList
import java.util.regex.*

class RichTextUtil
{
    companion object
    {
        /**
         * @param targetStr 要处理的字符串
         * @description 切割字符串，将文本和img标签碎片化，如"ab<img></img>cd"转换为"ab"、"<img></img>"、"cd"
         */
        fun cutStringByImgTag(targetStr: String): List<String>
        {
            val splitTextList = ArrayList<String>()
            val pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>")
            val matcher = pattern.matcher(targetStr)
            var lastIndex = 0
            while (matcher.find())
            {
                if (matcher.start() > lastIndex)
                {
                    splitTextList.add(targetStr.substring(lastIndex, matcher.start()))
                }
                splitTextList.add(targetStr.substring(matcher.start(), matcher.end()))
                lastIndex = matcher.end()
            }
            if (lastIndex != targetStr.length)
            {
                splitTextList.add(targetStr.substring(lastIndex, targetStr.length))
            }
            return splitTextList
        }

        /**
         * 获取img标签中的src值
         * @param content
         * @return
         */
        fun getImgSrc(content: String): String?
        {
            var str_src: String? = null
            //目前img标签标示有3种表达式
            //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
            //开始匹配content中的<img />标签
            val p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)")
            val m_img = p_img.matcher(content)
            var result_img = m_img.find()
            if (result_img)
            {
                while (result_img)
                {
                    //获取到匹配的<img />标签中的内容
                    val str_img = m_img.group(2)

                    //开始匹配<img />标签中的src
                    val p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')")
                    val m_src = p_src.matcher(str_img)
                    if (m_src.find())
                    {
                        str_src = m_src.group(3)
                    }
                    //结束匹配<img />标签中的src

                    //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                    result_img = m_img.find()
                }
            }
            return str_src
        }
    }
}