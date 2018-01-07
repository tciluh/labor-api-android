package de.uni_hannover.htci.labglasses.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.adapter.ResultPagerAdapter
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Result
import kotlinx.android.synthetic.main.result_dialog.*

/**
 * Created by sl33k on 1/7/18.
 */
class ResultDialogFragment: DialogFragment() {
    interface ResultSelectionDelegate {
        fun onResultSelect(result: Result)
    }
    var resultDelegate: ResultSelectionDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.TitledDialogFragment)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View?
           = inflater?.inflate(R.layout.result_dialog, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        result_pager.adapter = ResultPagerAdapter(childFragmentManager, instruction)
        result_pager_tabstrip.drawFullUnderline = false
        dialog_cancel_button.setOnClickListener {
            this.dismiss()
        }
        dialog_okay_button.setOnClickListener{
            val result = instruction.results[result_pager.currentItem]
            resultDelegate?.onResultSelect(result)
            this.dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(getString(R.string.result_dialog_title))
        return dialog
    }

    private val instruction: Instruction get() = arguments.getParcelable(DIALOG_INSTRUCTION_ITEM)

    companion object {
        const val DIALOG_INSTRUCTION_ITEM = "instruction-item-result-dialog"
        const val DIALOG_SELECTION_DELEGATE_ITEM = "delegate-result-dialog"
    }
}