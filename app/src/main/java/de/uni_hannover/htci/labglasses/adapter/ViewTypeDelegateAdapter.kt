package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by sl33k on 11/8/17.
 */
interface ViewTypeDelegateAdapter{

    //selection handler consumes an item type and returns nothing
    val selectHandler: (ViewType) -> Unit

    fun createViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder
    fun bindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType)
}