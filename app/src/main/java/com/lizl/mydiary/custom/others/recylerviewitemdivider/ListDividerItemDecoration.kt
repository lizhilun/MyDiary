package com.lizl.mydiary.custom.others.recylerviewitemdivider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.lizl.mydiary.R

class ListDividerItemDecoration(private val context: Context) : DividerItemDecoration(context, VERTICAL)
{
    private var mBounds = Rect()
    private var mDivider: Drawable = ContextCompat.getDrawable(context, R.drawable.bg_list_divider)!!

    init
    {
        setDrawable(mDivider)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State)
    {
        canvas.save()

        val left: Int = context.resources.getDimensionPixelOffset(R.dimen.global_divide_line_padding_edge)
        val right: Int = parent.width - context.resources.getDimensionPixelOffset(R.dimen.global_divide_line_padding_edge)

        val childCount = parent.childCount

        for (i in 0 until childCount)
        {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom = mBounds.bottom + Math.round(child.translationY)
            val top = bottom - mDivider.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
        canvas.restore()
    }
}