package com.yuan.timeflighter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
//import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.yuan.timeflighter.R

class GameActivity : AppCompatActivity() {
    internal val TAG = GameActivity::class.java.simpleName

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
        Log.d(TAG, "onCreate called. Score is: $score")
        Log.d("myTag", "This is my message");
        // connect views to variables
        // 1
        gameScoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeLeftTextView = findViewById<TextView>(R.id.time_left_text_view)
        tapMeButton = findViewById<Button>(R.id.tap_me_button)
        // 2 attaches a listener to the button which calls incrementScore function
        tapMeButton.setOnClickListener { v -> incrementScore() }
        resetGame()
    }

    private fun incrementScore() {
        if (!gameStarted) {
            startGame()
        }
        score++
        val newScore = getString(R.string.your_score, Integer.toString(score))
        gameScoreTextView.text = newScore
    }

    private fun resetGame() {
        // 1
        score = 0
        val initialScore = getString(R.string.your_score,
            Integer.toString(score))
        gameScoreTextView.text = initialScore
        val initialTimeLeft = getString(R.string.time_left,
            Integer.toString(60))
        timeLeftTextView.text = initialTimeLeft
        // 2
        countDownTimer = object : CountDownTimer(initialCountDown,
            countDownInterval) {
            // 3 onTick function will be called at every countDownInterval, here, once a second
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt() / 1000
                val timeLeftString = getString(R.string.time_left,
                    Integer.toString(timeLeft))
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
        countDownTimer.start()
        gameStarted = true
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message,
            Integer.toString(score)), Toast.LENGTH_LONG).show()
        resetGame()
    }
}