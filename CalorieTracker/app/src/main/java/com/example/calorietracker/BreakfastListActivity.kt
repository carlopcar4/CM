package com.example.calorietracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BreakfastListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val foodList = ArrayList<MainActivity.Food>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        listView = findViewById(R.id.listView)

        db.collection("breakfast")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombre = document.getString("name") ?: ""
                    val caloriasStr = document.getString("calories") ?: "0"
                    val calorias = parseCalorias(caloriasStr)
                    val food = MainActivity.Food(nombre, calorias)
                    foodList.add(food)
                }
                Log.d("SUCCESS", "Success getting documents.")

                val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    android.R.id.text1,
                    foodList.map { "${it.nombre}\n${it.calorias} calories" }
                )
                listView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR", "Error getting documents.", exception)
            }
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedFoodCalories = foodList[position].calorias
            val intent = Intent()
            intent.putExtra("selectedFoodCalories", selectedFoodCalories.toString())
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun parseCalorias(caloriasStr: String): Int {
        val cleanString = caloriasStr.replace(Regex("[^\\d,.]"), "")
        val parseableString = cleanString.replace(',', '.')
        val floatValue = parseableString.toFloatOrNull() ?: 0.0f
        return floatValue.toInt()
    }
}