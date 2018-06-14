package de.uni_hannover.htci.labglasses.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.fragments.pager.*
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by sl33k on 1/5/18.
 */
class InstructionPagerAdapter(fm: FragmentManager?, protocol: Protocol, private var measurements: Map<String, Double>) : FragmentStatePagerAdapter(fm) {
    private var displayedInstructions: ArrayList<Instruction> = arrayListOf(protocol.firstInstruction)
    data class FragmentInformation(val fragment: Fragment, val type: Instruction.Companion.InstructionType)
    private val fragmentInfo: MutableMap<Int, FragmentInformation> = mutableMapOf()
    private var instantiated = false

    init {
        //add all non branching instructions after the first instruction to be viewable
        var current = protocol.firstInstruction
        while(true) {
            if(current.isBranchInstruction || current.isPotentiallyUnfinishedInstruction) break
            val nextId: Int? = current. nextInstructionId
            if (nextId != null) {
                val nextInstruction = protocol.instructionById(nextId)
                if (nextInstruction != null) {
                    current = nextInstruction
                    displayedInstructions.add(current)
                } else {
                    break
                }
            } else {
                break
            }
        }
    }

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        return super.instantiateItem(container, position).also {
            if(it is PageContainerFragment){
                fragmentInfo[position] = FragmentInformation(it, displayedInstructions[position].type)
            }
        }
    }

    override fun destroyItem(container: ViewGroup?, position: Int, fragment: Any?) {
        super.destroyItem(container, position, fragment)
        if(fragment is PageContainerFragment) {
            fragmentInfo.remove(position)
        }
    }

    override fun finishUpdate(container: ViewGroup?) {
        super.finishUpdate(container)
        if(!instantiated) {
            instantiated = true
            onPageVisible(0)
        }
    }

    override fun getItem(position: Int): Fragment {
        val instruction = displayedInstructions[position]
        val fragment = PageContainerFragment()
        fragment.arguments = generatePageFragmentBundle(position)
        return fragment
    }
    override fun getCount(): Int = displayedInstructions.size

    override fun getPageTitle(position: Int): CharSequence = "Step ${position + 1}"

    val currentInstruction: Instruction get() = displayedInstructions.last()

    fun instructionAtIndex(index: Int): Instruction? = displayedInstructions.getOrNull(index)

    private fun generatePageFragmentBundle(position: Int, previous: Bundle? = null): Bundle {
        return (previous ?: Bundle()).also {
            it.putParcelable(INSTRUCTION_ITEM, instructionAtIndex(position))
            it.putSerializable(MEASUREMENTS_ITEM, measurements.toMap(HashMap()))
        }
    }

    fun add(instruction: Instruction) {
        displayedInstructions.add(instruction)
        notifyDataSetChanged()
    }

    fun updateMeasurements(measurements: Map<String,Double>) {
        this.measurements = measurements
        for((position, info) in fragmentInfo){
            if(info.type == Instruction.Companion.InstructionType.Equation
                    && info.fragment is UpdateableFragment) {
                info.fragment.arguments = generatePageFragmentBundle(position)
                info.fragment.onArgumentsChanged()
            }
        }
    }

    fun updateInstruction(instruction: Instruction) {
        val currentPos = displayedInstructions.indexOfFirst { instr -> instr.id == instruction.id }
        if(currentPos == -1) error("couldn't find instruction: $instruction in displayedInstructions")
        val info = fragmentInfo[currentPos]
        displayedInstructions[currentPos] = instruction
        if(info != null
                && info.fragment is UpdateableFragment
                && info.type == Instruction.Companion.InstructionType.Simple) {
            generatePageFragmentBundle(currentPos, info.fragment.arguments)
            info.fragment.onArgumentsChanged()
        }
    }

    fun onPageVisible(index: Int) {
        val info = fragmentInfo[index]
        if(info != null
                && info.fragment is PagingAwareFragment) {
            info.fragment.onPageVisible()
        }
    }

    fun onPageHidden(index: Int) {
        val info = fragmentInfo[index]
        if(info != null
                && info.fragment is PagingAwareFragment) {
            info.fragment.onPageHidden()
        }
    }

    fun isFinished(index: Int): Boolean {
        val info = fragmentInfo[index]
        if(info != null && info.fragment is InstructionFragment){
            return info.fragment.isFinished()
        }
        return true
    }
}