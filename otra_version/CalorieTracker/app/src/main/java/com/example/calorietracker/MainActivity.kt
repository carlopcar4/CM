package com.example.calorietracker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val optionsRequestCode = 1
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var caloriesTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("CaloriePrefs", MODE_PRIVATE)
        val caloriesValue = sharedPreferences.getString("caloriesValue", "0")

        caloriesTextView = findViewById(R.id.textViewCalories)
        caloriesTextView.text = caloriesValue

        val optionsButton = findViewById<Button>(R.id.viewOptionsButton)
        optionsButton.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivityForResult(intent, optionsRequestCode, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == optionsRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val newCaloriesValue = data.getStringExtra("caloriesValue") ?: "0"
            caloriesTextView.text = newCaloriesValue
        }
    }
}
