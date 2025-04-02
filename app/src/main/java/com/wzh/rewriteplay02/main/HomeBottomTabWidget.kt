package com.wzh.rewriteplay02.main

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.databinding.LayoutHomeBottomTabBinding

class HomeBottomTabWidget @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseHomeBottomTabWidget(context, attrs, defStyleAttr), View.OnClickListener {
    private var textViews: ArrayList<TextView> = arrayListOf()

    //设置点击事件
    init {
        //加载这个布局纯粹只是为了拿到里面的textView
        val view = LayoutHomeBottomTabBinding.inflate(LayoutInflater.from(context), this, true)
        //搞个列表就是为了方便遍历设置点击事件
        view.apply { textViews = arrayListOf(tvHomePage, tvProject, tvOfficialAccount, tvMine) }
        for (textView in textViews) {
            textView.setOnClickListener(this)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tv_home_page -> changeFragment(0)
            R.id.tv_project -> changeFragment(1)
            R.id.tv_official_account -> changeFragment(2)
            R.id.tv_mine -> changeFragment(3)
        }
    }

    override fun destroy() {
        super.destroy()
        if (textViews.isNotEmpty()) {
            textViews.clear()
        }
    }


    //扩充父类的方法，实现点击效果
    override fun changeFragment(position: Int) {
        super.changeFragment(position)
        for (j in textViews.indices) {
            textViews[j].isSelected = position == j
        }
    }

}