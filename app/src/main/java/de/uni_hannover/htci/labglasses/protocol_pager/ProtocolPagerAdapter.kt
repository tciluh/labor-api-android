package de.uni_hannover.htci.labglasses.protocol_pager

import android.media.audiofx.Equalizer
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.model.Instruction.Companion.InstructionType
import org.jetbrains.anko.support.v4.withArguments
import java.util.*

/**
 * Created by sl33k on 1/5/18.
 */
class ProtocolPagerAdapter(fm: FragmentManager?, private val protocol: Protocol) : FragmentStatePagerAdapter(fm) {
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
        val fragment: BaseFragment
        when(instruction.type){
            InstructionType.Simple -> fragment = SimpleFragment()
            InstructionType.Invalid -> error("got invalid fragment")
            InstructionType.Timer -> fragment = TimerFragment()
            InstructionType.Equation -> fragment = EquationFragment()
        }
        return fragment.withArguments(BaseFragment.INSTRUCTION_ITEM to instruction)
    }
    override fun getCount(): Int = displayedInstructions.size

    override fun getPageTitle(position: Int): CharSequence = "Step ${position + 1}"

    val currentInstruction: Instruction get() = displayedInstructions.last()

    fun add(instruction: Instruction) {
        displayedInstructions.add(instruction)
        notifyDataSetChanged()
    }


}