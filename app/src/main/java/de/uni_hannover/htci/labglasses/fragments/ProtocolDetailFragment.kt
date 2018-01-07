package de.uni_hannover.htci.labglasses.fragments

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.activity.ProtocolDetailActivity
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.adapter.InstructionPagerAdapter
import de.uni_hannover.htci.labglasses.model.Result
import kotlinx.android.synthetic.main.protocol_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.support.v4.withArguments

/**
 * A fragment representing a single Protocol detail screen.
 * This fragment is either contained in a [ProtocolListActivity]
 * in two-pane mode (on tablets) or a [ProtocolDetailActivity]
 * on handsets.
 */
class ProtocolDetailFragment : Fragment(), AnkoLogger, ViewPager.OnPageChangeListener, ProtocolDetailActivity.PagingToolbarDelegate, ResultDialogFragment.ResultSelectionDelegate {

    private val protocol: Protocol by lazy {
        arguments.getParcelable<Protocol>(PROTOCOL_ITEM)
    }
    private var adapter: InstructionPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.protocol_detail, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = InstructionPagerAdapter(childFragmentManager, protocol)
        protocol_pager.adapter = adapter
        protocol_pager.addOnPageChangeListener(this)
        activity.title = protocol.name
    }

    override fun onPageSelected(position: Int) {
        adapter?.let {
           if(position == it.count - 1)  {
               val instruction = it.instructionAtIndex(position)
               if(instruction != null && !instruction.isBranchInstruction) {
                   val nextId = instruction.nextInstructionId
                   if(nextId != null) {
                       val nextInstruction = protocol.instructionById(nextId)
                       if(nextInstruction != null) {
                           it.add(nextInstruction)
                       }
                   }
               }
           }
        }
    }
    override fun onPageScrollStateChanged(state: Int) = when(state)  {
           SCROLL_STATE_SETTLING -> {
               Handler().postDelayed({
                   toast(getString(R.string.protocol_pager_step_x,
                           protocol_pager.currentItem + 1)).show()
               }, 200)
               Unit
           }
           else -> Unit
        }
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) = Unit

    override fun onNextPage() {
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
            if(instruction != null && instruction.isBranchInstruction) {
               // show some dialog to decide
                //which route to take
                ResultDialogFragment()
                        .withArguments(ResultDialogFragment.DIALOG_INSTRUCTION_ITEM to instruction)
                        .also{
                            it.resultDelegate = this
                        }
                        .show(fragmentManager, "result-dialog")
            }
            else{
                //otherwise just add the next page unconditionally and scroll there
                instruction?.nextInstructionId?.let {
                    protocol.instructionById(it)?.let {
                        adapter?.add(it)
                        protocol_pager.currentItem++
                    }
                }
            }

        }
    }

    override fun onPreviousPage() {
        val current = protocol_pager.currentItem
        if(current > 0) {
            protocol_pager.currentItem = current - 1
        }
    }

    // called by the dialog when a result is selected
    override fun onResultSelect(result: Result) {
        if (result.targetInstructionId == null) return
        val nextInstruction = protocol.instructionById(result.targetInstructionId)
        nextInstruction?.let {
            adapter?.add(it)
            protocol_pager.currentItem++
        }
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val PROTOCOL_ITEM = "protocol_item"
    }
}

