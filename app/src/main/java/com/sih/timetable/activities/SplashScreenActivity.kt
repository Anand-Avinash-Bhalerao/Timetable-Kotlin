package com.sih.timetable.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()


        }, 1500)
    }
}