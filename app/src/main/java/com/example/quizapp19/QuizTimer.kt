package com.example.quizapp19

import android.os.CountDownTimer
import java.util.concurrent.TimeUnit

class QuizTimer(
    totalTimeInSeconds: Int,
    private val onTick: (String) -> Unit,
    private val onFinish: () -> Unit
) {
    private val countDownTimer: CountDownTimer

    init {
        val totalTimeInMillis = totalTimeInSeconds * 1000L
        countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                onTick(String.format("%02d:%02d", minutes, seconds))
            }

            override fun onFinish() {
                this@QuizTimer.onFinish()
            }
        }
    }

    fun start() {
        countDownTimer.start()
    }

    fun cancel() {
        countDownTimer.cancel()
    }
}