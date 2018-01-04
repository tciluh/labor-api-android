package de.uni_hannover.htci.labglasses

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.ProtocolStepFragment.Companion.INSTRUCTION_ITEM
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import kotlinx.android.synthetic.main.activity_protocol_detail.*
import kotlinx.android.synthetic.main.protocol_detail.*
import kotlinx.android.synthetic.main.protocol_step_page.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.bundleOf

/**
 * A fragment representing a single Protocol detail screen.
 * This fragment is either contained in a [ProtocolListActivity]
 * in two-pane mode (on tablets) or a [ProtocolDetailActivity]
 * on handsets.
 */
class ProtocolDetailFragment : Fragment(), AnkoLogger {

    private val protocol: Protocol by lazy {
        arguments.getParcelable<Protocol>(PROTOCOL_ITEM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        protocol.let {
            activity.detail_toolbar?.title = it.name
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.protocol_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        protocol_pager.adapter = ProtocolPagerAdapter(childFragmentManager, protocol)

    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val PROTOCOL_ITEM = "protocol_item"
    }
}

class ProtocolPagerAdapter(fm: FragmentManager?, private val protocol: Protocol) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val f = ProtocolStepFragment()
        val b = bundleOf("num" to position, INSTRUCTION_ITEM to protocol.instructions[position])
        f.arguments = b
        return f
    }
    override fun getCount(): Int {
        return protocol.instructions.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return protocol.instructions[position].id.toString()
    }
}

class ProtocolStepFragment: Fragment() {
    private val num: Int by lazy {
        arguments.getInt("num")
    }
    private val instruction: Instruction by lazy {
        arguments.getParcelable<Instruction>(INSTRUCTION_ITEM)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.protocol_step_page, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        textView.text = "Fragment # " + num.toString() + "\n" + "Instruction Description:\n" + instruction.description
    }

    companion object {
        const val INSTRUCTION_ITEM = "protocol_step_instruction"
    }

}
