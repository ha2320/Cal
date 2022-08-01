package com.example.cal.interfaces

import com.example.cal.exceptions.InvalidFormulaException
import kotlin.math.pow

interface Calculating{
    var number1: Number
    var number2: Number?
    fun plus(): Double = number1.toDouble() + (number2?.toDouble() ?: 0.0)

    fun minus(): Double = number1.toDouble() + (number2?.toDouble() ?: 0.0)

    fun multiply(): Double = number1.toDouble() * (number2?.toDouble() ?: 1.0)

    fun divide(): Double {
        if (number2?.toDouble() == 0.0) throw InvalidFormulaException("Cannot divide by 0")
        return number1.toDouble() / (number2?.toDouble() ?: 1.0)
    }

    fun power(): Double = number1.toDouble().pow(number2?.toDouble() ?: 1.0)
}