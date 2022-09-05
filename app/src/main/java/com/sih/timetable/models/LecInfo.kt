package com.sih.timetable.models

data class LecInfo(
    var lecName: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var isFree:Boolean = false,
    var gap:Int = 4
)