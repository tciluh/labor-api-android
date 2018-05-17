package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Api IOAction Object which is used to describe a measurement action taken in a instruction
 */

@Parcelize
@SuppressLint("ParcelCreator")
data class Action(val id: Int, val identifier: String, val action: String, val arguments: Map<String, String>, val equationIdentifier: String?) : Parcelable {
    override fun equals(other: Any?): Boolean
       = when(other) {
        is Action -> other.id == this.id
        else -> false
    }
    override fun hashCode(): Int = this.id
}