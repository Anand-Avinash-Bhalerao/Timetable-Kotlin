package com.sih.timetable.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sih.timetable.R
import com.sih.timetable.models.LecInfo

public const val FREE = 1
public const val LECTURE = 2

class LectureAdapter(private val context: Context, private val list: List<LecInfo>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FREE) {
            FreeHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_free_recy, parent, false)
            )
        } else {
            LectureHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_lecture_info, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = list[position]
        if (current.isFree) {
            val tempHolder: FreeHolder = holder as FreeHolder
            //view ka size set with the help of pair ka second number.
            tempHolder.backgroundCL.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                current.gap
            )

        } else {
            val tempHolder: LectureHolder = holder as LectureHolder
            tempHolder.backgroundCL.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                current.gap
            )
            tempHolder.lecNameTV.text = current.lecName
            tempHolder.lecTimeTV.text = current.startTime + " to " + current.endTime
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {

        return if (list[position].isFree) {
            FREE
        } else {
            LECTURE
        }
    }


    class LectureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var backgroundCL: ConstraintLayout = itemView.findViewById(R.id.cl_background)
        var lecNameTV: TextView = itemView.findViewById(R.id.tv_lectureName)
        var lecTimeTV: TextView = itemView.findViewById(R.id.tv_lectureTime)
    }

    class FreeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var backgroundCL: ConstraintLayout = itemView.findViewById(R.id.cl_background)
    }
}