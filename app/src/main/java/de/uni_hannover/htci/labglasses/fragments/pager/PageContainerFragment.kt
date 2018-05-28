package de.uni_hannover.htci.labglasses.fragments.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.utils.withTransaction
import kotlinx.android.synthetic.main.base_instruction.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.withArguments

/**
 * Created by sl33k on 1/5/18.
 */
open class PageContainerFragment: Fragment(), AnkoLogger, UpdateableFragment, PagingAwareFragment {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(savedInstanceState == null) {
            val fragment: Fragment = when(instruction?.type){
                null -> result?.let { SimpleFragment() } ?: error("neither instruction nor result is defined. aborting!")
                Instruction.Companion.InstructionType.Simple -> SimpleFragment()
                Instruction.Companion.InstructionType.Invalid -> error("got instruction with type invalid")
                Instruction.Companion.InstructionType.Timer -> TimerFragment()
                Instruction.Companion.InstructionType.Equation -> EquationFragment()
            }
            this.childFragmentManager.withTransaction {
                add(R.id.contentContainer,
                        fragment.withArguments(
                                INSTRUCTION_ITEM to instruction,
                                RESULT_ITEM to result,
                                MEASUREMENTS_ITEM to measurements
                        ))
            }
        }
        else {
            val fragment = this.childFragmentManager.findFragmentById(R.id.contentContainer)
            updateArguments(fragment.arguments)
        }
        return inflater?.inflate(R.layout.base_instruction, container, false)
    }

    private fun updateArguments(old: Bundle) {
        old.putParcelable(INSTRUCTION_ITEM, instruction)
        old.putSerializable(MEASUREMENTS_ITEM, measurements?.toMap(HashMap()))
        old.putParcelable(RESULT_ITEM, result)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //set the description text view
        //first try for an instruction
        instruction?.let {
           descriptionTextView.text = it.description
        }
        //then for a result
        result?.let {
           descriptionTextView.text = it.description
        }
    }

    override fun onArgumentsChanged() {
        val fragment = this.childFragmentManager.findFragmentById(R.id.contentContainer)
        fragment?.let {
            if(it is UpdateableFragment){
                updateArguments(it.arguments)
                it.onArgumentsChanged()
            }
        }
    }

    override fun onPageHidden() {
        val fragment = this.childFragmentManager.findFragmentById(R.id.contentContainer)
        fragment?.let {
            if(it is PagingAwareFragment){
                it.onPageHidden()
            }
        }
    }

    override fun onPageVisible() {
        val fragment = this.childFragmentManager.findFragmentById(R.id.contentContainer)
        fragment?.let {
            if(it is PagingAwareFragment){
                it.onPageVisible()
            }
        }
    }
}