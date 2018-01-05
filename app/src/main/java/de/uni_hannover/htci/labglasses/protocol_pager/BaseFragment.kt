package de.uni_hannover.htci.labglasses.protocol_pager

import android.support.v4.app.Fragment
import de.uni_hannover.htci.labglasses.model.Instruction
import org.jetbrains.anko.AnkoLogger

/**
 * Created by sl33k on 1/5/18.
 */
open class BaseFragment: Fragment(), AnkoLogger {
    protected val instruction: Instruction by lazy {
        arguments.getParcelable<Instruction>(INSTRUCTION_ITEM)
    }
    companion object {
        const val INSTRUCTION_ITEM = "protocol_step_instruction"
    }
}