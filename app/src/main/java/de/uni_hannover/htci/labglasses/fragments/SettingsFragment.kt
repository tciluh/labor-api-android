package de.uni_hannover.htci.labglasses.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import de.uni_hannover.htci.labglasses.R
import org.jetbrains.anko.AnkoLogger

/**
 * Created by sl33k on 12/11/17.
 */
class SettingsFragment: PreferenceFragmentCompat(), AnkoLogger {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}