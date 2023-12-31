package es.ismasg.comptador

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var textTemps: TextView
    private lateinit var textScore: TextView
    private lateinit var textRecordScore: TextView
    private lateinit var textCenter: TextView
    private lateinit var buttonCenter: Button

    private var roundTime = 10
    private var score = 0
    private var recordScore = 0
    private var running = false
    private var disabled = false

    private var startingTimer = object : CountDownTimer(4000, 1000) {
        override fun onTick(p0: Long) {
            val timeLeft = (p0 / 1000) + 1
            textCenter.text = getString(R.string.starting_in, timeLeft)
        }
        override fun onFinish() {
            textCenter.visibility = View.INVISIBLE
            buttonCenter.text = getString(R.string.tap_me)
            buttonCenter.visibility = View.VISIBLE
            roundTimer.start()
        }
    }

    private var roundTimer = object : CountDownTimer(roundTime.toLong() * 1000, 1000) {
        override fun onTick(p0: Long) {
            val timeLeft = p0 / 1000
            textTemps.text = getString(R.string.time_left, timeLeft)
        }
        override fun onFinish() {
            if(score > recordScore) {
                recordScore = score
                textRecordScore.text = getString(R.string.your_record, recordScore)
            }
            running = false
            disabled = true
            buttonCenter.text = getString(R.string.start_again)
            Toast.makeText(applicationContext, "Round finished!", Toast.LENGTH_LONG).show()
            disableTimer.start()
        }
    }

    private var disableTimer = object : CountDownTimer(3000, 1000) {
        override fun onTick(p0: Long) {
        }

        override fun onFinish() {
            disabled = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupGUI()
    }

    private fun setupGUI() {
        textTemps = findViewById(R.id.textTemps)
        textScore = findViewById(R.id.textScore)
        textRecordScore = findViewById(R.id.textRecordScore)
        textCenter = findViewById(R.id.textCenter)
        buttonCenter = findViewById(R.id.buttonCenter)
        textTemps.text = getString(R.string.time_left, roundTime)
        textScore.text = getString(R.string.score, score)
        textRecordScore.text = getString(R.string.your_record, recordScore)
        buttonCenter.text = getString(R.string.tap_me_start)
        buttonCenter.setOnClickListener {
            buttonTrigger()
        }
    }

    private fun buttonTrigger(){
        if(!running && !disabled) {
            running = true
            score = 0
            textTemps.text = getString(R.string.time_left, roundTime)
            textScore.text = getString(R.string.score, score)
            textCenter.text = getString(R.string.starting_in, 5)
            textCenter.visibility = View.VISIBLE
            buttonCenter.visibility = View.INVISIBLE
            startingTimer.start()
        }
        else if(running && !disabled){
            score += 1
            textScore.text = getString(R.string.score, score)
            buttonCenter.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fade))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when(item.itemId){
            R.id.menuAbout -> {
                showInfo()
            }
        }
        return true
    }

    private fun showInfo() {
        val dialogTitle = getString(R.string.about_title) + " " + BuildConfig.VERSION_NAME
        val dialogMessage = getString(R.string.about_message)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle).setMessage(dialogMessage).create().show()
    }
}