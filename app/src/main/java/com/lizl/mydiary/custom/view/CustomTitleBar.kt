package com.lizl.mydiary.custom.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.BarUtils
import com.lizl.mydiary.R
import com.lizl.mydiary.adapter.TitleBarBtnListAdapter
import com.lizl.mydiary.bean.TitleBarBtnBean
import com.lizl.mydiary.util.UiUtil
import skin.support.constraint.SkinCompatConstraintLayout
import skin.support.widget.SkinCompatEditText
import skin.support.widget.SkinCompatImageView
import skin.support.widget.SkinCompatTextView

class CustomTitleBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatConstraintLayout(context, attrs, defStyleAttr)
{
    private lateinit var backBtn: SkinCompatImageView
    private lateinit var titleTextView: SkinCompatTextView
    private lateinit var btnListView: RecyclerView
    private lateinit var searchEditText: SkinCompatEditText

    private var isBackBtnVisible = false
    private var inSearchMode = false

    private var onBackBtnClickListener: (() -> Unit)? = null
    private var onSearchTextChangeListener: ((searchText: String) -> Unit)? = null
    private var onTitleClickListener: (() -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        val statusBarHolder = View(context)
        statusBarHolder.id = generateViewId()
        addView(statusBarHolder)

        backBtn = SkinCompatImageView(context)
        val padding = context.resources.getDimensionPixelOffset(R.dimen.toolbar_back_icon_padding)
        backBtn.scaleType = ImageView.ScaleType.FIT_START
        backBtn.setImageResource(R.drawable.ic_back)
        backBtn.setPadding(0, padding, 0, padding)
        backBtn.id = generateViewId()
        addView(backBtn)

        titleTextView = SkinCompatTextView(context)
        titleTextView.id = generateViewId()
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.toolbar_title_text_size))
        titleTextView.setTextColor(ContextCompat.getColor(context, R.color.color_toolbar_text))
        titleTextView.gravity = Gravity.CENTER_VERTICAL
        addView(titleTextView)

        btnListView = RecyclerView(context)
        btnListView.id = generateViewId()
        addView(btnListView)

        searchEditText = SkinCompatEditText(context)
        searchEditText.id = generateViewId()
        searchEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.toolbar_title_text_size))
        searchEditText.setTextColor(ContextCompat.getColor(context, R.color.color_toolbar_text))
        searchEditText.gravity = Gravity.CENTER_VERTICAL
        searchEditText.filters = arrayOf(UiUtil.getNoWrapOrSpaceFilter())
        searchEditText.setLines(1)
        searchEditText.background = null
        addView(searchEditText)

        var titleBarHeight = LayoutParams.MATCH_PARENT

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar)
        for (index in 0 until typeArray.indexCount)
        {
            when (val attr = typeArray.getIndex(index))
            {
                R.styleable.CustomTitleBar_backBtnVisible ->
                {
                    isBackBtnVisible = typeArray.getBoolean(attr, true)
                    backBtn.isVisible = isBackBtnVisible
                }
                R.styleable.CustomTitleBar_titleText      -> titleTextView.text = typeArray.getString(attr)
                R.styleable.CustomTitleBar_titleBarHeight ->
                {
                    titleBarHeight = typeArray.getDimensionPixelOffset(attr, titleBarHeight)
                }
            }
        }
        typeArray.recycle()

        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        constraintSet.constrainHeight(statusBarHolder.id, BarUtils.getStatusBarHeight())
        constraintSet.constrainWidth(statusBarHolder.id, LayoutParams.MATCH_PARENT)
        constraintSet.connect(statusBarHolder.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)

        constraintSet.constrainHeight(backBtn.id, titleBarHeight)
        constraintSet.constrainWidth(backBtn.id, context.resources.getDimensionPixelOffset(R.dimen.toolbar_back_icon_size))
        constraintSet.connect(backBtn.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.connect(backBtn.id, ConstraintSet.TOP, statusBarHolder.id, ConstraintSet.BOTTOM)

        constraintSet.constrainHeight(titleTextView.id, titleBarHeight)
        constraintSet.constrainWidth(titleTextView.id, LayoutParams.WRAP_CONTENT)
        constraintSet.connect(titleTextView.id, ConstraintSet.START, backBtn.id, ConstraintSet.END)
        constraintSet.connect(titleTextView.id, ConstraintSet.TOP, statusBarHolder.id, ConstraintSet.BOTTOM)

        constraintSet.constrainHeight(btnListView.id, titleBarHeight)
        constraintSet.constrainWidth(btnListView.id, LayoutParams.WRAP_CONTENT)
        constraintSet.connect(btnListView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.connect(btnListView.id, ConstraintSet.TOP, statusBarHolder.id, ConstraintSet.BOTTOM)

        constraintSet.constrainHeight(searchEditText.id, titleBarHeight)
        constraintSet.constrainWidth(searchEditText.id, LayoutParams.MATCH_CONSTRAINT)
        constraintSet.connect(searchEditText.id, ConstraintSet.START, titleTextView.id, ConstraintSet.END)
        constraintSet.connect(searchEditText.id, ConstraintSet.END, btnListView.id, ConstraintSet.START, padding)
        constraintSet.connect(searchEditText.id, ConstraintSet.TOP, statusBarHolder.id, ConstraintSet.BOTTOM)

        constraintSet.applyTo(this)

        searchEditText.isVisible = false

        backBtn.setOnClickListener { onBackBtnClickListener?.invoke() }

        searchEditText.setOnClickListener { UiUtil.showInputKeyboard(searchEditText) }
        searchEditText.addTextChangedListener { onSearchTextChangeListener?.invoke(it.toString()) }

        titleTextView.setOnClickListener { onTitleClickListener?.invoke() }
    }

    fun setOnBackBtnClickListener(onBackBtnClickListener: () -> Unit)
    {
        this.onBackBtnClickListener = onBackBtnClickListener
    }

    fun setBtnList(btnList: List<TitleBarBtnBean.BaseBtnBean>)
    {
        btnListView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, true)
        btnListView.adapter = TitleBarBtnListAdapter(btnList)
    }

    fun updateBtn(btnBean: TitleBarBtnBean.BaseBtnBean)
    {
        if (btnListView.adapter !is TitleBarBtnListAdapter) return
        (btnListView.adapter as TitleBarBtnListAdapter).update(btnBean)
    }

    fun setBackBtnRedId(redId: Int)
    {
        backBtn.setImageResource(redId)
    }

    fun setTitleText(text: String)
    {
        titleTextView.text = text
    }

    fun startSearchMode(searchText: String, withTitleBtn: Boolean, onSearchTextChangeListener: (searchText: String) -> Unit)
    {
        searchEditText.isVisible = true
        backBtn.isVisible = true
        btnListView.isVisible = withTitleBtn
        titleTextView.isVisible = false
        inSearchMode = true
        searchEditText.setText(searchText)

        this.onSearchTextChangeListener = onSearchTextChangeListener

        UiUtil.showInputKeyboard(searchEditText)
    }

    fun stopSearchMode()
    {
        searchEditText.isVisible = false
        backBtn.isVisible = isBackBtnVisible
        btnListView.isVisible = true
        titleTextView.isVisible = true
        inSearchMode = false

        onSearchTextChangeListener = null

        searchEditText.setText("")
        UiUtil.hideInputKeyboard(searchEditText)
    }

    fun getSearchText() = searchEditText.text.toString()

    fun setOnTitleClickListener(onTitleClickListener: () -> Unit)
    {
        this.onTitleClickListener = onTitleClickListener
    }
}