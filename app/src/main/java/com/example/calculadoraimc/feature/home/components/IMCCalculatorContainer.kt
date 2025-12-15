package com.example.calculadoraimc.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.datasource.Calculations
import com.example.calculadoraimc.feature.home.model.IMCData
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme

@Composable
fun IMCCalculatorContainer(
    // agora retornamos TUDO o que o usuário digitou
    onResult: (result: IMCData, height: String, weight: String, age: Int, sex: String, activity: String) -> Unit
) {
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    // "M" para Masculino, "F" para Feminino
    var selectedSex by remember { mutableStateOf("M") }

    // opções de atividade
    val activityOptions = listOf("Sedentário", "Leve", "Moderado", "Intenso")
    var selectedActivity by remember { mutableStateOf(activityOptions[0]) }
    var expandedActivity by remember { mutableStateOf(false) } // Controla se o menu está aberto

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Altura e Peso
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = height,
                onValueChange = { if (it.length <= 3) height = it },
                label = { Text("Altura (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = weight,
                onValueChange = { if (it.length <= 6) weight = it },
                label = { Text("Peso (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp)
            )
        }

        // Idade e Sexo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Input de Idade
            OutlinedTextField(
                modifier = Modifier.weight(0.4f),
                value = age,
                onValueChange = { if (it.length <= 3) age = it },
                label = { Text("Idade") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp)
            )

            // Seleção de Sexo (Dois botões)
            Row(
                modifier = Modifier.weight(0.6f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SexOption(text = "Homem", selected = selectedSex == "M") { selectedSex = "M" }
                SexOption(text = "Mulher", selected = selectedSex == "F") { selectedSex = "F" }
            }
        }

        // Nível de Atividade
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = selectedActivity,
                onValueChange = {},
                readOnly = true, // Não deixa digitar, só selecionar
                label = { Text("Nível de Atividade Física") },
                trailingIcon = {
                    Icon(Icons.Filled.ArrowDropDown, "Selecionar", Modifier.clickable { expandedActivity = true })
                },
                modifier = Modifier.fillMaxWidth().clickable { expandedActivity = true },
                shape = RoundedCornerShape(12.dp)
            )

            DropdownMenu(
                expanded = expandedActivity,
                onDismissRequest = { expandedActivity = false }
            ) {
                activityOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedActivity = option
                            expandedActivity = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        //  BOTÃO CALCULAR
        Button(
            modifier = Modifier.fillMaxWidth().height(50.dp),
            onClick = {
                // Validação simples antes de enviar
                if (height.isNotEmpty() && weight.isNotEmpty() && age.isNotEmpty()) {
                    Calculations.calculateIMC(height = height, weight = weight, response = { result ->
                        onResult(
                            result,
                            height,
                            weight,
                            age.toIntOrNull() ?: 0,
                            selectedSex,
                            selectedActivity
                        )
                    })
                }
            },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.White
            )
        ) {
            Text(text = "Calcular Indicadores", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// componente visual para o botão de Sexo
@Composable
fun SexOption(text: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewContainer() {
    CalculadoraIMCTheme {
        IMCCalculatorContainer { _, _, _, _, _, _ -> }
    }
}