package de.uni_hannover.htci.labglasses.protocol_pager

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
open class BaseFragment: Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragment: Fragment
        when(instruction.type){
            Instruction.Companion.InstructionType.Simple -> fragment = SimpleFragment()
            Instruction.Companion.InstructionType.Invalid -> {
                error("got invalid fragment")
                fragment = SimpleFragment()
            }
            Instruction.Companion.InstructionType.Timer -> fragment = TimerFragment()
            Instruction.Companion.InstructionType.Equation -> fragment = EquationFragment()
        }
        this.childFragmentManager.withTransaction {
            add(R.id.contentContainer, fragment.withArguments(INSTRUCTION_ITEM to instruction))
        }

        return inflater?.inflate(R.layout.base_instruction, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //set the description text view
        descriptionTextView.text = instruction.description
    }
}