package com.example.calculadoraimc.feature.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadoraimc.datasource.Calculations
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.datasource.database.IMCEntity
import com.example.calculadoraimc.feature.home.model.IMCData
import kotlinx.coroutines.launch

class HomeViewModel(private val dao: IMCDao) : ViewModel() {

    // estado da UI (o que aparece na tela)
    var imcResult by mutableStateOf<IMCData?>(null)
        private set

    // função que a tela chama quando o usuário clica em "Calcular"
    fun calculateAndSave(
        heightStr: String,
        weightStr: String,
        age: Int,
        sex: String,
        activity: String
    ) {
        // 1. calcular o IMC (visual)
        Calculations.calculateIMC(height = heightStr, weight = weightStr) { result ->
            imcResult = result

            // 2. calcular extras e salvar no Banco (background)
            viewModelScope.launch {
                val weight = weightStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                val height = heightStr.replace(",", ".").toDoubleOrNull() ?: 0.0

                val tmbValue = Calculations.calculateTMB(weight, height, age, sex)
                val idealWeightValue = Calculations.calculateIdealWeight(height, sex)
                val caloriesValue = Calculations.calculateDailyCalories(tmbValue, activity)

                val imcValueParaBanco = result.value.replace(",", ".").toDoubleOrNull() ?: 0.0

                val novaMedicao = IMCEntity(
                    height = heightStr,
                    weight = weightStr,
                    imc = imcValueParaBanco,
                    classification = result.text,
                    age = age,
                    sex = sex,
                    activityLevel = activity,
                    tmb = tmbValue,
                    idealWeight = idealWeightValue,
                    dailyCalories = caloriesValue
                )
                dao.insert(novaMedicao)
            }
        }
    }
}