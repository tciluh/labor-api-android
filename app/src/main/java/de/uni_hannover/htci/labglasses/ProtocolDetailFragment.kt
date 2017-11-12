package de.uni_hannover.htci.labglasses

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.model.Protocol
import kotlinx.android.synthetic.main.activity_protocol_detail.*
import kotlinx.android.synthetic.main.protocol_detail.view.*

/**
 * A fragment representing a single Protocol detail screen.
 * This fragment is either contained in a [ProtocolListActivity]
 * in two-pane mode (on tablets) or a [ProtocolDetailActivity]
 * on handsets.
 */
class ProtocolDetailFragment : Fragment() {

    private val protocol: Protocol by lazy {
        arguments.getParcelable<Protocol>(PROTOCOL_ITEM)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        protocol.let {
            activity.toolbar_layout?.title = it.name
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.protocol_detail, container, false)

        // Show the dummy content as text in a TextView.
        protocol.let {
            rootView.protocol_detail.text = it.description
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val PROTOCOL_ITEM = "protocol_item"
    }
}
