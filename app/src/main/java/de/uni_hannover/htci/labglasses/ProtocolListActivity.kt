package de.uni_hannover.htci.labglasses

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import de.uni_hannover.htci.labglasses.adapter.ProtocolListAdapter
import de.uni_hannover.htci.labglasses.adapter.ViewType
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.model.Result
import kotlinx.android.synthetic.main.activity_protocol_list.*
import kotlinx.android.synthetic.main.protocol_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ProtocolDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ProtocolListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPaned: Boolean = false

    private val recyclerView by lazy {
        protocol_list.setHasFixedSize(true)
        protocol_list
    }

    private val listAdapter: ProtocolListAdapter by lazy {
        recyclerView.adapter = ProtocolListAdapter(this::onItemSelect)
        recyclerView.adapter as ProtocolListAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (protocol_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPaned = true
        }

        swipeRefreshLayout.isRefreshing = true

        //add mock protocols
        listAdapter.addProtocols((listOf(
                Protocol(0, name = "some name", description = "some description",
                        instructions = arrayOf(
                                Instruction(id = 0, description = "bla", results = arrayOf(
                                        Result(id = 0, description = "bla")
                                ))
                        )
                ))))

        swipeRefreshLayout.isRefreshing = false
    }

    private fun onItemSelect(item: ViewType){
        when(item){
            is Protocol -> {
                if (twoPaned) {
                    val fragment = ProtocolDetailFragment().apply {
                        arguments = Bundle()
                        arguments.putParcelable(ProtocolDetailFragment.ARG_ITEM_ID, item)
                    }
                    this.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.protocol_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(this, ProtocolDetailActivity::class.java).apply {
                        putExtra(ProtocolDetailFragment.ARG_ITEM_ID, item)
                    }
                    this.startActivity(intent)
                }
            }
            else -> Snackbar.make(recyclerView, "Something else Selected", 1000).show()
        }
    }
}
