package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import de.uni_hannover.htci.labglasses.adapter.AdapterType
import de.uni_hannover.htci.labglasses.adapter.ViewType
import kotlinx.android.parcel.Parcelize


/**
 * Created by sl33k on 11/6/17.
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class Protocol(val id: Int, val name: String, val description: String, var instructions: Array<Instruction>) : Parcelable, ViewType {
    init {
        instructions.sortBy { it.id }
    }
    override fun equals(other: Any?): Boolean{
        return when (other) {
            is Protocol -> other.id == this.id
            else -> return false
        }
    }

    @Transient
    override val viewType = AdapterType.PROTOCOL

    val firstInstruction: Instruction get() = this.instructions.first { it.isFirst }

    fun instructionById(id: Int): Instruction? = this.instructions.firstOrNull { it.id == id }

    fun instructionForAction(action: Action) = this.instructions.firstOrNull { it.actions.contains(action) }

    override fun hashCode(): Int = this.id

}
