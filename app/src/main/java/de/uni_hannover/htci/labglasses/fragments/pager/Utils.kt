package de.uni_hannover.htci.labglasses.fragments.pager

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Result


/**
 * Created by sl33k on 1/5/18.
 */
interface UpdateableFragment {
    fun onArgumentsChanged();
}

const val INSTRUCTION_ITEM = "protocol_step_instruction"
const val RESULT_ITEM = "protocol_step_result"
const val MEASUREMENTS_ITEM = "protocol_step_measurements"
//extend fragment inside this package to allow retrieval of instruction
val <T: Fragment> T.instruction: Instruction? get() = arguments?.getParcelable(INSTRUCTION_ITEM)
val <T: Fragment> T.result: Result? get() = arguments?.getParcelable(RESULT_ITEM)
val <T: Fragment> T.measurements: Map<String, Double>? get() = arguments?.getSerializable(MEASUREMENTS_ITEM) as? HashMap<String, Double>
