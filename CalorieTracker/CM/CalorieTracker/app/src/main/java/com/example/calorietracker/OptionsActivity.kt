package com.example.calorietracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Switch


class OptionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        val switchDeficit = findViewById<Switch>(R.id.switchModeDeficit)
        val switchSurplus = findViewById<Switch>(R.id.switchModeSurplus)
        switchDeficit.setChecked(true)
        switchDeficit.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchSurplus.setChecked(false)
            }
        })
        switchSurplus.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                switchDeficit.setChecked(false)
            }
        })
    }
}