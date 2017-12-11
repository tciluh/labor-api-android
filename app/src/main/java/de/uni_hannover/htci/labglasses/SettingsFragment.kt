package de.uni_hannover.htci.labglasses

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import org.jetbrains.anko.AnkoLogger

/**
 * Created by sl33k on 12/11/17.
 */
class SettingsFragment: PreferenceFragmentCompat(), AnkoLogger {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

}