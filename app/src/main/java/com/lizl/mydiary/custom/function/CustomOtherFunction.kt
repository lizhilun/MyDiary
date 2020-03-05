import com.lizl.mydiary.bean.DiaryBean

fun List<String>?.isSameList(list: List<String>?): Boolean
{
    return orEmpty().containsAll(list.orEmpty()) && list.orEmpty().containsAll(orEmpty())
}

fun DiaryBean.isDiarySameContent(diaryBean: DiaryBean): Boolean
{
    return uid == diaryBean.uid
           && content == diaryBean.content
           && createTime == diaryBean.createTime
           && mood == diaryBean.mood
           && tag == diaryBean.tag
           && imageList.isSameList(diaryBean.imageList)
}