package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.model.Protocol
import kotlin.math.abs

/**
 * Created by sl33k on 11/9/17.
 */
class ProtocolListAdapter(onSelectHandler: (ViewType) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var items: ArrayList<ViewType> = ArrayList()
    //init delegate adapters
    private val delegateAdapters = mapOf(
            AdapterType.LOADING.ordinal to LoadingDelegateAdapter(onSelectHandler),
            AdapterType.PROTOCOL.ordinal to ProtocolDelegateAdapter(onSelectHandler)
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
        //add items
        val initialSize = items.size
        if (items.addAll(protocols)) {
            notifyItemRangeInserted(initialSize - 1, protocols.size)
        } else {
            throw IllegalStateException("can't insert protocols into items")
        }
    }

    fun setProtocols(protocols: List<Protocol>){
        val prev = items.size
        val diff = protocols.size - prev
        items.clear()
        items.addAll(protocols)
        when {
            diff == 0 -> notifyItemRangeChanged(0, prev)
            diff < 0 -> {
                notifyItemRangeChanged(0, prev + diff)
                notifyItemRangeRemoved(prev + diff - 1, abs(diff))
            }
            diff > 0 -> {
                notifyItemRangeChanged(0, prev)
                notifyItemRangeInserted(prev, diff)
            }
        }
    }


}