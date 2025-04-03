package com.wzh.base.view.custom

import android.content.Context
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class StaggeredDividerItemDecoration(
    private val context: Context,
    private val interval:Int = 5
) :ItemDecoration(){
}