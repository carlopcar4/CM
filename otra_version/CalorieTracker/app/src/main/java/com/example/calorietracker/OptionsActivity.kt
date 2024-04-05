package com.example.calorietracker

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class OptionsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var switchModeDeficit: Switch
    private lateinit var switchModeSurplus: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        sharedPreferences = getSharedPreferences("CaloriePrefs", MODE_PRIVATE)

        val insertCaloriesView = findViewById<EditText>(R.id.insertCaloriesView)
        insertCaloriesView.setText(sharedPreferences.getString("caloriesValue", ""))

        val insertDifferenceView = findViewById<EditText>(R.id.insertDifferenceView)
        insertDifferenceView.setText(sharedPreferences.getString("differenceValue", ""))



        switchModeDeficit = findViewById(R.id.switchModeDeficit)
        switchModeSurplus = findViewById(R.id.switchModeSurplus)

        // Restaurar el estado del Switch
        switchModeDeficit.isChecked = sharedPreferences.getBoolean("isDeficitChecked", false)
        switchModeSurplus.isChecked = sharedPreferences.getBoolean("isSurplusChecked", false)

        switchModeDeficit.setOnCheckedChangeListener { _, isChecked ->
            switchModeSurplus.isChecked = !isChecked // Desmarcar el otro Switch

            // Guardar el estado del Switch en las preferencias compartidas
            sharedPreferences.edit().putBoolean("isDeficitChecked", isChecked).apply()
        }

        switchModeSurplus.setOnCheckedChangeListener { _, isChecked ->
            switchModeDeficit.isChecked = !isChecked // Desmarcar el otro Switch

            // Guardar el estado del Switch en las preferencias compartidas
            sharedPreferences.edit().putBoolean("isSurplusChecked", isChecked).apply()
        }

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            val caloriesValue = insertCaloriesView.text.toString()
            val differenceValue = insertDifferenceView.text.toString()

            val calories = caloriesValue.toIntOrNull() ?: 0
            val difference = differenceValue.toIntOrNull() ?: 0

            val isDeficitActive = switchModeDeficit.isChecked

            // Si el modo de déficit está activo, restar la diferencia del total de calorías
            val totalCalories = if (isDeficitActive) {
                calories - difference
            } else {
                calories + difference
            }

            // Guarda los valores en SharedPreferences
            saveCaloriesValue(caloriesValue)
            saveDifferenceValue(differenceValue)

            // Envía el resultado de vuelta a la actividad principal
            val intent = Intent()
            intent.putExtra("caloriesValue", totalCalories.toString()) // Pasar totalCalories en lugar de caloriesValue
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun saveCaloriesValue(value: String) {
        sharedPreferences.edit().putString("caloriesValue", value).apply()
    }

    private fun saveDifferenceValue(value: String) {
        sharedPreferences.edit().putString("differenceValue", value).apply()
    }
}
