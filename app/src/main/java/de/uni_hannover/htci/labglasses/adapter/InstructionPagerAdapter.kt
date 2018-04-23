package de.uni_hannover.htci.labglasses.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import de.uni_hannover.htci.labglasses.fragments.pager.*
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by sl33k on 1/5/18.
 */
class InstructionPagerAdapter(fm: FragmentManager?, private val protocol: Protocol, private var measurements: Map<String, Double>) : FragmentStatePagerAdapter(fm) {
    private var displayedInstructions: ArrayList<Instruction> = arrayListOf(protocol.firstInstruction)
    init {
        //add all non branching instructions after the first instruction to be viewable
        var current = protocol.firstInstruction
        while(true) {
            val nextId: Int? = current.nextInstructionId
            if(nextId != null) {
                val nextInstruction = protocol.instructionById(nextId)
                if(nextInstruction != null){
                    current = nextInstruction
                    displayedInstructions.add(current)
                }
                else{
                    break
                }
            }
            else{
                break
            }

        }
    }

    override fun getItem(position: Int): Fragment {
        val instruction = displayedInstructions[position]
        val fragment = PageContainerFragment()
        val bundle = Bundle()
        bundle.putParcelable(INSTRUCTION_ITEM, instruction)
        bundle.putSerializable(MEASUREMENTS_ITEM, measurements.toMap(HashMap()))
        fragment.arguments = bundle
        return fragment
    }
    override fun getCount(): Int = displayedInstructions.size

    override fun getPageTitle(position: Int): CharSequence = "Step ${position + 1}"

    override fun getItemPosition(obj: Any?): Int = when(obj) {
        is PageContainerFragment -> if(obj.instruction?.type == Instruction.Companion.InstructionType.Equation) {
                PagerAdapter.POSITION_NONE
            }
            else PagerAdapter.POSITION_UNCHANGED // always update EquationFragments on notifyDataSetChanged()
        else -> PagerAdapter.POSITION_UNCHANGED
    }



    val currentInstruction: Instruction get() = displayedInstructions.last()

    fun instructionAtIndex(index: Int): Instruction? = displayedInstructions.getOrNull(index)

    fun add(instruction: Instruction) {
        displayedInstructions.add(instruction)
        notifyDataSetChanged()
    }

    fun setMeasurements(measurements: Map<String,Double>) {
        this.measurements = measurements
        notifyDataSetChanged()
    }


}