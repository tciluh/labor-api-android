package de.uni_hannover.htci.labglasses.protocol_pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R

/**
 * Created by sl33k on 1/5/18.
 */

class TimerFragment: BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.timer_instruction, container, false)
    }
}
