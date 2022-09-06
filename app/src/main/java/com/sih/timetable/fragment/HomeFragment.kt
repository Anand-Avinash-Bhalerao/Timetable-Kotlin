package com.sih.timetable.fragment

import android.R
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sih.timetable.adapters.CustomAdapter
import com.sih.timetable.adapters.LectureAdapter
import com.sih.timetable.adapters.TimeAdapter
import com.sih.timetable.databinding.FragmentHomeBinding
import com.sih.timetable.models.LecInfo
import com.sih.timetable.models.TimeInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var lec: ArrayList<ArrayList<LecInfo>>
    private var dayNumber: Int = Calendar.MONDAY

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
//        Log.d("LIST_IS", list.toString())
        getTodayDay()
        setSpinner()
        itemClick()
        setTimeRecycler()
        loadLecData()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun itemClick() {
        binding.atvDay.setOnItemClickListener { parent, view, i, l ->
            val selected = parent.getItemAtPosition(i) as String
            val index = getDayIndex(selected)
            Toast.makeText(activity,"clicked",Toast.LENGTH_SHORT).show()
            setTheRecyclerView(lec, index)
        }
    }

    private fun getDayIndex(selected: String): Int {
        Log.d("SELECTED", selected)
        return when (selected) {
            "Monday" -> Calendar.MONDAY
            "Tuesday" -> Calendar.TUESDAY
            "Wednesday" -> Calendar.WEDNESDAY
            "Thursday" -> Calendar.THURSDAY
            "Friday" -> Calendar.FRIDAY
            "Today" -> dayNumber
            else -> {
                val maybe = dayNumber + 1
                if (dayNumber > 7) Calendar.MONDAY
                else maybe
            }
        }
    }

    private fun setSpinner() {
        setAdapter(
            binding.atvDay,
            arrayListOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Today", "Tomorrow")
        )
        val list = arrayListOf(
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        )
        val dayName = list[dayNumber - 1]
//        binding.atvDay.setText(dayName)
    }

    private fun setAdapter(view1: AutoCompleteTextView, list: ArrayList<String>) {
        val adapter: CustomAdapter = CustomAdapter(
            requireActivity(),
            R.layout.simple_spinner_item, list
        )
        view1.setAdapter(adapter)
        var no = dayNumber
        if (no >= 2 || no <= 6) no -= 2
        else no = 0
        view1.setText(list[no], false)
    }

    private fun setTimeRecycler() {
        var startTime = 8
        val arr = arrayOf("00", "15", "30", "45")
        val timeList: MutableList<TimeInfo> = ArrayList()
        var current = 0
        while (startTime != 5) {
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
        lec = ArrayList<ArrayList<LecInfo>>()
        FirebaseDatabase.getInstance().reference.child("Database").child("Timetables")
            .child("TE").child("TE2").child("K2").addValueEventListener(object :
                ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTheRecyclerView(lec: ArrayList<ArrayList<LecInfo>>, dayNo: Int) {

        //sunday is 1 and saturday is 7
        val tempDateNumber = if (dayNo == 1 || dayNo == 7) {
            Calendar.MONDAY - 2
        } else {
            dayNo - 2;
        }
        var temp = lec
        if (dayNo == dayNumber) {
            val changesList = setCurrentLec(lec, tempDateNumber)
            if(changesList.size ==2){
                temp[changesList[0]][changesList[1]].active = View.VISIBLE
                Log.d("VALUES","new values is${temp[changesList[0]][changesList[1]].active }")
            }
        }
        for(obj in temp[tempDateNumber]){
            Log.d("VAL_FINAL", obj.toString())
        }
        //-1 to make it 0 based index
        val adapter = activity?.let { LectureAdapter(it, getTimeAdjusted(temp[tempDateNumber])) }
        binding.rvLectures.layoutManager = LinearLayoutManager(activity)
        binding.rvLectures.isNestedScrollingEnabled = false
        binding.rvLectures.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setCurrentLec(
        lec: ArrayList<ArrayList<LecInfo>>,
        dayNo: Int
    ): ArrayList<Int> {
        var ans = lec
        var temp = ans[dayNo]
        //get current time
        val currentTime = LocalDateTime.now()

        val hrFormatter = DateTimeFormatter.ofPattern("HH")
        val minFormatter = DateTimeFormatter.ofPattern("mm")
        val hr = currentTime.format(hrFormatter).toInt()
        val min = currentTime.format(minFormatter).toInt()

        //check which one lies in the range
        Log.d("VALUES","time is $hr : $min")

        for (i in 0 until temp.size) {
            var current = temp[i]
            val sTime = current.startTime
            val sHr = sTime.split(":")[0].toInt()
            val sMin = sTime.split(":")[1].toInt()
            val eTime = current.endTime
            val eHr = eTime.split(":")[0].toInt()
            val eMin = eTime.split(":")[1].toInt()
            Log.d("VALUES","start is $sHr : $sMin, end is $eHr : $eMin")
            if((hr==sHr && min>=sMin)||(hr in (sHr) until eHr)||(hr==eHr && min<=eMin) ){
                Log.d("VALUES","set values is${ans[dayNo][i].active}")
                var withValues = ArrayList<Int>()
                withValues.add(dayNo)
                withValues.add(i)
                return withValues
            }
        }
        return ArrayList()
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
            Log.d("GAPS", "gapBefore = $gapBefore and gapBetween = $gapBetween \n")
            finalList.add(
                LecInfo(
                    current.lecName,
                    current.startTime,
                    current.endTime,
                    false,
                    gapInPixel(gapBetween),
                    current.active
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