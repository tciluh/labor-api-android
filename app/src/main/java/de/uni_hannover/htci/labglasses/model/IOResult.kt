package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by sl33k on 05.04.18.
 * An IOResult is used by the API to represent a result that was recorded in response to an action request.
 */

@Parcelize
@SuppressLint("ParcelCreator")
data class IOResult(val id: Int, val identifier: String, val action: String, val arguments: Map<String, String>,val value: String, val createdAt: String) : Parcelable {
    override fun equals(other: Any?): Boolean
            = when(other) {
        this -> true
        is IOResult -> other.id == this.id
        else -> false
    }
    override fun hashCode(): Int = this.id
}