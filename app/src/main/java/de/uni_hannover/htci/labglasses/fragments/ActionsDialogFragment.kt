package de.uni_hannover.htci.labglasses.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.adapter.ActionListAdapter
import de.uni_hannover.htci.labglasses.model.Action
import de.uni_hannover.htci.labglasses.singletons.ErrorCallback
import de.uni_hannover.htci.labglasses.singletons.ResultCallback
import de.uni_hannover.htci.labglasses.singletons.SocketManager
import kotlinx.android.synthetic.main.action_list.*
import kotlinx.android.synthetic.main.action_list_content.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.longToast

/**
 * Created by sl33k on 02.03.18.
 * This is the dialog that gets shown when doing measurements.
 */

class ActionsDialogFragment: DialogFragment(), AnkoLogger, ActionListAdapter.ActionDelegate {
    interface MeasurementResultDelegate {
        fun onMeasurementCompleted(action: Action, result: Any)
    }
    var measurementDelegate: MeasurementResultDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(android.support.v4.app.DialogFragment.STYLE_NORMAL, R.style.TitledDialogFragment)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater?.inflate(R.layout.action_list, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        actionRecyclerView.adapter = ActionListAdapter(actions).also { it.delegate = this }
        actionRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(getString(R.string.action_dialog_title))
        return dialog
    }

    override fun handleAction(action: Action, cb: ResultCallback, errorCb: ErrorCallback) {
        SocketManager.getInstance(this.activity).sendAction(action, {
            res ->
            activity.runOnUiThread {
                measurementDelegate?.onMeasurementCompleted(action, res)
                cb(res)
            }
        }, {
            error ->
            activity.runOnUiThread {
                errorCb(error)
            }

        })
    }

    private val actions: Array<Action> get() = arguments.getParcelableArray(DIALOG_ACTIONS_ITEM) as Array<Action>

    companion object {
        const val DIALOG_ACTIONS_ITEM = "actions-item-actions-dialog"
    }
}