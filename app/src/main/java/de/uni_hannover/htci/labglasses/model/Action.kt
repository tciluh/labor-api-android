package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import org.json.JSONObject

/**
 * Created by sl33k on 02.03.18.
 */

@Parcelize
@SuppressLint("ParcelCreator")
data class Action(val id: Int, val identifier: String, val action: String, val arguments: Map<String, String>) : Parcelable {
    override fun equals(other: Any?): Boolean
       = when(other) {
        this -> true
        is Action -> other.id == this.id;
        else -> false
    }

}