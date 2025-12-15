package com.example.calculadoraimc.feature.details.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.feature.details.viewmodel.DetailsViewModel
import com.example.calculadoraimc.feature.home.viewmodel.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    imcId: Int,
    imcDao: IMCDao,
    onBack: () -> Unit
) {
    val viewModel: DetailsViewModel = viewModel(
        factory = AppViewModelFactory(imcDao)
    )

    LaunchedEffect(imcId) {
        viewModel.fetchDetails(imcId)
    }

    val imcRecord = viewModel.uiState

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Detalhes da Medição") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            imcRecord?.let { record ->

                Spacer(modifier = Modifier.height(20.dp))

                IMCGraphicDetails(imcValue = record.imc)

                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Resultado do IMC",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoCard(
                    label = "Seu IMC",
                    value = "%.2f".format(record.imc)
                )

                Spacer(modifier = Modifier.height(8.dp))

                InfoCard(
                    label = "Classificação",
                    value = record.classification
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Peso",
                        value = "${record.weight} kg"
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Altura",
                        value = "${record.height} cm"
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Idade",
                        value = "${record.age} anos"
                    )
                    InfoCard(
                        modifier = Modifier.weight(1f),
                        label = "Sexo",
                        value = if (record.sex == "M") "Masculino" else "Feminino"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Análise Corporal Avançada",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoCard(
                    label = "Taxa Metabólica Basal (TMB)",
                    value = "%.0f kcal".format(record.tmb)
                )

                Text(
                    text = "Calorias que seu corpo gasta em repouso.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                InfoCard(
                    label = "Necessidade Calórica Diária",
                    value = "%.0f kcal".format(record.dailyCalories)
                )

                Text(
                    text = "Baseado no nível: ${record.activityLevel}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                InfoCard(
                    label = "Peso Ideal Estimado",
                    value = "%.1f kg".format(record.idealWeight)
                )

                Text(
                    text = "Segundo a fórmula de Devine.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(40.dp))

            } ?: run {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

/* ===================== COMPONENTES AUXILIARES ===================== */

@Composable
fun InfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun IMCGraphicDetails(imcValue: Double) {
    val percentValue = (imcValue.coerceAtMost(40.0)) / 40.0
    val animationProgress = remember { Animatable(0f) }

    val graphicColor = when {
        imcValue < 18.5 -> Color(0xFF2196F3)
        imcValue < 25.0 -> Color(0xFF4CAF50)
        imcValue < 30.0 -> Color(0xFFFFC107)
        else -> Color(0xFFFF5252)
    }

    LaunchedEffect(imcValue) {
        animationProgress.animateTo(
            targetValue = percentValue.toFloat(),
            animationSpec = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            )
        )
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center
    ) {
        val size = 200.dp

        Canvas(
            modifier = Modifier.size(size)
        ) {
            val strokeWidth = 35f

            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = 140f,
                sweepAngle = 260f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            drawArc(
                color = graphicColor,
                startAngle = 140f,
                sweepAngle = 260f * animationProgress.value,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            tint = graphicColor,
            modifier = Modifier.size(40.dp)
        )
    }
}
