
package com.carlos.calculadora

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val model = CalculatorModel()

    val result = mutableStateOf("0")
    val expression = mutableStateOf("")
    var contentResult = mutableListOf("0")

    // Lista donde se guardan las operaciones
    val history = mutableStateListOf<String>()

    fun manageClickButtons(valueButton: String) {

        if (valueButton.isEmpty()) return

        val operators = listOf("+", "-", "×", "÷", "%")

        if (valueButton in operators) {

            val lastCharacter = result.value.last().toString()

            if (lastCharacter in operators) return
        }

        if (valueButton == ".") {

            var currentNumber = result.value

            if (currentNumber.contains("+")) {
                currentNumber = currentNumber.substringAfterLast("+")
            }

            if (currentNumber.contains("-")) {
                currentNumber = currentNumber.substringAfterLast("-")
            }

            if (currentNumber.contains("×")) {
                currentNumber = currentNumber.substringAfterLast("×")
            }

            if (currentNumber.contains("÷")) {
                currentNumber = currentNumber.substringAfterLast("÷")
            }

            if (currentNumber.contains("%")) {
                currentNumber = currentNumber.substringAfterLast("%")
            }

            if (currentNumber.contains(".")) return
        }

        if (result.value == "0" &&
            valueButton != "+" &&
            valueButton != "-" &&
            valueButton != "×" &&
            valueButton != "÷" &&
            valueButton != "%" &&
            valueButton != "."
        ) {
            contentResult.clear()
        }

        contentResult.add(valueButton)

        result.value = contentResult.joinToString(separator = "")
        expression.value = result.value
    }

    fun clearCalculator() {

        contentResult.clear()
        contentResult.add("0")

        result.value = "0"
        expression.value = ""
    }

    fun deleteLastCharacter() {

        if (contentResult.isEmpty()) return

        if (contentResult.size == 1) {
            clearCalculator()
            return
        }

        contentResult.removeAt(contentResult.lastIndex)

        result.value = contentResult.joinToString(separator = "")
        expression.value = result.value
    }

    fun changeSign() {

        if (result.value == "0") return

        if (result.value.startsWith("-")) {
            result.value = result.value.substring(1)
        } else {
            result.value = "-" + result.value
        }

        contentResult.clear()
        contentResult.add(result.value)

        expression.value = result.value
    }

    fun calculateResult(): Double {

        val expression = result.value

        var operator = ""
        var position = -1

        if (expression.contains("+")) {
            operator = "+"
            position = expression.indexOf("+")
        } else if (expression.indexOf("-") > 0) {
            operator = "-"
            position = expression.indexOf("-")
        } else if (expression.contains("×")) {
            operator = "×"
            position = expression.indexOf("×")
        } else if (expression.contains("÷")) {
            operator = "÷"
            position = expression.indexOf("÷")
        } else if (expression.contains("%")) {
            operator = "%"
            position = expression.indexOf("%")
        }

        if (position == -1) return 0.0

        val number1 = expression.substring(0, position).toDoubleOrNull()
        val number2 = expression.substring(position + 1).toDoubleOrNull()

        if (number1 == null || number2 == null) return 0.0

        when (operator) {

            "+" -> return model.add(number1, number2)

            "-" -> return model.subtraction(number1, number2)

            "×" -> return model.multiply(number1, number2)

            "÷" -> {
                if (number2 == 0.0) return 0.0
                return model.divide(number1, number2)
            }

            "%" -> {
                if (number2 == 0.0) return 0.0
                return model.module(number1, number2)
            }
        }

        return 0.0
    }

    fun showResult() {

        if (!result.value.contains("+") &&
            !result.value.contains("-") &&
            !result.value.contains("×") &&
            !result.value.contains("÷") &&
            !result.value.contains("%")
        ) {
            return
        }

        // Guardamos la operación original
        val operation = result.value

        expression.value = operation

        val answer = calculateResult()

        // Registramos la operación en el historial
        history.add("$operation = $answer")

        result.value = answer.toString()

        contentResult.clear()
        contentResult.add(result.value)
    }
}
