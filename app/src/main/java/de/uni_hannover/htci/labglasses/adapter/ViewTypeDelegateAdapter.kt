package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by sl33k on 11/8/17.
 */
interface ViewTypeDelegateAdapter<T : RecyclerView.ViewHolder> {
    fun createViewHolder(parent: ViewGroup?): T
    fun bindViewHolder(holder: T, item: ViewType)
}