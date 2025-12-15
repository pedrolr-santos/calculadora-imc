package com.example.calculadoraimc.feature.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Importante: Importe o viewModel do Jetpack Compose
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.feature.home.components.IMCCalculatorContainer
import com.example.calculadoraimc.feature.home.components.MainCard
import com.example.calculadoraimc.feature.home.viewmodel.AppViewModelFactory
import com.example.calculadoraimc.feature.home.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    imcDao: IMCDao,
    onNavigateToHistory: () -> Unit
) {
    // 1. INSTANCIA O VIEWMODEL
    // a 'Factory' ensina o android a criar o ViewModel injetando o banco de dados
    val viewModel: HomeViewModel = viewModel(
        factory = AppViewModelFactory(imcDao)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Calculadora de IMC", fontWeight = FontWeight.Bold)
                },
                actions = {
                    IconButton(onClick = { onNavigateToHistory() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Ver Histórico"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(12.dp))

            // entrada de dados
            IMCCalculatorContainer(
                onResult = { _, heightStr, weightStr, age, sex, activity ->

                    viewModel.calculateAndSave(
                        heightStr = heightStr,
                        weightStr = weightStr,
                        age = age,
                        sex = sex,
                        activity = activity
                    )
                }
            )

            Spacer(Modifier.height(28.dp))

            // 2. OBSERVA O ESTADO DO VIEWMODEL
            // s o ViewModel atualizar o 'imcResult', a tela redesenha sozinha aqui.
            viewModel.imcResult?.let { result ->
                MainCard(result)
            }
        }
    }
}