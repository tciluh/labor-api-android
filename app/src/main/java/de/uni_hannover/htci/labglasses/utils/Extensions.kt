package de.uni_hannover.htci.labglasses.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by sl33k on 11/8/17.
 */

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}