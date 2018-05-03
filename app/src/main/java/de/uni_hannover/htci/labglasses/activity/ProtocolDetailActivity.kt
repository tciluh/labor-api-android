package de.uni_hannover.htci.labglasses.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.Menu
import android.view.MenuItem
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.fragments.ActionsDialogFragment
import de.uni_hannover.htci.labglasses.fragments.ProtocolDetailFragment
import de.uni_hannover.htci.labglasses.fragments.ResultDialogFragment
import de.uni_hannover.htci.labglasses.model.Action
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Result
import de.uni_hannover.htci.labglasses.utils.consume
import de.uni_hannover.htci.labglasses.utils.withTransaction
import kotlinx.android.synthetic.main.activity_protocol_detail.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.support.v4.withArguments
import org.jetbrains.anko.toast

/**
 * An activity representing a single Protocol detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ProtocolListActivity].
 */
class ProtocolDetailActivity : BaseActivity(),
        ResultDialogFragment.ResultSelectionDelegate,
        ActionsDialogFragment.MeasurementResultDelegate,
        ProtocolDetailFragment.BranchingDelegate {

    private val detailFragment get() = supportFragmentManager.findFragmentById(R.id.protocol_detail_container) as ProtocolDetailFragment
    private val completedMeasurements: MutableMap<String, Double> = mutableMapOf()

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
                R.id.action_previous -> consume {
                    detailFragment.previousPage()
                }
                R.id.action_next -> consume {
                    detailFragment.nextPage()
                }
                R.id.action_measurements -> consume {
                    val actions: Array<Action> = detailFragment.currentInstruction?.actions ?: error("error parsing actions from api")

                    if(actions.isEmpty()) {
                        toast("No Measurements defined for this step!").show()
                    }
                    ActionsDialogFragment()
                            .withArguments( ActionsDialogFragment.DIALOG_ACTIONS_ITEM to actions)
                            .also { it.measurementDelegate = this }
                            .show(supportFragmentManager, "actions-dialog")
                }
                else -> super.onOptionsItemSelected(item)
            }

    override fun onResultSelect(result: Result) {
        detailFragment.nextPage(result)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun selectBranchInstructionResult(current: Instruction) {
        ResultDialogFragment()
                .withArguments(ResultDialogFragment.DIALOG_INSTRUCTION_ITEM to current)
                .also{
                    it.resultDelegate = this
                }
                .show(supportFragmentManager, "result-dialog")
    }

    override fun onMeasurementCompleted(action: Action, result: Any) {
        if(action.equationIdentifier != null) {
            val doubleVal: Double? = when(result) {
                is Double -> result
                is String -> result.toDoubleOrNull()
                else -> result.toString().toDoubleOrNull()
            }
            if(doubleVal != null) {
                completedMeasurements[action.equationIdentifier] = doubleVal
                detailFragment.updateMeasurements(completedMeasurements)
            }
            else {
                debug("couldn't add: $result to measurementMap as it is not a double")
            }
        }
        else {
            debug("got result: $result for action: $action, but no equation identifier was set.")
        }
    }
}
