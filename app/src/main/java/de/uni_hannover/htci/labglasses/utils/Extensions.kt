package de.uni_hannover.htci.labglasses.utils

import android.support.v4.app.FragmentTransaction
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by sl33k on 11/8/17.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

//a function which calls another function and then returns true
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

//a function which starts a FragmentManagerTransaction, applies one or more transactions commands
//and then commits the transaction
inline fun FragmentManager.withTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()
