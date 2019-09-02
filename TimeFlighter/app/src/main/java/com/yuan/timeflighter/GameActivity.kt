package com.yuan.timeflighter
import android.os.Bundle
import android.os.CountDownTimer
//import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yuan.timeflighter.R

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

        // This line takes the layout you’ve created and puts it on your device screen by
        // passing in the identifier for the layout
        setContentView(R.layout.activity_game)
        // connect views to variables
        // 1
        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)
        // 2 attaches a listener to the button which calls incremetnScore function
        tapMeButton.setOnClickListener { v -> incrementScore() }
    }

    private fun incrementScore() {
        score++
        val newScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = newScore
    }

    private fun resetGame() {
        // reset game logic
    }
    private fun startGame() {
        // start game logic
    }
    private fun endGame() {
        // end game logic
    }
}