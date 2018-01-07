package de.uni_hannover.htci.labglasses.fragments.pager

import android.support.v4.app.Fragment
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Result

/**
 * Created by sl33k on 1/5/18.
 */
const val INSTRUCTION_ITEM = "protocol_step_instruction"
const val RESULT_ITEM = "protocol_step_result"
//extend fragment inside this package to allow retrieval of instruction
val <T: Fragment> T.instruction: Instruction? get() = arguments.getParcelable(INSTRUCTION_ITEM)
val <T: Fragment> T.result: Result? get() = arguments.getParcelable(RESULT_ITEM)

