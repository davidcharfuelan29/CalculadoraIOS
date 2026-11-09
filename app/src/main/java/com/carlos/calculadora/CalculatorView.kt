package com.carlos.calculadora

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ShowCalculator(
    onHistoryClick: () -> Unit,
    viewModel: CalculatorViewModel = viewModel()
) {

    val result = viewModel.result.value

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .safeDrawingPadding()
    ) {

        val sideMargin = maxWidth * 0.082f
        val gap = maxWidth * 0.0175f
        val buttonSize = (maxWidth - sideMargin * 2 - gap * 3) / 4
        val bottomMargin = maxWidth * 0.17f

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Boton de historial
            Button(
                onClick = onHistoryClick,
                modifier = Modifier
                    .padding(start = sideMargin, top = 8.dp)
                    .size(48.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C2C2E)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "↶",
                    color = Color.White,
                    fontSize = 25.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Operacion en gris
            Text(
                text = viewModel.expression.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sideMargin),
                color = Color(0xFF8E8E93),
                fontSize = 20.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )

            // Resultado en blanco
            Text(
                text = result,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sideMargin),
                color = Color.White,
                fontSize = 55.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primera fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("⌫", Color(0xFF636366), buttonSize) {
                    viewModel.deleteLastCharacter()
                }
                CalculatorButton("AC", Color(0xFF636366), buttonSize) {
                    viewModel.clearCalculator()
                }
                CalculatorButton("%", Color(0xFF636366), buttonSize) {
                    viewModel.manageClickButtons("%")
                }
                CalculatorButton("÷", Color(0xFFFF9500), buttonSize) {
                    viewModel.manageClickButtons("÷")
                }
            }

            // Segunda fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("7", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("7")
                }
                CalculatorButton("8", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("8")
                }
                CalculatorButton("9", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("9")
                }
                CalculatorButton("×", Color(0xFFFF9500), buttonSize) {
                    viewModel.manageClickButtons("×")
                }
            }

            // Tercera fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("4", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("4")
                }
                CalculatorButton("5", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("5")
                }
                CalculatorButton("6", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("6")
                }
                CalculatorButton("−", Color(0xFFFF9500), buttonSize) {
                    viewModel.manageClickButtons("-")
                }
            }

            // Cuarta fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("1", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("1")
                }
                CalculatorButton("2", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("2")
                }
                CalculatorButton("3", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("3")
                }
                CalculatorButton("+", Color(0xFFFF9500), buttonSize) {
                    viewModel.manageClickButtons("+")
                }
            }

            // Quinta fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("±", Color(0xFF333333), buttonSize) {
                    viewModel.changeSign()
                }
                CalculatorButton("0", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons("0")
                }
                CalculatorButton(".", Color(0xFF333333), buttonSize) {
                    viewModel.manageClickButtons(".")
                }
                CalculatorButton("=", Color(0xFFFF9500), buttonSize) {
                    viewModel.showResult()
                }
            }

            Spacer(modifier = Modifier.height(bottomMargin))
        }
    }
}

@Composable
fun CalculatorRow(
    sideMargin: Dp,
    gap: Dp,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = sideMargin),
        horizontalArrangement = Arrangement.spacedBy(gap)
    ) {
        content()
    }
}

@Composable
fun CalculatorButton(
    text: String,
    backgroundColor: Color,
    buttonSize: Dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(buttonSize),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Text(
            text = text,
            fontSize = 23.sp,
            color = Color.White,
            maxLines = 1
        )
    }
}
