package com.wzh.base.view.custom

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import com.wzh.base.R
import com.wzh.base.databinding.LayoutTitleBinding
import com.wzh.base.util.hideIme

class TitleBar @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(mContext, attrs, defStyleAttr), View.OnClickListener {
    private var mTitleTv: TextView
    private var mImgBack: ImageView
    private var mImgRight: ImageView
    private var mTxtRight: TextView
    private var titleName: String? = null
    private var backImageVisible: Boolean? = null

    init {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        titleName = attr.getString(R.styleable.TitleBar_titleName)
        backImageVisible = attr.getBoolean(R.styleable.TitleBar_backImageVisible, true)
        attr.recycle()
    }

    init {
        View.inflate(mContext, R.layout.layout_title, this)
        val binding = LayoutTitleBinding.inflate(LayoutInflater.from(context), this, true)
        binding.apply {
            mImgBack = imgBack
            mTitleTv = txtTitle
            mImgRight = imgRight
            mTxtRight = txtRight
        }
        mImgBack.setOnClickListener(this)
        mTitleTv.text = titleName ?: ""
        setBackImageVisible(backImageVisible ?: true)
    }

    fun setTitle(title: String?) {
        mTitleTv.text = title
    }

    fun setBackImage(imageId: Int) {
        if (imageId != 0) {
            mImgBack.setImageResource(imageId)
        }
    }

    fun setBackImageVisible(imageVisible: Boolean) {
        mImgBack.isVisible = imageVisible
    }

    fun setRightImage(imageId: Int) {
        if (imageId != 0) {
            require(mTxtRight.visibility != View.VISIBLE) { "文字和图片不可以同时设置" }
            mImgRight.visibility = View.VISIBLE
            mImgRight.setImageResource(imageId)
        }
    }

    fun setRightText(text: String?) {
        if (text != null) {
            require(mImgRight.visibility != View.VISIBLE) { "文字和图片不可以同时设置" }
            mTxtRight.visibility = View.VISIBLE
            mTxtRight.text = text
        }
    }

    fun setRightBackColor(color: Int) {
        mTxtRight.setBackgroundColor(color)
    }

    fun setRightTextClick(click: Boolean) {
        mTxtRight.isClickable = click
    }

    override fun onClick(v: View) {
        if (v.id == R.id.imgBack) {
            (mContext as Activity).hideIme()
            mContext.finish()
        }
    }
    fun setRightTextOnClickListener(onClickListener: OnClickListener) {
        mTxtRight.setOnClickListener(onClickListener)
    }
    fun setRightImgOnClickListener(onClickListener: OnClickListener) {
        mImgRight.setOnClickListener(onClickListener)
    }
    fun setTitleOnClickListener(onClickListener: OnClickListener) {
        mTitleTv.setOnClickListener(onClickListener)
    }

}