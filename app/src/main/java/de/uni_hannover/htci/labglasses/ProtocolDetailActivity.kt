package de.uni_hannover.htci.labglasses

import android.content.Intent
import android.net.wifi.WifiConfiguration
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import de.uni_hannover.htci.labglasses.utils.withTransaction
import kotlinx.android.synthetic.main.activity_protocol_detail.*
import org.jetbrains.anko.support.v4.withArguments

/**
 * An activity representing a single Protocol detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ProtocolListActivity].
 */
class ProtocolDetailActivity : BaseActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_detail)
        setSupportActionBar(detail_toolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = ProtocolDetailFragment().withArguments(
                    ProtocolDetailFragment.PROTOCOL_ITEM to
                            intent.getParcelableExtra(ProtocolDetailFragment.PROTOCOL_ITEM)
            )
            supportFragmentManager.withTransaction {
               add(R.id.protocol_detail_container, fragment)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                android.R.id.home -> {
                    // This ID represents the Home or Up button. In the case of this
                    // activity, the Up button is shown. Use NavUtils to allow users
                    // to navigate up one level in the application structure. For
                    // more details, see the Navigation pattern on Android Design:
                    //
                    // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                    NavUtils.navigateUpTo(this, Intent(this, ProtocolListActivity::class.java))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
