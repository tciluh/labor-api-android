package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by sl33k on 11/8/17.
 */
interface ViewTypeDelegateAdapter {
    fun createViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder
    fun bindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType)
}