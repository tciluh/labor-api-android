package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.utils.inflate
import kotlinx.android.synthetic.main.protocol_list_content.view.*

/**
 * Created by sl33k on 11/8/17.
 */
class ProtocolDelegateAdapter(override val selectHandler: (ViewType) -> Unit)
    : ViewTypeDelegateAdapter {

    private val logTag = "ProtocolDelegateAdapter"

    override fun createViewHolder(parent: ViewGroup?): ViewHolder {
        val view = parent?.inflate(R.layout.protocol_list_content)
        return ViewHolder(view)
    }

    override fun bindViewHolder(holder: RecyclerView.ViewHolder?, item: ViewType) {
        val viewHolder = holder as? ViewHolder ?: throw IllegalStateException("can't cast ViewHolder in bindViewHolder")
        if (item is Protocol) {
            viewHolder.nameView.text = item.name
            viewHolder.descriptionView.text = item.description
            //bind click handler
            viewHolder.itemView.setOnClickListener { selectHandler(item) }
        } else {
            Log.e(logTag, "item is not of Protocol Type (is ${item.javaClass}) can't bind view")
        }
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView?.name ?: throw IllegalStateException("cant find id_text view in protocol_list_content")
        val descriptionView: TextView = itemView?.description ?: throw IllegalStateException("cant find content view in protocol_list_content")
    }
}