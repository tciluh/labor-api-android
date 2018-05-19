package de.uni_hannover.htci.labglasses.adapter

import android.os.Handler
import android.os.Looper
import android.provider.Settings.Global.getString
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
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
        fun handleAction(action: Action, onFinish: (() -> Unit)? = null)
    }
    var delegate: ActionDelegate? = null

    init {
        this.actions = this.actions.map { a ->
            a.copy()
        }.toTypedArray() // copy our own set for updating later
    }

    override fun getItemCount(): Int = actions.count()

    override fun onBindViewHolder(holder: ActionViewHolder?, position: Int) {
        if(holder == null) return

        val item = actions[position]
        holder.actionView.text = item.action
        holder.identifierView.text = item.identifier
        holder.errorButton.setOnClickListener {
            delegate?.handleAction(item)
        }
        holder.itemView.setOnClickListener {
            val item = this.actions[position]
            if(item.state != MeasurementState.InProgress) {
                delegate?.handleAction(item)
            }
        }

        when(item.state) {
            MeasurementState.NotStarted -> {
                holder.resultView.text = holder.resultView.context.getString(R.string.action_item_not_started)
                holder.resultView.visibility = View.VISIBLE
                holder.errorButton.visibility = View.INVISIBLE
                holder.progressSpinner.visibility = View.INVISIBLE
            }
            MeasurementState.InProgress -> {
                holder.resultView.visibility = View.INVISIBLE
                holder.errorButton.visibility = View.INVISIBLE
                holder.progressSpinner.visibility = View.VISIBLE

            }
            MeasurementState.Done -> {
                holder.resultView.text = item.result

                holder.resultView.visibility = View.VISIBLE
                holder.errorButton.visibility = View.INVISIBLE
                holder.progressSpinner.visibility = View.INVISIBLE
            }
            MeasurementState.Error -> {
                holder.resultView.visibility = View.INVISIBLE
                holder.errorButton.visibility = View.VISIBLE
                holder.progressSpinner.visibility = View.INVISIBLE
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

    fun startNextPendingAction() {
        val action = actions.firstOrNull { a -> a.state == MeasurementState.NotStarted }
        action?.let {
            delegate?.handleAction(it, {
                startNextPendingAction()
            })
        }
    }

    class ActionViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        val identifierView: TextView = itemView?.identifier ?: throw IllegalStateException("cant find identifier view")
        val actionView: TextView = itemView?.action ?: throw IllegalStateException("cant find action view")
        val resultView: TextView = itemView?.result ?: throw IllegalStateException("cant find result view")
        val progressSpinner: ProgressBar = itemView?.inProgressSpinner ?: throw IllegalStateException("cant find progress spinner")
        val errorButton: ImageView = itemView?.errorButton ?: throw IllegalStateException("cant find error view")
    }
}
