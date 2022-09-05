package com.sih.timetable.fragment

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sih.timetable.adapters.LectureAdapter
import com.sih.timetable.adapters.TimeAdapter
import com.sih.timetable.databinding.FragmentHomeBinding
import com.sih.timetable.models.LecInfo
import com.sih.timetable.models.TimeInfo
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var list: ArrayList<ArrayList<LecInfo>>
    private var dayNumber: Int = Calendar.MONDAY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
//        Log.d("LIST_IS", list.toString())
        list = ArrayList()
        getTodayDay()
        setTimeRecycler()
        loadLecData()
        return binding.root
    }

    private fun setTimeRecycler() {
        var startTime = 8
        val arr = arrayOf("00", "15", "30", "45")
        val timeList: MutableList<TimeInfo> = java.util.ArrayList()
        var current = 0
        while (startTime !=5) {
            val temp = TimeInfo()
            if (current % 4 == 0) {
                temp.hr = "$startTime :"
                startTime++
            }
            temp.min = arr[current]
            current++
            current = current % 4
            timeList.add(temp)
            if (startTime == 13) {
                startTime = 1
            }
        }
        val adapter = activity?.let { TimeAdapter(it, timeList) }
        binding.rvTimes.layoutManager = LinearLayoutManager(activity)
        binding.rvTimes.isNestedScrollingEnabled = false
        binding.rvTimes.adapter = adapter
    }

    private fun getTodayDay() {
        val calendar: Calendar = Calendar.getInstance()
        dayNumber = calendar.get(Calendar.DAY_OF_WEEK)
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
                    var startTime = 8
                    setTheRecyclerView(lec, dayNumber)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun getTimeGap(start: String, end: String): Int {
        var tStart = start
        var tEnd = end
        if (start == "") tStart = "00:00"
        if (end == "") tEnd = "00:00"
        val stArr = tStart.split(":").toTypedArray()
        val edArr = tEnd.split(":").toTypedArray()
        var stHr = stArr[0].toInt()
        var stMin = stArr[1].toInt()
        if (stHr < 8) {
            stHr += 12
        }
        var stTime = stHr * 60 + stMin

        var edHr = edArr[0].toInt()
        var edMin = edArr[1].toInt()
        if (edHr < 8) {
            edHr += 12
        }
        var edTime = edHr * 60 + edMin
        var diff = edTime - stTime
        return diff / 15
    }

    private fun setTheRecyclerView(lec: ArrayList<ArrayList<LecInfo>>, dayNo: Int) {
        //sunday is 1 and saturday is 7
        val tempDateNumber = if (dayNo == 1 || dayNo == 7) {
            Calendar.MONDAY - 2
        } else {
            dayNo - 2;
        }
        //-1 to make it 0 based index
        val adapter = activity?.let { LectureAdapter(it, getTimeAdjusted(lec[tempDateNumber])) }
        binding.rvLectures.layoutManager = LinearLayoutManager(activity)
        binding.rvLectures.isNestedScrollingEnabled = false
        binding.rvLectures.adapter = adapter
    }

    private fun getTimeAdjusted(list: ArrayList<LecInfo>): ArrayList<LecInfo> {
        var finalList = ArrayList<LecInfo>()
        var end = "8:00"
        for (current in list) {
            val gapBefore = getTimeGap(end, current.startTime)
            finalList.add(LecInfo("", "", "", true, gapInPixel(gapBefore)))
            val gapBetween = getTimeGap(current.startTime, current.endTime)
            //size of each lecture

            Log.d(
                "GAPS",
                "end = $end startTime = ${current.startTime} end time = ${current.endTime}"
            )
            Log.d("GAPS","gapBefore = $gapBefore and gapBetween = $gapBetween \n")
            finalList.add(
                LecInfo(
                    current.lecName,
                    current.startTime,
                    current.endTime,
                    false,
                    gapInPixel(gapBetween)
                )
            )
            end = current.endTime
        }
        return finalList
    }

    private fun gapInPixel(gap: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            (20 * gap).toFloat(),
            resources.displayMetrics
        ).toInt()

    }
}