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
    data class Food(val nombre: String, val calorias: Int)
    val foodList = mutableListOf<Food>()

    // Función para convertir una cadena de calorías en un valor entero
    fun parseCalorias(caloriasStr: String): Int {
        // Eliminar caracteres no numéricos excepto comas y puntos
        val cleanString = caloriasStr.replace(Regex("[^\\d,.]"), "")
        // Reemplazar comas con puntos para que el valor sea parseable
        val parseableString = cleanString.replace(',', '.')
        // Intentar convertir la cadena parseable en un valor flotante
        val floatValue = parseableString.toFloatOrNull() ?: 0.0f
        // Convertir el valor flotante a un entero
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

        val optionsButton = findViewById<Button>(R.id.viewOptionsButton)
        optionsButton.setOnClickListener {
            val intent = Intent(this, OptionsActivity::class.java)
            startActivityForResult(intent, optionsRequestCode, null)
        }
        val breakfastButton = findViewById<Button>(R.id.viewBreakfastButton)
        breakfastButton.setOnClickListener {
            // Crear un Intent para abrir la nueva actividad
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        db.collection("foods")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val nombre = document.getString("food") ?: ""
                    val caloriasStr = document.getString("calories") ?: "0"
                    val calorias = parseCalorias(caloriasStr)
                    val food = Food(nombre, calorias)
                    foodList.add(food)
                }
                Log.d("LISTA DE COMIDAS", foodList.toString())
                Log.d("SUCCESS", "Success getting documents.")
            }
            .addOnFailureListener { exception ->
                Log.w("ERROR", "Error getting documents.", exception)
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
            } else {
                val newCaloriesValueAfterDifference = newCaloriesValue.toInt() + newDifferenceValue.toInt()
                caloriesTextView.text = newCaloriesValueAfterDifference.toString()
            }
        }
    }
}
