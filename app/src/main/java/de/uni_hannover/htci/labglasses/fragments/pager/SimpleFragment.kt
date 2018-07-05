package de.uni_hannover.htci.labglasses.fragments.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.squareup.picasso.Picasso
import de.uni_hannover.htci.labglasses.activity.BaseActivity.Companion.API_HOST_PREFERENCE
import de.uni_hannover.htci.labglasses.activity.BaseActivity.Companion.API_PORT_PREFERENCE
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.adapter.ActionListAdapter
import kotlinx.android.synthetic.main.action_list_content.*
import kotlinx.android.synthetic.main.simple_instruction.*

/**
 * Created by sl33k on 1/5/18.
 * The Fragment that display a simple instruction or result with a picture
 */
class SimpleFragment: Fragment(), UpdateableFragment, PagingAwareFragment, InstructionFragment {
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
        val actions = instruction?.actions

        imageId?.let {
            Picasso.with(context)
                    .load("http://$host:$port/image/$it")
                    .into(imageView)
            imageView.visibility = View.VISIBLE
        }
        actions?.let {
            if(actions.isNotEmpty()){
                // cant be null here otherwise the let block wouldnt be executed
                actionRecyclerView.adapter = ActionListAdapter(instruction!!.actions)
                        .also {
                            it.delegate = activity as ActionListAdapter.ActionDelegate
                        }
                actionRecyclerView.layoutManager = LinearLayoutManager(context)
                actionRecyclerView.visibility = View.VISIBLE
                //setup cell divider
                //val dividerItemDecoration = DividerItemDecoration(actionRecyclerView.context, DividerItemDecoration.VERTICAL)
                //actionRecyclerView.addItemDecoration(dividerItemDecoration)
            }
        }
        if(actionRecyclerView.visibility == View.VISIBLE && imageView.visibility == View.VISIBLE) {
            actionRecyclerView.visibility = View.GONE
            imageView.visibility = View.VISIBLE
        }
    }

    override fun onArgumentsChanged() {
        instruction?.actions?.let {
            (actionRecyclerView.adapter as? ActionListAdapter)?.updateActions(instruction!!.actions)
        }
    }

    override fun onPageVisible() {
        if(actionRecyclerView.visibility == View.VISIBLE) {
            (actionRecyclerView.adapter as? ActionListAdapter)?.startNextPendingAction()
        }
    }

    override fun onPageHidden() = Unit

    override fun isFinished(): Boolean {
        if(instruction?.actions != null) {
            return (actionRecyclerView.adapter as? ActionListAdapter)?.allActionsFinished() ?: false
        }
        return true
    }
}