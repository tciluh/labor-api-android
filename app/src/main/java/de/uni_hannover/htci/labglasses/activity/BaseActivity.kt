package de.uni_hannover.htci.labglasses.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import de.uni_hannover.htci.labglasses.R
import org.jetbrains.anko.AnkoLogger

/**
 * Created by sl33k on 12/11/17.
 */
open class BaseActivity : AppCompatActivity(), AnkoLogger {
    //make sure that default values for settings are regardless of which activity comes first
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }

    companion object {
        val API_HOST_PREFERENCE = "api_url"
        val API_PORT_PREFERENCE = "api_port"
    }

}