package com.lizl.mydiary.custom.others.recylerviewitemdivider

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View

class GridDividerItemDecoration() : RecyclerView.ItemDecoration()
{
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State)
    {
        outRect.top = 1

        if ((parent.getChildLayoutPosition(view) + 1) % 3 == 0)
        {
            outRect.right = 0
        }
        else
        {
            outRect.right = 1
        }
    }
}
