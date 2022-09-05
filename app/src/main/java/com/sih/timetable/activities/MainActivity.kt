package com.sih.timetable.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sih.timetable.R
import com.sih.timetable.databinding.ActivityMainBinding
import com.sih.timetable.fragment.FoodFragment
import com.sih.timetable.fragment.HomeFragment
import com.sih.timetable.fragment.LinksFragment
import com.sih.timetable.fragment.TimetableFragment
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomBar = findViewById<AnimatedBottomBar>(R.id.bottom_bar)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                var fragment: Fragment? = null
                when (newTab.id) {
                    R.id.navigation_home -> fragment = HomeFragment()
                    R.id.navigation_food -> fragment = FoodFragment()
                    R.id.navigation_timetable -> fragment = TimetableFragment()
                    R.id.navigation_links -> fragment = LinksFragment()
                }
                fragment?.let { loadFragment(it) }
            }

            // An optional method that will be fired whenever an already selected tab has been selected again.
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })
    }

    private fun loadFragment(fragment: Fragment?) {
        //switching fragment
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

}