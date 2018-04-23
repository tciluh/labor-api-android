package de.uni_hannover.htci.labglasses.fragments.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import de.uni_hannover.htci.labglasses.activity.BaseActivity.Companion.API_HOST_PREFERENCE
import de.uni_hannover.htci.labglasses.activity.BaseActivity.Companion.API_PORT_PREFERENCE
import de.uni_hannover.htci.labglasses.R
import kotlinx.android.synthetic.main.simple_instruction.*

/**
 * Created by sl33k on 1/5/18.
 * The Fragment that display a simple instruction or result with a picture
 */
class SimpleFragment: Fragment() {
    lateinit var host: String
    lateinit var port: String


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //get api url + port
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        host = sharedPrefs.getString(API_HOST_PREFERENCE, "130.75.115.47")
        port = sharedPrefs.getString(API_PORT_PREFERENCE, "3000")
        return inflater?.inflate(R.layout.simple_instruction, container, false)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageId: Int? = instruction?.imageId ?: result?.imageId
        imageId?.let {
            Picasso.with(context)
                    .load("http://$host:$port/image/$it")
                    .fit()
                    .into(imageView)
        }
    }
}