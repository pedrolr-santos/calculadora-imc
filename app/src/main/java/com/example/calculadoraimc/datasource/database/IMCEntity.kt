package com.example.calculadoraimc.datasource.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "imc_history")
data class IMCEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val height: String,
    val weight: String,
    val imc: Double,
    val classification: String,

    // --- NOVOS CAMPOS ---
    val age: Int,
    val sex: String,           // Vamos salvar como "M" ou "F"
    val activityLevel: String, // Ex: "Sedentário", "Moderado", etc.

    // Resultados adicionais (Calcularemos depois, mas já deixamos espaço)
    val tmb: Double = 0.0,
    val idealWeight: Double = 0.0,
    val dailyCalories: Double = 0.0,

    val timestamp: Long = System.currentTimeMillis()
)