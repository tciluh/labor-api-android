package de.uni_hannover.htci.labglasses.protocol_pager

import android.support.v4.app.Fragment
import de.uni_hannover.htci.labglasses.model.Instruction

/**
 * Created by sl33k on 1/5/18.
 */
const val INSTRUCTION_ITEM = "protocol_step_instruction"
//extend fragment inside this package to allow retrieval of instruction
val <T: Fragment> T.instruction: Instruction get() = arguments.getParcelable(INSTRUCTION_ITEM)

