
package com.carlos.calculadora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowHistory(
    onBack: () -> Unit,
    viewModel: CalculatorViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Historial",
            color = Color.White,
            fontSize = 30.sp
        )

        if (viewModel.history.isEmpty()) {

            Text(
                text = "No hay cálculos registrados",
                color = Color.Gray,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 20.dp)
            )

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {

                items(viewModel.history.reversed()) { operation ->

                    Text(
                        text = operation,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }
            }
        }

        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
        ) {
            Text("Volver")
        }
    }
}
