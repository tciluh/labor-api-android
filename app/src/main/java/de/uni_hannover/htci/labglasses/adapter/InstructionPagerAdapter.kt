package de.uni_hannover.htci.labglasses.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import de.uni_hannover.htci.labglasses.fragments.pager.BaseFragment
import de.uni_hannover.htci.labglasses.fragments.pager.INSTRUCTION_ITEM
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import org.jetbrains.anko.support.v4.withArguments
import java.util.*

/**
 * Created by sl33k on 1/5/18.
 */
class InstructionPagerAdapter(fm: FragmentManager?, private val protocol: Protocol) : FragmentStatePagerAdapter(fm) {
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
        return BaseFragment().withArguments(INSTRUCTION_ITEM to instruction)
    }
    override fun getCount(): Int = displayedInstructions.size

    override fun getPageTitle(position: Int): CharSequence = "Step ${position + 1}"

    val currentInstruction: Instruction get() = displayedInstructions.last()

    fun instructionAtIndex(index: Int): Instruction? = displayedInstructions.getOrNull(index)

    fun add(instruction: Instruction) {
        displayedInstructions.add(instruction)
        notifyDataSetChanged()
    }


}