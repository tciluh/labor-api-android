package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by sl33k on 11/9/17.
 */
class ProtocolListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //init items with loading item preset
    private val loadingItem = object : ViewType {
        override val viewType = AdapterType.LOADING
    }
    private var items: ArrayList<ViewType> = arrayListOf(loadingItem)
    //init delegate adapters
    private val delegateAdapters = mapOf(
            AdapterType.LOADING.ordinal to LoadingDelegateAdapter(),
            AdapterType.PROTOCOL.ordinal to ProtocolDelegateAdapter()
    )

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType.ordinal


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return delegateAdapters[viewType]?.createViewHolder(parent) ?: throw IllegalStateException("cant find adapter for viewType $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val item = items[position]
        val type = item.viewType.ordinal
        delegateAdapters[type]?.bindViewHolder(holder, item)
    }


}