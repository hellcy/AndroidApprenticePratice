package com.yuan.timefighter

import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class GameActivity : AppCompatActivity() {
    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button

    internal var score = 0

    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountDown: Long = 60000
    internal var countDownInterval: Long = 1000
    internal var timeLeft = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // 1
        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)
        // 2
        tapMeButton.setOnClickListener { v -> incrementScore() }

        resetGame()
    }

    private fun incrementScore() {
        // increment score logic

        // start the game when you click the button, if it hasn't started yet
        if (!gameStarted) {
            startGame()
        }
        score++
        val newScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = newScore
    }
    private fun resetGame() {
        // reset game logic
        score = 0

        var initialScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = initialScore

        var initialTimeLeft = getString(R.string.time_left, Integer.toString(60))
        timeLeftTextView.text = initialTimeLeft

        // 2
        countDownTimer = object: CountDownTimer(initialCountDown, countDownInterval) {
            // 3
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000

                val timeLeftString = getString(R.string.time_left, Integer.toString(timeLeft))
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }

        // 4
        gameStarted = false
    }
    private fun startGame() {
        // start game logic
        countDownTimer.start()
        gameStarted = true
    }
    private fun endGame() {
        // End game logic
        Toast.makeText(this, getString(R.string.game_over_message, Integer.toString(score)), Toast.LENGTH_LONG).show()
        resetGame()
    }
}
