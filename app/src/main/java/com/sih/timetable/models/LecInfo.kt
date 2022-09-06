package com.sih.timetable.models

import android.view.View

data class LecInfo(
    var lecName: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var isFree:Boolean = false,
    var gap:Int = 4,
    var active:Int = View.INVISIBLE
)