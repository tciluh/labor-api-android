package de.uni_hannover.htci.labglasses

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.uni_hannover.htci.labglasses.adapter.ProtocolListAdapter
import de.uni_hannover.htci.labglasses.dummy.DummyContent
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.model.Result
import de.uni_hannover.htci.labglasses.utils.inflate
import kotlinx.android.synthetic.main.activity_protocol_list.*
import kotlinx.android.synthetic.main.protocol_list.*
import kotlinx.android.synthetic.main.protocol_list_content.view.*

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
    private var mTwoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (protocol_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true
        }




        setupRecyclerView(protocol_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        //improve performance, all our cells are fixed size
        recyclerView.setHasFixedSize(true)
        if (recyclerView.adapter == null) {
            recyclerView.adapter = ProtocolListAdapter()
        }
        (recyclerView.adapter as ProtocolListAdapter).addProtocols(listOf(
                Protocol(0, name = "some name", description = "some description",
                        instructions = arrayOf(
                                Instruction(id = 0, description = "bla", results = arrayOf(
                                        Result(id = 0, description = "bla")
                                ))
                        )
                )))
        //recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane)

    }

    class SimpleItemRecyclerViewAdapter(private val mParentActivity: ProtocolListActivity,
                                        private val mValues: List<DummyContent.DummyItem>,
                                        private val mTwoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val mOnClickListener: View.OnClickListener

        init {
            mOnClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (mTwoPane) {
                    val fragment = ProtocolDetailFragment().apply {
                        arguments = Bundle()
                        arguments.putString(ProtocolDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    mParentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.protocol_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, ProtocolDetailActivity::class.java).apply {
                        putExtra(ProtocolDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = parent.inflate(R.layout.protocol_list_content)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = mValues[position]
            holder.mIdView.text = item.id
            holder.mContentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(mOnClickListener)
            }
        }

        override fun getItemCount(): Int {
            return mValues.size
        }

        inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
            val mIdView: TextView = mView.id_text
            val mContentView: TextView = mView.content
        }
    }
}
