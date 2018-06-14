package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.concurrent.timer

/**
 * Created by sl33k on 11/6/17.
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class Instruction(val id: Int, val isFirst: Boolean, val description: String, val imageId: Int? = null, val equation: String? = null, val timerDuration: String? = null, val results: Array<Result>, var actions: Array<Action>) : Parcelable {
    init {
        results.sortBy { it.id }
    }
    override fun equals(other: Any?): Boolean{
        return when (other) {
            is Instruction -> other.id == this.id
            else -> return false
        }
    }

    override fun hashCode(): Int = this.id

    fun copy(): Instruction = Instruction(id, isFirst, description, imageId, equation, timerDuration, results.map { it.copy() }.toTypedArray(), actions.map { it.copy()}.toTypedArray() )

    fun actionForId(actionId: Int) = this.actions.first { it.id == actionId }

    val isBranchInstruction get() = this.results.size > 1
    val isPotentiallyUnfinishedInstruction get() = type == InstructionType.Timer || (type == InstructionType.Simple && actions.isNotEmpty())

    val type: InstructionType get()
    = if (this.equation != null && this.timerDuration == null) {
           InstructionType.Equation
        }
        else if(this.equation == null && this.timerDuration != null) {
            InstructionType.Timer
        }
        else if (this.equation != null && this.timerDuration != null) {
            InstructionType.Invalid
        }
        else {
            InstructionType.Simple
        }


    val nextInstructionId: Int? get() {
        //we cant determine a definitive next instruction if the result is unambiguous
        if(this.isBranchInstruction)
            return null
        val result = this.results.firstOrNull()
        return result?.targetInstructionId
    }

    companion object {
        enum class InstructionType {
            Equation, Timer, Simple, Invalid
        }
    }

}