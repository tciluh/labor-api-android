package de.uni_hannover.htci.labglasses.fragments

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.adapter.ResultPagerAdapter
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Result
import de.uni_hannover.htci.labglasses.views.KeyboardViewPager
import kotlinx.android.synthetic.main.result_dialog.*

/**
 * Created by sl33k on 1/7/18.
 */
class ResultDialogFragment: DialogFragment(), KeyboardViewPager.NavigationDelegate {
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
        result_pager.navigationDelegate = this
        dialog_cancel_button.setOnClickListener {
            this.dismissAllowingStateLoss()
        }
        dialog_okay_button.setOnClickListener{
            val result = instruction.results[result_pager.currentItem]
            resultDelegate?.onResultSelect(result)
            this.dismissAllowingStateLoss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setTitle(getString(R.string.result_dialog_title))
        return dialog
    }

    override fun onLeftDpad() {
        result_pager.currentItem--
    }

    override fun onRightDpad() {
        result_pager.currentItem++
    }

    override fun onCenterDpad() {
        val result = instruction.results[result_pager.currentItem]
        resultDelegate?.onResultSelect(result)
        this.dismissAllowingStateLoss()
    }

    private val instruction: Instruction get() = arguments.getParcelable(DIALOG_INSTRUCTION_ITEM)

    companion object {
        const val DIALOG_INSTRUCTION_ITEM = "instruction-item-result-dialog"
        const val DIALOG_SELECTION_DELEGATE_ITEM = "delegate-result-dialog"
    }
}