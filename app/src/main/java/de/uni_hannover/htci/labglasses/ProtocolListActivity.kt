package de.uni_hannover.htci.labglasses

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.DividerItemDecoration.VERTICAL
import android.util.Log
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import de.uni_hannover.htci.labglasses.adapter.ProtocolListAdapter
import de.uni_hannover.htci.labglasses.adapter.ViewType
import de.uni_hannover.htci.labglasses.api.LaborApi
import de.uni_hannover.htci.labglasses.model.Protocol
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_protocol_list.*
import kotlinx.android.synthetic.main.protocol_list.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory


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
        //create adapter
        recyclerView.adapter = ProtocolListAdapter(this::onItemSelect)
        //setup cell divider
        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
        //last line is lazy return
        recyclerView.adapter as ProtocolListAdapter
    }

    private val moshi: Moshi by lazy {
        Moshi.Builder()
                .add(Wrapped.ADAPTER_FACTORY)
                .add(KotlinJsonAdapterFactory())
                .build()
    }

    private val retrofit: Retrofit by lazy {
        //we want the subscription to run on a background thread
        val rx2adapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

        Retrofit.Builder()
                .baseUrl("http://192.168.1.21:3000/")
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(rx2adapter)
                .build()
    }

    private val protocolApi: LaborApi  by lazy {
        retrofit.create(LaborApi::class.java)
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

        //subscribe to the api to get all protocols
        protocolApi.getProtocols()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ list ->
                    swipeRefreshLayout.isRefreshing = false
                    Log.d("ProtocolListActivity", "got api response: $list")
                    listAdapter.addProtocols(list)

                }, { error ->
                    Snackbar.make(recyclerView, "An Error occurred", 2000).show()
                    Log.e("ProtocolListActivity", "error loading api data: $error")
                })


        //swipeRefreshLayout.isRefreshing = false
    }

    private fun onItemSelect(item: ViewType) {
        when (item) {
            is Protocol -> {
                if (twoPaned) {
                    val fragment = ProtocolDetailFragment().apply {
                        arguments = Bundle()
                        arguments.putParcelable(ProtocolDetailFragment.PROTOCOL_ITEM, item)
                    }
                    this.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.protocol_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(this, ProtocolDetailActivity::class.java).apply {
                        putExtra(ProtocolDetailFragment.PROTOCOL_ITEM, item)
                    }
                    this.startActivity(intent)
                }
            }
            else -> Snackbar.make(recyclerView, "Something else Selected", 1000).show()
        }
    }
}
