package com.sih.timetable.fragment

import android.icu.number.NumberFormatter.with
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jsibbold.zoomage.ZoomageView
import com.sih.timetable.R
import com.sih.timetable.activities.MainActivity
import com.sih.timetable.databinding.FragmentFoodBinding
import com.sih.timetable.databinding.FragmentTimetableBinding
import java.text.SimpleDateFormat
import java.util.*

class TimetableFragment : Fragment() {

    lateinit var binding: FragmentTimetableBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTimetableBinding.inflate(layoutInflater)
        setTheDay()
        loadTheImage()
        return binding.root
    }

    private fun setTheDay(){
        val calendar: Calendar = Calendar.getInstance()
        val date: Date = calendar.time
        val dayName = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        binding.tvDayName.text = dayName
    }

    private fun loadTheImage(){
        FirebaseDatabase.getInstance().reference.child("Database").child("Timetables")
            .child("TE").child("TE2").child("ttURL").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val url = snapshot.getValue(String::class.java)
                    Glide.with(context!!).load(url).into(binding.zvTtImage)
                    binding.progressCircular.visibility = View.GONE
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}