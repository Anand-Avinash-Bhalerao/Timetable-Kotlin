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
import com.sih.timetable.models.TimeInfo
import java.sql.Time

class TimeAdapter(private val context: Context, private val list: List<TimeInfo>) :
    RecyclerView.Adapter<TimeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_time_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = list[position]
        holder.hrTV.text = current.hr
        holder.minTV.text = current.min
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var hrTV: TextView = itemView.findViewById(R.id.hourTV)
        var minTV: TextView = itemView.findViewById(R.id.minTV)
    }
}