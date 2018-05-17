package de.uni_hannover.htci.labglasses.adapter

import android.provider.Settings.Global.getString
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.TextView
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.model.Action
import de.uni_hannover.htci.labglasses.model.Action.MeasurementState;
import de.uni_hannover.htci.labglasses.utils.inflate
import kotlinx.android.synthetic.main.action_list_content.view.*


/**
 * Created by sl33k on 02.03.18.
 */
class ActionListAdapter(var actions: Array<Action>): RecyclerView.Adapter<ActionListAdapter.ActionViewHolder>() {
    interface ActionDelegate {
        fun handleAction(action: Action)
    }
    var delegate: ActionDelegate? = null

    init {
        this.actions = this.actions.map { a ->
            a.copy()
        }.toTypedArray() // copy our own set for updating later
    }

    override fun getItemCount(): Int = actions.count()

    override fun onBindViewHolder(holder: ActionViewHolder?, position: Int) {
        if(holder == null) return;

        val item = actions[position]
        holder.button.setOnClickListener { _ ->
            delegate?.handleAction(item)
        }
        holder.actionView.text = item.action
        holder.identifierView.text = item.identifier
        when(item.state) {
            MeasurementState.NotStarted -> {
                holder.resultView.text = holder.resultView.context.getString(R.string.action_item_not_started)
            }
            MeasurementState.InProgress -> {
                holder.resultView.text = holder.resultView.context.getString(R.string.action_item_inprogress)
            }
            MeasurementState.Done -> {
                holder.resultView.text = item.result
            }
            MeasurementState.Error -> {
                holder.resultView.text = holder.resultView.context.getString(R.string.action_item_error)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ActionViewHolder {
        val view = parent?.inflate(R.layout.action_list_content)
        return ActionViewHolder(view)
    }

    fun updateActions(newActions: Array<Action>) {
        val updatedPositions = mutableListOf<Int>()
        for(action in newActions) {
            val corresponding = actions.find { it == action } //checks for id
            if(corresponding?.state != action.state || corresponding.amountOfResults != action.amountOfResults) {
                updatedPositions.add(actions.indexOf(corresponding))
            }
        }
        this.actions = newActions.map { it.copy() }.toTypedArray()
        for(pos in updatedPositions) {
            notifyItemChanged(pos)
        }
    }

    class ActionViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        val identifierView: TextView = itemView?.action ?: throw IllegalStateException("cant find identifier view")
        val actionView: TextView = itemView?.identifier ?: throw IllegalStateException("cant find action view")
        val resultView: TextView = itemView?.result ?: throw IllegalStateException("cant find result view")
        val button: Button = itemView?.start_button ?: throw IllegalStateException("cant find button view")
    }
}
