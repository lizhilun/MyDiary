package com.lizl.mydiary.custom.popup

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener
import com.lizl.mydiary.R
import com.lizl.mydiary.util.GlideUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.enums.PopupStatus
import com.lxj.xpopup.interfaces.OnDragChangeListener
import com.lxj.xpopup.photoview.PhotoView
import com.lxj.xpopup.util.XPopupUtils
import kotlinx.android.synthetic.main.popup_image_viewer.view.*
import java.util.*

class PopupImageViewer(context: Context) : BasePopupView(context), OnDragChangeListener
{
    private var argbEvaluator = ArgbEvaluator()
    private var urls: MutableList<Any> = ArrayList()
    private var position = 0
    private var rect: Rect? = null
    private var srcView //动画起始的View，如果为null，移动和过渡动画效果会没有，只有弹窗的缩放功能
            : ImageView? = null
    private var snapshotView: PhotoView? = null
    private var isShowSaveBtn = true //是否显示保存按钮
    private var bgColor = Color.BLACK //弹窗的背景颜色，可以自定义

    private val photoViewAdapter = PhotoViewAdapter()

    override fun getPopupLayoutId() = R.layout.popup_image_viewer

    override fun initPopupContent()
    {
        super.initPopupContent()
        photoViewContainer.setOnDragChangeListener(this)
        pager.adapter = photoViewAdapter
        pager.offscreenPageLimit = 3
        pager.currentItem = position
        pager.isInvisible = true
        addOrUpdateSnapshot()
        pager.addOnPageChangeListener(object : SimpleOnPageChangeListener()
        {
            override fun onPageSelected(i: Int)
            {
                position = i
                showPagerIndicator()
            }
        })
        tv_delete.isVisible = isShowSaveBtn
        tv_delete.setOnClickListener {}
    }

    private fun showPagerIndicator()
    {
        if (urls.size > 1)
        {
            tv_pager_indicator.text = (position + 1).toString() + "/" + urls!!.size
        }
    }

    private fun addOrUpdateSnapshot()
    {
        if (srcView == null) return
        if (snapshotView == null)
        {
            snapshotView = PhotoView(context)
            photoViewContainer!!.addView(snapshotView)
            snapshotView!!.scaleType = srcView!!.scaleType
            snapshotView!!.translationX = rect!!.left.toFloat()
            snapshotView!!.translationY = rect!!.top.toFloat()
            XPopupUtils.setWidthHeight(snapshotView, rect!!.width(), rect!!.height())
        }
        snapshotView!!.setImageDrawable(srcView!!.drawable)
    }

    override fun doAfterShow()
    {
        //do nothing self.
    }

    public override fun doShowAnimation()
    {
        if (srcView == null)
        {
            photoViewContainer!!.setBackgroundColor(bgColor)
            pager.visibility = View.VISIBLE
            showPagerIndicator()
            photoViewContainer!!.isReleasing = false
            super.doAfterShow()
            return
        }
        photoViewContainer!!.isReleasing = true
        snapshotView!!.visibility = View.VISIBLE
        snapshotView!!.post {
            TransitionManager.beginDelayedTransition((snapshotView!!.parent as ViewGroup),
                    TransitionSet().setDuration(XPopup.getAnimationDuration().toLong()).addTransition(ChangeBounds()).addTransition(ChangeTransform())
                        .addTransition(ChangeImageTransform()).setInterpolator(FastOutSlowInInterpolator()).addListener(object : TransitionListenerAdapter()
                        {
                            override fun onTransitionEnd(transition: Transition)
                            {
                                pager.visibility = View.VISIBLE
                                snapshotView!!.visibility = View.INVISIBLE
                                showPagerIndicator()
                                photoViewContainer!!.isReleasing = false
                                super@PopupImageViewer.doAfterShow()
                            }
                        }))
            snapshotView!!.translationY = 0f
            snapshotView!!.translationX = 0f
            snapshotView!!.scaleType = ImageView.ScaleType.FIT_CENTER
            XPopupUtils.setWidthHeight(snapshotView, photoViewContainer!!.width, photoViewContainer!!.height)

            // do shadow anim.
            animateShadowBg(bgColor)
        }
    }

