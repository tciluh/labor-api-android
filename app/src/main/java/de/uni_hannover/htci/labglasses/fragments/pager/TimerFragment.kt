package de.uni_hannover.htci.labglasses.fragments.pager

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.Fragment
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.uni_hannover.htci.labglasses.R
import kotlinx.android.synthetic.main.timer_instruction.*
import org.jetbrains.anko.support.v4.act

/**
 * Created by sl33k on 1/5/18.
 * this fragment displays a countdown that can be started/stopped and reset by the user.
 */

class TimerFragment: Fragment(), PagingAwareFragment, InstructionFragment {
    interface TimerStepFragmentDelegate {
        fun onTimerStepFinished()
    }

    private var countdown: CountDownTimer? = null
    private var duration: Long = 0
    private var durationLeft: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: find out what this is for.
        userVisibleHint = false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(savedInstanceState == null) {
            val durationString = instruction?.timerDuration
            if(durationString != null) {
                try{
                    val converted = durationString.toLong() * 1000 // in milliseconds
                    duration = converted
                    durationLeft = converted
                }
                catch (e: NumberFormatException) {
                    error("not a valid time in timer duration: ${durationString}")
                }
            }
            else{
                error("timerDuration is null but a TimerFragment was loaded for this instruction??")
            }
        }
        else {
            duration = savedInstanceState.getLong(DURATION_KEY)
            durationLeft = savedInstanceState.getLong(DURATION_LEFT_KEY)
        }
        return inflater?.inflate(R.layout.timer_instruction, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        countdown?.cancel()
        outState?.putLong(DURATION_KEY, duration)
        outState?.putLong(DURATION_LEFT_KEY, durationLeft)
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetButton.visibility = View.GONE
        resetButton.setOnClickListener{ _ ->
            resetButton.visibility = View.GONE
            countdown?.cancel()
            countdown = buildCountdownTimer(duration)
            countdown?.start()
        }
        if(savedInstanceState != null) {
            // manually call onPageVisible(), as this will not be done by the pager
            // this scenario happens if the view is rotated while the timer is running
            onPageVisible()
        }
    }

    override fun onPageVisible() {
        if(durationLeft > 1000) {
            resetButton.visibility = View.GONE
            countdown = buildCountdownTimer(durationLeft)
            countdown?.start()
        }
    }

    override fun onPageHidden() {
        countdown?.cancel()
    }

    override fun isFinished(): Boolean {
        return durationLeft < 1000
    }

    private fun onTick(millisUntilFinished: Long) {
        durationLeft = millisUntilFinished
        updateVisibleProgress()
    }

    private fun onFinish() {
        resetButton.visibility = View.VISIBLE
        (this.activity as? TimerStepFragmentDelegate)?.onTimerStepFinished()
    }


    private fun updateVisibleProgress() {
        timerTextView?.let {
            val formatted = DateUtils.formatElapsedTime(durationLeft/1000)
            it.text = getString(R.string.countdown_timer, formatted)
        }
    }

    private fun buildCountdownTimer(duration: Long) =
            object: CountDownTimer(duration, 500) {
                    override fun onTick(millisUntilFinished: Long) = this@TimerFragment.onTick(millisUntilFinished)
                    override fun onFinish() = this@TimerFragment.onFinish()
                }


    companion object {
        const val DURATION_KEY = "timer-fragment-saved-duration"
        const val DURATION_LEFT_KEY = "timer-fragment-duration-left"
    }


}
