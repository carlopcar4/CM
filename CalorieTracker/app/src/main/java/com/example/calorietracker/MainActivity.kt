package com.example.calorietracker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val optionsRequestCode = 1
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var caloriesTextView: TextView
    private lateinit var caloriesConsumed: TextView
    private val breakfastListRequestCode = 100
    private val lunchListRequestCode = 101
    private val dinnerListRequestCode = 102
    private val snacksListRequestCode = 103
    data class Food(val nombre: String, val calorias: Int)
    val foodList = mutableListOf<Food>()

    fun parseCalorias(caloriasStr: String): Int {
        val cleanString = caloriasStr.replace(Regex("[^\\d,.]"), "")
        val parseableString = cleanString.replace(',', '.')
        val floatValue = parseableString.toFloatOrNull() ?: 0.0f
        return floatValue.toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Firebase.firestore

        sharedPreferences = getSharedPreferences("CaloriePrefs", MODE_PRIVATE)
        val caloriesValue = sharedPreferences.getString("caloriesValue", "0")

        caloriesTextView = findViewById(R.id.textViewCalories)
        caloriesTextView.text = caloriesValue

        val savedCaloriesTextView = sharedPreferences.getInt("caloriesTextView", 0)
        caloriesTextView.text = savedCaloriesTextView.toString()

        caloriesConsumed = findViewById(R.id.viewCaloriesConsumed)
        caloriesConsumed.text = "0"

        val savedCaloriesConsumed = sharedPreferences.getInt("caloriesConsumed", 0)
        caloriesConsumed.text = savedCaloriesConsumed.toString()

        val optionsButton = findViewById<Button>(R.id.viewOptionsButton)
        optionsButton.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivityForResult(intent, optionsRequestCode, null)
        }
        val breakfastButton = findViewById<Button>(R.id.viewBreakfastButton)
        breakfastButton.setOnClickListener {
            val intent = Intent(this, BreakfastListActivity::class.java)
            startActivityForResult(intent, breakfastListRequestCode)
        }

        val lunchButton = findViewById<Button>(R.id.viewLunchButton)
        lunchButton.setOnClickListener {
            val intent = Intent(this, LunchListActivity::class.java)
            startActivityForResult(intent, lunchListRequestCode)
        }

        val dinnerButton = findViewById<Button>(R.id.viewDinnerButton)
        dinnerButton.setOnClickListener {
            val intent = Intent(this, DinnerListActivity::class.java)
            startActivityForResult(intent, dinnerListRequestCode)
        }

        val snacksButton = findViewById<Button>(R.id.viewSnacksButton)
        snacksButton.setOnClickListener {
            val intent = Intent(this, SnackListActivity::class.java)
            startActivityForResult(intent, snacksListRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == optionsRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val newCaloriesValue = data.getStringExtra("caloriesValue") ?: "0"
            val newDifferenceValue = data.getStringExtra("differenceValue") ?: "0"
            val deficit = sharedPreferences.getBoolean("isDeficitChecked", false)
            Log.d("DEFICIT: ", deficit.toString())
            if (deficit) {
                val newCaloriesValueAfterDifference = newCaloriesValue.toInt() - newDifferenceValue.toInt()
                caloriesTextView.text = newCaloriesValueAfterDifference.toString()

                val editor = sharedPreferences.edit()
                editor.putInt("caloriesTextView", newCaloriesValueAfterDifference)
                editor.apply()
            } else {
                val newCaloriesValueAfterDifference = newCaloriesValue.toInt() + newDifferenceValue.toInt()
                caloriesTextView.text = newCaloriesValueAfterDifference.toString()

                val editor = sharedPreferences.edit()
                editor.putInt("caloriesTextView", newCaloriesValueAfterDifference)
                editor.apply()
            }
        } else if (requestCode == breakfastListRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFoodCalories = data.getStringExtra("selectedFoodCalories")?.toInt() ?: 0
            val currentCaloriesConsumed = caloriesConsumed.text.toString().toInt()
            val newCaloriesConsumed = currentCaloriesConsumed + selectedFoodCalories
            caloriesConsumed.text = newCaloriesConsumed.toString()
            val editor = sharedPreferences.edit()
            editor.putInt("caloriesConsumed", newCaloriesConsumed)
            editor.apply()
        } else if (requestCode == lunchListRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFoodCalories = data.getStringExtra("selectedFoodCalories")?.toInt() ?: 0
            val currentCaloriesConsumed = caloriesConsumed.text.toString().toInt()
            val newCaloriesConsumed = currentCaloriesConsumed + selectedFoodCalories
            caloriesConsumed.text = newCaloriesConsumed.toString()
            val editor = sharedPreferences.edit()
            editor.putInt("caloriesConsumed", newCaloriesConsumed)
            editor.apply()
        } else if (requestCode == dinnerListRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFoodCalories = data.getStringExtra("selectedFoodCalories")?.toInt() ?: 0
            val currentCaloriesConsumed = caloriesConsumed.text.toString().toInt()
            val newCaloriesConsumed = currentCaloriesConsumed + selectedFoodCalories
            caloriesConsumed.text = newCaloriesConsumed.toString()
        } else if (requestCode == snacksListRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            val selectedFoodCalories = data.getStringExtra("selectedFoodCalories")?.toInt() ?: 0
            val currentCaloriesConsumed = caloriesConsumed.text.toString().toInt()
            val newCaloriesConsumed = currentCaloriesConsumed + selectedFoodCalories
            caloriesConsumed.text = newCaloriesConsumed.toString()
            val editor = sharedPreferences.edit()
            editor.putInt("caloriesConsumed", newCaloriesConsumed)
            editor.apply()
        }
    }
}
