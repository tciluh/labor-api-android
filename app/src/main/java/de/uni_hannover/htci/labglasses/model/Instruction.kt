package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by sl33k on 11/6/17.
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class Instruction(val id: Int, val isFirst: Boolean, val description: String, val imageId: Int? = null, val equation: String? = null, val timerDuration: String? = null, val results: Array<Result>) : Parcelable {
    init {
        results.sortBy { it.id }
    }
    override fun equals(other: Any?): Boolean{
        return when (other) {
            this -> return true
            is Instruction -> other.id == this.id
            else -> return false
        }
    }

    override fun hashCode(): Int = this.id

    val isBranchInstruction get() = this.results.size > 1

    val type: InstructionType get() {
        if (this.equation != null && this.timerDuration == null) {
           return InstructionType.Equation
        }
        else if(this.equation == null && this.timerDuration != null) {
            return InstructionType.Timer
        }
        else if (this.equation != null && this.timerDuration != null) {
            return InstructionType.Invalid
        }
        else {
            return InstructionType.Simple
        }
    }

    val nextInstructionId: Int? get() {
        //we cant determine a definitive next instruction if the result is ambiguous
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