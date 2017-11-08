package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Created by sl33k on 11/6/17.
 */
@Parcelize
@SuppressLint("ParcelCreator")
class Protocol(val id: Int, val name: String, val description: String, val instructions: Array<Instruction>) : Parcelable {
    override fun equals(other: Any?): Boolean{
        return when (other) {
            this -> return true
            is Protocol -> other.id == this.id
            else -> return false
        }
    }
    override fun hashCode(): Int = this.id
}
