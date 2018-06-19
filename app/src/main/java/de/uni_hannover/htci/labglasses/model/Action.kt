package de.uni_hannover.htci.labglasses.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Api IOAction Object which is used to describe a measurement action taken in a instruction
 */

@Parcelize
@SuppressLint("ParcelCreator")
data class Action(val id: Int,
                  val plugin: String,
                  val action: String,
                  val humanReadableName: String,
                  val arguments: Map<String, String>,
                  val equationIdentifier: String?,
                  val unit: String?,
                  var state: MeasurementState = MeasurementState.NotStarted ,
                  private val results: MutableList<Double> = mutableListOf()) : Parcelable {
    override fun equals(other: Any?): Boolean
       = when(other) {
        is Action -> other.id == this.id
        else -> false
    }
    override fun hashCode(): Int = this.id

    fun copy(): Action = Action(id, plugin, action, humanReadableName, arguments.toMap(), equationIdentifier, unit, state, results.toMutableList())

    enum class MeasurementState {
        NotStarted,
        InProgress,
        Done,
        Error
    }

    fun addResult(result: Double) {
        this.state = MeasurementState.Done
        this.results.add(result)
    }
    val hasResult get() = results.isNotEmpty()
    val amountOfResults get() = results.size
    val result: String get() = String.format("%.2f", results.last())
}