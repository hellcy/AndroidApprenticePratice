package com.yuan.timeflighter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
//import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    // 1
    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

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
        // 2 attaches a listener to the button which calls incremetnScore function,  load the bounce animation
        //defined within the anim folder
        tapMeButton.setOnClickListener { v ->
            val bounceAnimation = AnimationUtils.loadAnimation(this,
                R.anim.bounce);
            v.startAnimation(bounceAnimation)
            incrementScore()
        }
        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    // 2
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $score & Time Left: $timeLeft")
    }
    // 3
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called.")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // This method is called whenever an item in your menu is selected
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            showInfo()
        }
        return true
    }

    // create an AlertDialog.Builder object and pass in a Context instance to let
    //the Dialog know where it should appear. You then pass in your title and message, create
    //the Dialog, and finally display it.
    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title,
            BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
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
            // 3 onTick function will be called on every countDownInterval
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

    private fun restoreGame() {
        val restoredScore = getString(R.string.your_score,
            Integer.toString(score))
        gameScoreTextView.text = restoredScore
        val restoredTime = getString(R.string.time_left,
            Integer.toString(timeLeft))
        timeLeftTextView.text = restoredTime
        countDownTimer = object : CountDownTimer((timeLeft * 1000).toLong(),
            countDownInterval) {
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
        countDownTimer.start()
        gameStarted = true
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