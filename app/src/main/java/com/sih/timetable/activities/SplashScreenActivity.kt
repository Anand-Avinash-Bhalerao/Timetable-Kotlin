package com.sih.timetable.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sih.timetable.R
public const val DATA_SAVED = "DATA_SAVED"
public const val LECTURES = "LECTURES"
public const val SHARED_PREFERENCES = "SHARED_PREFERENCES"

class SplashScreenActivity : AppCompatActivity() {




    private var isAlreadyDataSaved = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        checkSharedPref()
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isAlreadyDataSaved) {
                Toast.makeText(this, "Not Saved", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, OnBoardingActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Is Saved", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }

        }, 0)
    }
    private fun checkSharedPref() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        isAlreadyDataSaved = sharedPreferences.getBoolean(DATA_SAVED, false)
    }

}