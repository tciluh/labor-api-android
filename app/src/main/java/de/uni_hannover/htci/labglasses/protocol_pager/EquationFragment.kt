package de.uni_hannover.htci.labglasses.protocol_pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import kotlinx.android.synthetic.main.equation_instruction.*

/**
 * Created by sl33k on 1/5/18.
 */
class EquationFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.equation_instruction, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mathView.text = "`${instruction.equation}`"
    }
}