package de.uni_hannover.htci.labglasses.data

/**
 * Created by sl33k on 11/6/17.
 */
data class Instruction(val id: Int, val description:String, val imageId: Int?, val equation: String?, val timerDuration: String?, val results: Array<Result>){
    override fun equals(other: Any?): Boolean{
        return when (other) {
            this -> return true
            is Instruction -> other.id == this.id
            else -> return false
        }
    }
    override fun hashCode(): Int = this.id
}