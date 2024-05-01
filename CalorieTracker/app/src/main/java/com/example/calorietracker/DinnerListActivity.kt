package com.example.calorietracker

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DinnerListActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val foodList = ArrayList<MainActivity.Food>()
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        listView = findViewById(R.id.listView)

        db.collection("dinner")
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

                // Una vez que se hayan cargado los datos desde la base de datos,
                // actualizamos el ListView con los nombres y las calorías de las comidas
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
    }

    // Función para convertir una cadena de calorías en un valor entero
    private fun parseCalorias(caloriasStr: String): Int {
        // Eliminar caracteres no numéricos excepto comas y puntos
        val cleanString = caloriasStr.replace(Regex("[^\\d,.]"), "")
        // Reemplazar comas con puntos para que el valor sea parseable
        val parseableString = cleanString.replace(',', '.')
        // Intentar convertir la cadena parseable en un valor flotante
        val floatValue = parseableString.toFloatOrNull() ?: 0.0f
        // Convertir el valor flotante a un entero
        return floatValue.toInt()
    }
}