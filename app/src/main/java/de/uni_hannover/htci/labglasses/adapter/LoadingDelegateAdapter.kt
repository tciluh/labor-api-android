package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.utils.inflate

/**
 * Created by sl33k on 11/8/17.
 */

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {
    override fun createViewHolder(parent: ViewGroup?): LoadingViewHolder {
        val view = parent?.inflate(R.layout.protocol_list_loading)
        return LoadingViewHolder(view)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) {
        //do nothing. everything is laid out in the xml
    }

    class LoadingViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}