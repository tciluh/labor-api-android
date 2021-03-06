package de.uni_hannover.htci.labglasses.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.NavUtils
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import de.uni_hannover.htci.labglasses.R
import de.uni_hannover.htci.labglasses.adapter.ActionListAdapter
import de.uni_hannover.htci.labglasses.fragments.ProtocolDetailFragment
import de.uni_hannover.htci.labglasses.fragments.ResultDialogFragment
import de.uni_hannover.htci.labglasses.fragments.pager.TimerFragment
import de.uni_hannover.htci.labglasses.model.Action
import de.uni_hannover.htci.labglasses.model.Action.MeasurementState.*
import de.uni_hannover.htci.labglasses.model.Instruction
import de.uni_hannover.htci.labglasses.model.Protocol
import de.uni_hannover.htci.labglasses.model.Result
import de.uni_hannover.htci.labglasses.singletons.SocketManager
import de.uni_hannover.htci.labglasses.utils.withTransaction
import de.uni_hannover.htci.labglasses.views.KeyboardViewPager
import kotlinx.android.synthetic.main.activity_protocol_detail.*
import org.jetbrains.anko.debug
import org.jetbrains.anko.support.v4.withArguments
import com.vuzix.sdk.speechrecognitionservice.VuzixSpeechClient
import org.jetbrains.anko.error
import java.security.Key


/**
 * An activity representing a single Protocol detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [ProtocolListActivity].
 */
class ProtocolDetailActivity : BaseActivity(),
        ResultDialogFragment.ResultSelectionDelegate,
        ProtocolDetailFragment.BranchingDelegate,
        ActionListAdapter.ActionDelegate,
        TimerFragment.TimerStepFragmentDelegate,
        KeyboardViewPager.NavigationDelegate
{

    private val detailFragment get() = supportFragmentManager.findFragmentById(R.id.protocol_detail_container) as ProtocolDetailFragment
    private val completedMeasurements: MutableMap<String, Double> = mutableMapOf()
    private val protocol: Protocol get() = intent.getParcelableExtra(PROTOCOL_ITEM)
    private lateinit var speechClient: VuzixSpeechClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_protocol_detail)
        setSupportActionBar(detail_toolbar)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

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
                    ProtocolDetailFragment.PROTOCOL_ITEM to protocol
            )
            supportFragmentManager.withTransaction {
               add(R.id.protocol_detail_container, fragment)
            }
            try {
                speechClient = VuzixSpeechClient(this)
                // add our phrases
                speechClient.insertKeycodePhrase("next step", KeyEvent.KEYCODE_DPAD_RIGHT)
                speechClient.insertKeycodePhrase("previous step", KeyEvent.KEYCODE_DPAD_LEFT)
                speechClient.insertKeycodePhrase("next result", KeyEvent.KEYCODE_DPAD_RIGHT)
                speechClient.insertKeycodePhrase("previous result", KeyEvent.KEYCODE_DPAD_LEFT)
                speechClient.insertKeycodePhrase("select result", KeyEvent.KEYCODE_DPAD_CENTER)
                speechClient.insertKeycodePhrase("end protocol", KeyEvent.KEYCODE_BACK)
            }
            catch(e: NoClassDefFoundError) {

            }
            // try enabling the hello vuzix phrase
            try {
                VuzixSpeechClient.EnableRecognizer(applicationContext, true)
            } catch (e: NoSuchMethodError) {
                // This is not an M300 version 1.2.5 or higher
                Snackbar.make(protocol_detail_container, "Error enabling voice services, please update your M300!", 2).show()
            }
            catch(e: NoClassDefFoundError) {
                error("VuzixSpeechClient not found. Am I running on a updated M300?")
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
                /*
                R.id.action_previous -> consume {
                    detailFragment.previousPage()
                }
                R.id.action_next -> consume {
                    detailFragment.nextPage()
                }
                */
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
        if(supportFragmentManager.findFragmentByTag("result-dialog") == null) {
            try {
                ResultDialogFragment()
                        .withArguments(ResultDialogFragment.DIALOG_INSTRUCTION_ITEM to current)
                        .also{
                            it.resultDelegate = this
                        }
                        .show(supportFragmentManager, "result-dialog")
            }
            catch(e: IllegalStateException) {
                debug { "couldnt show resultdialogfragment: ${e.cause} (${e.message}\n${e.stackTrace}"}
            }

        }
    }

    override fun handleAction(action: Action, onFinish: (() -> Unit)? ){
        // the actions that is recieved here is a copy
        val instruction = protocol.instructionForAction(action)
        if(instruction != null) {
            instruction.actionForId(action.id).state = InProgress
            detailFragment.onUpdatedInstruction(instruction)
        }
        else {
            debug("no instruction for action: $action found.")
            return
        }

        SocketManager.getInstance(this).sendAction(action, {
            result ->
            runOnUiThread {
                if(action.equationIdentifier != null) {
                    val doubleVal: Double? = when(result) {
                        is Double -> result
                        is String -> result.toDoubleOrNull()
                        else -> result.toString().toDoubleOrNull()
                    }
                    if(doubleVal != null) {
                        completedMeasurements[action.equationIdentifier] = doubleVal
                        instruction.actionForId(action.id).addResult(doubleVal)
                        detailFragment.onUpdatedMeasurements(completedMeasurements)
                        detailFragment.onUpdatedInstruction(instruction)
                        onFinish?.invoke()
                    }
                    else {
                        debug("couldn't add: $result to measurementMap as it is not a double")
                        instruction.actionForId(action.id).state = Error
                        detailFragment.onUpdatedInstruction(instruction)
                        onFinish?.invoke()
                    }
                }
                else {
                    debug("got result: $result for action: $action, but no equation identifier was set.")
                    instruction.actionForId(action.id).state = Done
                    detailFragment.onUpdatedInstruction(instruction)
                    onFinish?.invoke()
                }
            }
        }, {
            error ->
            runOnUiThread {
                instruction.actionForId(action.id).state = Error
                detailFragment.onUpdatedInstruction(instruction)
                onFinish?.invoke()
            }

        })
    }

    override fun onTimerStepFinished() = detailFragment.nextPage()
    override fun onLeftDpad() = detailFragment.previousPage()
    override fun onRightDpad() = detailFragment.nextPage()

    companion object {
        const val PROTOCOL_ITEM = "protocol-detail-protocol-item"
    }
}
