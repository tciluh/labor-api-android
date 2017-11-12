package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.model.Protocol

/**
 * Created by sl33k on 11/9/17.
 */
class ProtocolListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var items: ArrayList<ViewType> = ArrayList()
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

    fun addProtocols(protocols: List<Protocol>) {
        //remove loading
        //val index = items.indexOf(loadingItem)
        //items.removeAt(index)
        //notifyItemRemoved(index)
        //add items
        val initialSize = items.size
        if (items.addAll(protocols)) {
            notifyItemRangeInserted(initialSize - 1, protocols.size)
        } else {
            throw IllegalStateException("can't insert protocols into items")
        }
    }


}