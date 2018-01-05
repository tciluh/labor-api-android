package de.uni_hannover.htci.labglasses.protocol_pager

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

/**
 * Created by sl33k on 1/5/18.
 */

class TimerFragment: Fragment() {
    var countdown: CountDownTimer? = null
    var duration: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userVisibleHint = false
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val durationString = instruction.timerDuration
        if(durationString != null) {
            try{
                val converted = durationString.toLong()
                duration = converted
                countdown = buildCountdownTimer(converted)
            }
            catch (e: NumberFormatException) {
                error("not a valid time in timer duration: ${instruction.timerDuration}")
            }
        }
        else{
            error("timerDuration is null but a TimerFragment was loaded for this instruction??")
        }
        return inflater?.inflate(R.layout.timer_instruction, container, false)
    }
    private fun onTick(millisUntilFinished: Long) {
        if(timerTextView != null) {
            val formatted = DateUtils.formatElapsedTime(millisUntilFinished/1000)
            timerTextView.text = getString(R.string.countdown_timer, formatted)
        }
    }
    private fun onFinish() {
        //TODO: not sure what to here yet
    }

    private fun buildCountdownTimer(duration: Long) =
            object: CountDownTimer(duration*1000, 500) {
                    override fun onTick(millisUntilFinished: Long) = this@TimerFragment.onTick(millisUntilFinished)
                    override fun onFinish() = this@TimerFragment.onFinish()
                }

    private fun initTextView() = timerTextView?.let {
        it.text = getString(R.string.countdown_timer, DateUtils.formatElapsedTime(duration ?: 0))
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTextView()
        startButton.setOnClickListener { _ ->
            countdown?.start()
        }
        resetButton.setOnClickListener{ _ ->
            countdown?.cancel()
            duration?.let {
                countdown = buildCountdownTimer(it)
                initTextView()
                countdown?.start()
            }
        }

    }

}
