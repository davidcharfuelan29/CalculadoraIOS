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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
            .background(colorResource(R.color.calculator_background))
            .safeDrawingPadding()
    ) {

        val sideMargin = maxWidth * 0.082f
        val gap = maxWidth * 0.03f
        val buttonSize = (maxWidth - sideMargin * 2 - gap * 3) / 4
        val bottomMargin = maxWidth * 0.17f

        val buttonNumberColor = colorResource(R.color.button_number)
        val buttonFunctionDarkColor = colorResource(R.color.button_function_dark)
        val buttonOperatorColor = colorResource(R.color.button_operator)
        val buttonHistoryColor = colorResource(R.color.button_history)
        val textPrimaryColor = colorResource(R.color.text_primary)
        val textTertiaryColor = colorResource(R.color.text_tertiary)
        val clearText = stringResource(R.string.button_clear)
        val percentText = stringResource(R.string.button_percent)
        val signText = stringResource(R.string.button_sign)
        val equalsText = stringResource(R.string.button_equals)
        val addText = stringResource(R.string.operation_add)
        val subtractText = stringResource(R.string.operation_subtract)
        val multiplyText = stringResource(R.string.operation_multiply)
        val divideText = stringResource(R.string.operation_divide)
        val historyButtonText = stringResource(R.string.button_history)
        val historyButtonDesc = stringResource(R.string.history_title)

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // Boton de historial
            Button(
                onClick = onHistoryClick,
                modifier = Modifier
                    .padding(start = sideMargin, top = 8.dp)
                    .size(48.dp)
                    .semantics { contentDescription = historyButtonDesc },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonHistoryColor
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = historyButtonText,
                    color = textPrimaryColor,
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
                color = textTertiaryColor,
                fontSize = 20.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )

            // Resultado en blanco (o mensaje de error)
            val errorCode = viewModel.error.value
            val displayResult = if (errorCode.isNotEmpty()) {
                when (errorCode) {
                    "error_division_zero" -> stringResource(R.string.error_division_zero)
                    "error_invalid_operation" -> stringResource(R.string.error_invalid_operation)
                    else -> errorCode
                }
            } else {
                viewModel.result.value
            }

            Text(
                text = displayResult,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = sideMargin),
                color = textPrimaryColor,
                fontSize = 55.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                softWrap = false
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Primera fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("⌫", buttonFunctionDarkColor, buttonSize) {
                    viewModel.deleteLastCharacter()
                }
                CalculatorButton(clearText, buttonFunctionDarkColor, buttonSize) {
                    viewModel.clearCalculator()
                }
                CalculatorButton(percentText, buttonFunctionDarkColor, buttonSize) {
                    viewModel.manageClickButtons("%")
                }
                CalculatorButton(divideText, buttonOperatorColor, buttonSize) {
                    viewModel.manageClickButtons("÷")
                }
            }

            // Segunda fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("7", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("7")
                }
                CalculatorButton("8", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("8")
                }
                CalculatorButton("9", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("9")
                }
                CalculatorButton(multiplyText, buttonOperatorColor, buttonSize) {
                    viewModel.manageClickButtons("×")
                }
            }

            // Tercera fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("4", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("4")
                }
                CalculatorButton("5", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("5")
                }
                CalculatorButton("6", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("6")
                }
                CalculatorButton(subtractText, buttonOperatorColor, buttonSize) {
                    viewModel.manageClickButtons("-")
                }
            }

            // Cuarta fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton("1", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("1")
                }
                CalculatorButton("2", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("2")
                }
                CalculatorButton("3", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("3")
                }
                CalculatorButton(addText, buttonOperatorColor, buttonSize) {
                    viewModel.manageClickButtons("+")
                }
            }

            // Quinta fila
            CalculatorRow(sideMargin, gap) {
                CalculatorButton(signText, buttonNumberColor, buttonSize) {
                    viewModel.changeSign()
                }
                CalculatorButton("0", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons("0")
                }
                CalculatorButton(".", buttonNumberColor, buttonSize) {
                    viewModel.manageClickButtons(".")
                }
                CalculatorButton(equalsText, buttonOperatorColor, buttonSize) {
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
            fontSize = 27.sp,
            color = colorResource(R.color.text_primary),
            maxLines = 1
        )
    }
}
