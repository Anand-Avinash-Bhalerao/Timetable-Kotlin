package com.sih.timetable.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.sih.timetable.R
import com.sih.timetable.models.LecInfo




class OnBoardingActivity : AppCompatActivity() {

    private lateinit var list: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        init()
        loadLecData()
    }

    private fun navigateToMain() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    private fun init() {
        list = ArrayList()
    }



    private fun saveLecToSF(lec: ArrayList<ArrayList<LecInfo>>) {
        Log.d("THE_DATA", lec.toString())
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(lec)
        editor.putString(LECTURES, json)
        editor.apply()
    }

    private fun setLecSaved() {
        val sharedPreferencesSaved = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferencesSaved.edit()
        editor.putBoolean(DATA_SAVED, true)
        editor.apply()
    }

    private fun loadLecData() {
        val lec = ArrayList<ArrayList<LecInfo>>()
        FirebaseDatabase.getInstance().reference.child("Database").child("Timetables")
            .child("TE").child("TE2").child("K2").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (days in snapshot.children) {
                        val temp = ArrayList<LecInfo>()
                        for (lectures in days.children) {
                            val current = lectures.getValue(LecInfo::class.java)
                            temp.add(current!!)
                        }
                        lec.add(temp)
                    }
                    saveLecToSF(lec)
                    setLecSaved()
                    navigateToMain()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

}
