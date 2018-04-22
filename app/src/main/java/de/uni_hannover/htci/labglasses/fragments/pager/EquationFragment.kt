package de.uni_hannover.htci.labglasses.fragments.pager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import kotlinx.android.synthetic.main.equation_instruction.*
import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Constant

/**
 * Created by sl33k on 1/5/18.
 * This fragment displays a simple math equation with automatic calculation with the passed
 * measurements map.
 */
class EquationFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.equation_instruction, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        measurements?.let {
            val args: List<Argument> = it.map { kv ->
                Argument(kv.key, kv.value)
            }
            val input = instruction?.equation
            if (input != null) {
                val expr = Constant(input, *(args.toTypedArray()))
                mathView.text = "`${expr.constantName} = ${expr.constantValue}`"
            }
        }
    }
}