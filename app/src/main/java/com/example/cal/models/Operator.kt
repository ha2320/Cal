package com.example.cal.models

enum class Operator() {
    PLUS, MINUS, DIVIDE, MULTIPLY, POWER, UNDEFINED;
    public fun getVal(): String = when(this){
        PLUS -> "+"
        MINUS -> "-"
        MULTIPLY -> "*"
        DIVIDE -> "/"
        POWER -> "^"
        else -> ""
    }
}