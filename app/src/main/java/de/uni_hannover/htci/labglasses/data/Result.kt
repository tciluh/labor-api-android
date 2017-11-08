package de.uni_hannover.htci.labglasses.data

/**
 * Created by sl33k on 11/6/17.
 */
data class Result (val id: Int, val description: String, val targetInstructionId: Int?, val imageId: Int?){
    override fun equals(other: Any?): Boolean{
        return when (other) {
            this -> return true
            is Result -> other.id == this.id
            else -> return false
        }
    }
    override fun hashCode(): Int = this.id
}