    private fun animateShadowBg(endColor: Int)
    {
        val start = (photoViewContainer!!.background as ColorDrawable).color
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation ->
            photoViewContainer!!.setBackgroundColor((argbEvaluator.evaluate(animation.animatedFraction, start, endColor) as Int))
        }
        animator.setDuration(XPopup.getAnimationDuration().toLong()).interpolator = LinearInterpolator()
        animator.start()
    }

    public override fun doDismissAnimation()
    {
        if (srcView == null)
        {
            photoViewContainer!!.setBackgroundColor(Color.TRANSPARENT)
            doAfterDismiss()
            pager.isInvisible = true
            return
        }
        tv_pager_indicator!!.visibility = View.INVISIBLE
        tv_delete!!.visibility = View.INVISIBLE
        pager.visibility = View.INVISIBLE
        snapshotView!!.visibility = View.VISIBLE
        photoViewContainer!!.isReleasing = true
        TransitionManager.beginDelayedTransition((snapshotView!!.parent as ViewGroup),
                TransitionSet().setDuration(XPopup.getAnimationDuration().toLong()).addTransition(ChangeBounds()).addTransition(ChangeTransform())
                    .addTransition(ChangeImageTransform()).setInterpolator(FastOutSlowInInterpolator()).addListener(object : TransitionListenerAdapter()
                    {
                        override fun onTransitionEnd(transition: Transition)
                        {
                            doAfterDismiss()
                            pager.visibility = View.INVISIBLE
                            snapshotView!!.visibility = View.VISIBLE
                            pager.scaleX = 1f
                            pager.scaleY = 1f
                            snapshotView!!.scaleX = 1f
                            snapshotView!!.scaleY = 1f
                        }
                    }))
        snapshotView!!.translationY = rect!!.top.toFloat()
        snapshotView!!.translationX = rect!!.left.toFloat()
        snapshotView!!.scaleX = 1f
        snapshotView!!.scaleY = 1f
        snapshotView!!.scaleType = srcView!!.scaleType
        XPopupUtils.setWidthHeight(snapshotView, rect!!.width(), rect!!.height())

        // do shadow anim.
        animateShadowBg(Color.TRANSPARENT)
    }

    override fun getAnimationDuration(): Int
    {
        return 0
    }

    override fun dismiss()
    {
        if (popupStatus != PopupStatus.Show) return
        popupStatus = PopupStatus.Dismissing
        if (srcView != null)
        {
            //snapshotView拥有当前pager中photoView的样子(matrix)
            val current = pager.getChildAt(pager.currentItem) as PhotoView
            val matrix = Matrix()
            current.getSuppMatrix(matrix)
            snapshotView!!.setSuppMatrix(matrix)
        }
        doDismissAnimation()
    }

    fun setImageUrls(urls: MutableList<Any>): PopupImageViewer
    {
        this.urls = urls
        photoViewAdapter.setData(urls)
        return this
    }

    fun setSrcView(srcView: ImageView?, position: Int): PopupImageViewer
    {
        this.srcView = srcView
        this.position = position
        if (srcView != null)
        {
            val locations = IntArray(2)
            this.srcView!!.getLocationInWindow(locations)
            rect = Rect(locations[0], locations[1], locations[0] + srcView.width, locations[1] + srcView.height)
        }
        return this
    }

    override fun onRelease()
    {
        dismiss()
    }

    override fun onDragChange(dy: Int, scale: Float, fraction: Float)
    {
        tv_pager_indicator!!.alpha = 1 - fraction
        if (isShowSaveBtn) tv_delete!!.alpha = 1 - fraction
        photoViewContainer!!.setBackgroundColor((argbEvaluator.evaluate(fraction * .8f, bgColor, Color.TRANSPARENT) as Int))
    }

    override fun onDismiss()
    {
        super.onDismiss()
        srcView = null
    }

    inner class PhotoViewAdapter : PagerAdapter()
    {
        private val imageList = mutableListOf<Any>()

        fun setData(imageList: MutableList<Any>)
        {
            this.imageList.clear()
            this.imageList.addAll(imageList)
            notifyDataSetChanged()
        }

        override fun getCount() = imageList.size

        override fun isViewFromObject(view: View, o: Any): Boolean
        {
            return o === view
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any
        {
            return PhotoView(container.context).apply {
                GlideUtil.displayOriImage(this, imageList[position])
                container.addView(this)
                setOnClickListener { dismiss() }
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any)
        {
            container.removeView(`object` as View)
        }
    }
}
