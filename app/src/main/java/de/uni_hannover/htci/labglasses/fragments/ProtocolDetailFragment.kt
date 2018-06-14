package de.uni_hannover.htci.labglasses.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.activity.ProtocolDetailActivity
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.adapter.InstructionPagerAdapter
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Result
import de.uni_hannover.htci.labglasses.utils.isM300
import de.uni_hannover.htci.labglasses.views.KeyboardViewPager
import kotlinx.android.synthetic.main.protocol_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.support.v4.toast

/**
 * A fragment representing a single Protocol detail screen.
 * This fragment is either contained in a [ProtocolListActivity]
 * in two-pane mode (on tablets) or a [ProtocolDetailActivity]
 * on handsets.
 */
class ProtocolDetailFragment : Fragment(), AnkoLogger,
        ViewPager.OnPageChangeListener {

    interface BranchingDelegate  {
        fun selectBranchInstructionResult(current: Instruction)
    }

    private val protocol: Protocol by lazy {
        arguments.getParcelable<Protocol>(PROTOCOL_ITEM)
    }
    private var adapter: InstructionPagerAdapter? = null

    val currentInstruction: Instruction? get() = adapter?.instructionAtIndex(protocol_pager.currentItem)
    var currentVisiblePage: Int = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.protocol_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = InstructionPagerAdapter(childFragmentManager, protocol, mapOf())

        protocol_pager.adapter = adapter
        protocol_pager.addOnPageChangeListener(this)
        protocol_pager.navigationDelegate = (activity as KeyboardViewPager.NavigationDelegate)
        protocol_pager.touchPagingEnabled = false

        next_button.setOnClickListener { _ ->
            nextPage()
        }
        previous_button.setOnClickListener{ _ ->
            previousPage()
        }

        if(isM300()) {
            button_layout.visibility = View.GONE
        }

        activity.title = protocol.name

        protocol_pager.requestFocus()
    }



    override fun onPageSelected(position: Int) {
        adapter?.let {
            it.onPageHidden(currentVisiblePage)
            currentVisiblePage = position
            it.onPageVisible(currentVisiblePage)
        }
    }

    override fun onPageScrollStateChanged(state: Int) = Unit
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

    fun nextPage() {
        val current = protocol_pager.currentItem
        if(current < protocol_pager.adapter.count - 1) {
            // there is a page preloaded which is next
            // simply scroll there
            protocol_pager.currentItem = current + 1
        }
        else {
            // the next page is not preloaded therefore check if there is a next instruction after
            // this one
            val instruction = adapter?.instructionAtIndex(current)
            if(instruction == null) { error { "current instruction is unknown to adapter" }; return }
            // first make sure that the current instruction is finished. otherwise we can't forward anyway
            if(instruction.isPotentiallyUnfinishedInstruction) {
                val adapter = protocol_pager.adapter as InstructionPagerAdapter
                if(!adapter.isFinished(protocol_pager.currentItem)) {
                    toast("can't forward to next step, please finish this step first!").show()
                    return
                }
                // else falltrough to checking for branches
            }

            if(instruction.isBranchInstruction) {
                //ask delegate which instruction to take next
                (activity as BranchingDelegate).selectBranchInstructionResult(instruction)
            }
            else{
                //otherwise just add the next page unconditionally and scroll there
                instruction.nextInstructionId?.let {
                    protocol.instructionById(it)?.let {
                        adapter?.add(it)
                        protocol_pager.currentItem++
                    }
                }
            }

        }
    }

    fun nextPage(result: Result) {
        if (result.targetInstructionId == null) return
        val nextInstruction = protocol.instructionById(result.targetInstructionId)
        nextInstruction?.let {
            adapter?.add(it)
            protocol_pager.currentItem++
        }
    }

    fun previousPage() {
        val current = protocol_pager.currentItem
        if(current > 0) {
            protocol_pager.currentItem = current - 1
        }
    }

    fun onUpdatedMeasurements(measurements: Map<String, Double>) {
        adapter?.updateMeasurements(measurements)
    }

    fun onUpdatedInstruction(instruction: Instruction) {
        adapter?.updateInstruction(instruction)
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val PROTOCOL_ITEM = "protocol_item"
    }
}

