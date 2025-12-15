package com.example.calculadoraimc.datasource

import android.annotation.SuppressLint
import com.example.calculadoraimc.feature.home.model.IMCData

object Calculations {

    // --- SEU CÓDIGO ANTIGO (MANTIDO IGUAL) ---
    @SuppressLint("DefaultLocale")
    fun calculateIMC(height: String, weight: String, response: (IMCData) -> Unit) {
        if (height.isNotEmpty() && weight.isNotEmpty()) {
            // Mantendo sua lógica de trocar vírgula por ponto
            val heightFormatted = height.replace(",", ".").toDoubleOrNull()
            val weightFormatted = weight.replace(",", ".").toDoubleOrNull()

            if (weightFormatted != null && heightFormatted != null && heightFormatted > 0) {
                val alturaEmMetros = heightFormatted / 100
                val imc = weightFormatted / (alturaEmMetros * alturaEmMetros)
                val imcFormate = String.format("%.2f", imc)

                val imcData = when {
                    imc < 18.5 -> IMCData(imcFormate, "Abaixo do peso", imc)
                    imc < 25 -> IMCData(imcFormate, "Peso normal", imc)
                    imc < 30 -> IMCData(imcFormate, "Sobrepeso", imc)
                    imc < 35 -> IMCData(imcFormate, "Obesidade Grau I", imc)
                    imc < 40 -> IMCData(imcFormate, "Obesidade Grau II", imc)
                    else -> IMCData(imcFormate, "Obesidade Grau III", imc)
                }

                response(imcData)
            } else {
                response(IMCData("null", "Valores inválidos", 0.0))
            }
        } else {
            response(IMCData("null", "Nenhum IMC Calculado", 0.0))
        }
    }

    // --- NOVAS FUNÇÕES (ADICIONADAS PARA O TRABALHO) ---

    // 1. Taxa Metabólica Basal (Mifflin-St Jeor)
    fun calculateTMB(weight: Double, heightCm: Double, age: Int, sex: String): Double {
        // Fórmula: (10 x peso) + (6.25 x altura) - (5 x idade) + (5 ou -161)
        val base = (10 * weight) + (6.25 * heightCm) - (5 * age)

        return if (sex == "M") {
            base + 5 // Homens
        } else {
            base - 161 // Mulheres
        }
    }

    // 2. Peso Ideal (Fórmula de Devine adaptada)
    fun calculateIdealWeight(heightCm: Double, sex: String): Double {
        // Altura base 152.4 cm (5 pés)
        val heightOverBase = heightCm - 152.4

        // Se for muito baixinho, a fórmula não se aplica bem, retornamos 0 ou um base mínimo
        if (heightOverBase <= 0) return 0.0

        // Cada polegada (2.54cm) acima de 5 pés adiciona:
        // Homem: 50kg + 2.3kg por polegada
        // Mulher: 45.5kg + 2.3kg por polegada
        // Simplificando para cm: ~0.9kg por cm extra

        val baseWeight = if (sex == "M") 50.0 else 45.5
        val extraWeight = 0.9 * heightOverBase

        return baseWeight + extraWeight
    }

    // 3. Necessidade Calórica Diária
    fun calculateDailyCalories(tmb: Double, activityLevel: String): Double {
        val factor = when (activityLevel) {
            "Sedentário" -> 1.2
            "Leve" -> 1.375
            "Moderado" -> 1.55
            "Intenso" -> 1.725
            else -> 1.2
        }
        return tmb * factor
    }
}