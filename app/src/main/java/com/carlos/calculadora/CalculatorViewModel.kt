
package com.carlos.calculadora

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    private val model = CalculatorModel()

    val result = mutableStateOf("0")
    val expression = mutableStateOf("")
    val contentResult = mutableListOf("0")
    val error = mutableStateOf("")

    // Lista donde se guardan las operaciones
    val history = mutableStateListOf<String>()

    fun manageClickButtons(valueButton: String) {

        // Si hay error previo, limpiarlo al comenzar una nueva operaci�n
        if (error.value.isNotEmpty()) {
            error.value = ""
            contentResult.clear()
            contentResult.add("0")
            result.value = "0"
            expression.value = ""
        }

        if (valueButton.isEmpty()) return

        val operators = listOf("+", "-", "×", "÷", "%")

        if (valueButton in operators) {

            val lastCharacter = result.value.last().toString()

            if (lastCharacter in operators) {
                // Permitir "-" como signo negativo despu�s de un operador
                if (valueButton != "-") return
                if (lastCharacter == "-") return
            }

            // Si la calculadora est� en "0" y se presiona "-", iniciar n�mero negativo
            if (result.value == "0" && valueButton == "-") {
                contentResult.clear()
                contentResult.add("-")
                result.value = "-"
                expression.value = "-"
                return
            }
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

        // Respetar el mecanismo de recuperaci�n de error previo
        if (error.value.isNotEmpty()) {
            error.value = ""
            contentResult.clear()
            contentResult.add("0")
            result.value = "0"
            expression.value = ""
            return
        }

        if (result.value == "0") return

        val currentExpression = result.value

        // Buscar el final del �ltimo n�mero (escaneo hacia atr�s por d�gitos y punto decimal)
        var lastNumberStart = currentExpression.lastIndex

        while (lastNumberStart >= 0 &&
               (currentExpression[lastNumberStart].isDigit() || currentExpression[lastNumberStart] == '.')) {
            lastNumberStart--
        }

        // lastNumberStart apunta al car�cter antes del n�mero
        var actualStart = lastNumberStart + 1

        // Si el car�cter antes del d�gitos es '-', verificar si es signo negativo del n�mero
        if (actualStart > 0 && currentExpression[actualStart - 1] == '-') {
            // Es signo negativo si est� al inicio o si el car�cter anterior es un operador
            if (actualStart - 1 == 0 ||
                currentExpression[actualStart - 2] in listOf('+', '-', '×', '÷', '%')
            ) {
                actualStart-- // Incluir el '-' como parte del n�mero
            }
        }

        val lastNumberStr = currentExpression.substring(actualStart)

        if (lastNumberStr.isEmpty()) return

        // Cambiar el signo del �ltimo n�mero
        val newLastNumber = if (lastNumberStr.startsWith("-")) {
            lastNumberStr.substring(1)
        } else {
            "-" + lastNumberStr
        }

        val prefix = currentExpression.substring(0, actualStart)
        val newExpression = prefix + newLastNumber

        result.value = newExpression
        this.expression.value = newExpression

        // Actualizar contentResult con los caracteres de la nueva expresi�n
        contentResult.clear()
        newExpression.forEach { contentResult.add(it.toString()) }
    }

    fun calculateResult(): Double {

        error.value = ""

        val expression = result.value

        // Dividir la expresi�n en tokens (n�meros y operadores)
        val tokens = tokenize(expression)

        if (tokens.isEmpty()) {
            error.value = "error_invalid_operation"
            return 0.0
        }

        // Verificar que la expresi�n no termine en un operador binario incompleto (ej. "5 +", "10 �")
        // "%" al final es v�lido porque es un porcentaje sobre el n�mero anterior (ej. "100 + 10 %")
        val lastToken = tokens.last()
        if (lastToken in listOf("+", "-", "×", "÷")) {
            error.value = "error_invalid_operation"
            return 0.0
        }

        // Paso 0: procesar porcentajes (n�mero � 100)
        val step0 = processPorcentaje(tokens)

        if (step0.isEmpty()) {
            error.value = "error_invalid_operation"
            return 0.0
        }

        // Paso 1: procesar multiplicaciones y divisiones de izquierda a derecha
        val step1 = processMultiplicacionDivision(step0)

        if (step1.isEmpty()) {
            error.value = "error_invalid_operation"
            return 0.0
        }

        // Paso 2: procesar sumas y restas de izquierda a derecha
        val answer = processSumaResta(step1)

        return answer
    }

    private fun processPorcentaje(tokens: List<String>): MutableList<String> {
        val result = mutableListOf<String>()
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]

            if (token == "%") {
                if (result.isEmpty()) {
                    error.value = "error_invalid_operation"
                    return mutableListOf()
                }

                // Obtener el n�mero antes de %
                val bStr = result.removeAt(result.lastIndex)
                val b = bStr.toDoubleOrNull()

                if (b == null) {
                    error.value = "error_invalid_operation"
                    return mutableListOf()
                }

                // Determinar el operador antes de b (si existe)
                var porcentajeValor: Double

                if (result.isNotEmpty() && result.last() in listOf("+", "-")) {
                    // Suma o resta: porcentaje sobre el primer operando
                    val opBefore = result.removeAt(result.lastIndex)

                    if (result.isEmpty()) {
                        error.value = "error_invalid_operation"
                        return mutableListOf()
                    }

                    val aStr = result.removeAt(result.lastIndex)
                    val a = aStr.toDoubleOrNull()

                    if (a == null) {
                        error.value = "error_invalid_operation"
                        return mutableListOf()
                    }

                    // a + b% = a + (a � b / 100), a - b% = a - (a � b / 100)
                    porcentajeValor = model.multiply(a, b) / 100.0

                    // Devolver a y el operador, seguido del valor del porcentaje
                    result.add(aStr)
                    result.add(opBefore)
                } else {
                    // Multiplicaci�n, divisi�n o sin operador: b / 100
                    porcentajeValor = model.porcentaje(b)
                }

                result.add(porcentajeValor.toString())
                i++
            } else {
                result.add(token)
                i++
            }
        }

        return result
    }

    private fun tokenize(expression: String): MutableList<String> {
        val tokens = mutableListOf<String>()
        var currentNumber = ""

        for (i in expression.indices) {
            val char = expression[i]
            val charStr = char.toString()

            if (char.isDigit() || char == '.') {
                currentNumber += charStr
            } else if (charStr in listOf("+", "-", "×", "÷", "%")) {
                // Si es menos al inicio o despu�s de un operador, es signo negativo
                if (char == '-' && currentNumber.isEmpty() &&
                    (tokens.isEmpty() || tokens.last() in listOf("+", "-", "×", "÷", "%"))
                ) {
                    currentNumber = "-"
                } else {
                    if (currentNumber.isNotEmpty()) {
                        tokens.add(currentNumber)
                        currentNumber = ""
                    }
                    tokens.add(charStr)
                }
            }
        }

        // Agregar el �ltimo n�mero
        if (currentNumber.isNotEmpty()) {
            tokens.add(currentNumber)
        }

        return tokens
    }

    private fun processMultiplicacionDivision(tokens: List<String>): MutableList<String> {
        val result = mutableListOf<String>()
        var i = 0

        while (i < tokens.size) {
            val token = tokens[i]

            if (token == "×" || token == "÷") {
                val left = result.removeAt(result.lastIndex).toDoubleOrNull()
                val right = tokens[i + 1].toDoubleOrNull()

                if (left == null || right == null) {
                    error.value = "error_invalid_operation"
                    return mutableListOf()
                }

                val calculated = when (token) {
                    "×" -> model.multiply(left, right)
                    "÷" -> {
                        if (right == 0.0) {
                            error.value = "error_division_zero"
                            return mutableListOf()
                        }
                        model.divide(left, right)
                    }
                    else -> 0.0
                }

                result.add(calculated.toString())
                i += 2
            } else {
                result.add(token)
                i++
            }
        }

        return result
    }

    private fun processSumaResta(tokens: List<String>): Double {
        if (tokens.isEmpty()) return 0.0

        var answer = tokens[0].toDoubleOrNull() ?: 0.0
        var i = 1

        while (i < tokens.size) {
            val operator = tokens[i]
            val right = tokens[i + 1].toDoubleOrNull() ?: 0.0

            when (operator) {
                "+" -> answer = model.add(answer, right)
                "-" -> answer = model.subtraction(answer, right)
            }

            i += 2
        }

        return answer
    }

    fun showResult() {

        // Verificar que sea una operaci�n real (no un n�mero aislado con signo)
        val hasRealOperator = result.value.contains("+") ||
            result.value.contains("×") ||
            result.value.contains("÷") ||
            result.value.contains("%") ||
            (result.value.length > 1 && result.value.substring(1).contains("-"))

        if (!hasRealOperator) {
            return
        }

        // Guardamos la operaci�n original
        val operation = result.value

        expression.value = operation

        // Limpiar error previo
        error.value = ""

        val answer = calculateResult()

        // Si hubo error, no guardar en historial. La vista mostrar� el mensaje.
        if (error.value.isNotEmpty()) {
            contentResult.clear()
            return
        }

        // Registramos la operaci�n en el historial (solo operaciones v�lidas)
        history.add("$operation = $answer")

        result.value = answer.toString()

        contentResult.clear()
        contentResult.add(result.value)
    }
}
