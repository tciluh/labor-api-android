package de.uni_hannover.htci.labglasses.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.TextView
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.model.Action
import de.uni_hannover.htci.labglasses.singletons.ErrorCallback
import de.uni_hannover.htci.labglasses.singletons.ResultCallback
import de.uni_hannover.htci.labglasses.singletons.SocketManager
import de.uni_hannover.htci.labglasses.utils.inflate
import kotlinx.android.synthetic.main.action_list_content.view.*

/**
 * Created by sl33k on 02.03.18.
 */
class ActionListAdapter(val actions: Array<Action>): RecyclerView.Adapter<ActionListAdapter.ActionViewHolder>() {
    interface ActionDelegate {
        fun handleAction(action: Action, cb: ResultCallback, errorCb: ErrorCallback)
    }
    var delegate: ActionDelegate? = null

    override fun getItemCount(): Int = actions.count()

    override fun onBindViewHolder(holder: ActionViewHolder?, position: Int) {
        val item = actions[position]
        holder?.button?.setOnClickListener { v ->
            //do something to actually measure the result
            val result = delegate?.handleAction(item, { res ->
                holder?.resultView?.text = res.toString()
            }, { error ->
                holder?.resultView?.text = "error: $error"
            })
        }
        holder?.actionView?.text = item.action
        holder?.identifierView?.text = item.identifier
        holder?.resultView?.text = "no data yet."

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ActionViewHolder {
        val view = parent?.inflate(R.layout.action_list_content)
        return ActionViewHolder(view)
    }

    class ActionViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        val identifierView: TextView = itemView?.action ?: throw IllegalStateException("cant find identifier view")
        val actionView: TextView = itemView?.identifier ?: throw IllegalStateException("cant find action view")
        val resultView: TextView = itemView?.result ?: throw IllegalStateException("cant find result view")
        val button: Button = itemView?.start_button ?: throw IllegalStateException("cant find button view")
    }
}
