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
class ProtocolAdapter : ViewTypeDelegateAdapter<ProtocolAdapter.ViewHolder> {
    val TAG = "ProtocolAdapter"

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val view = parent.inflate(R.layout.protocol_list_content)
        return ViewHolder(view)
    }

    override fun bindViewHolder(holder: ViewHolder, item: ViewType) {
        if (item is Protocol) {
            holder.idView.text = item.id.toString()
            holder.contentView.text = item.description
        } else {
            Log.e(TAG, "item is not of Protocol Type (is ${item.javaClass}) can't bind view")
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idView: TextView = itemView.id_text
        val contentView: TextView = itemView.content
    }
}