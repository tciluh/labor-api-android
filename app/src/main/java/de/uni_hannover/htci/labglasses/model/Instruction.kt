package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by sl33k on 11/6/17.
 */
@Parcelize
@SuppressLint("ParcelCreator")
data class Instruction(val id: Int, val description: String, val imageId: Int? = null, val equation: String? = null, val timerDuration: String? = null, val results: Array<Result>) : Parcelable {
    override fun equals(other: Any?): Boolean{
        return when (other) {
            this -> return true
            is Instruction -> other.id == this.id
            else -> return false
        }
    }
    override fun hashCode(): Int = this.id
}