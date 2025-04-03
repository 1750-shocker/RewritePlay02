package com.wzh.base.view.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerAdapter<V : ViewBinding> :
    RecyclerView.Adapter<BaseRecyclerAdapter.BaseRecyclerHolder<V>>() {

    open class BaseRecyclerHolder<V : ViewBinding>(val binding: V) :
        RecyclerView.ViewHolder(binding.root)

    abstract override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType:Int
    ):BaseRecyclerHolder<V>
    override fun onBindViewHolder(holder:BaseRecyclerHolder<V>, position:Int){
        onBaseBindViewHolder(holder.binding, position)
    }
    //把数据绑定逻辑抽离出来，后续基类可添加其他逻辑
    abstract fun onBaseBindViewHolder(binding:V, position:Int)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}