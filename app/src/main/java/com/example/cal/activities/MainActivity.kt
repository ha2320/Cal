package com.example.cal.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.cal.R
import com.example.cal.databinding.ActivityMainBinding
import com.example.cal.models.Calculation
import com.example.cal.models.Operator

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var resultStr: String = ""
    private var numb1Str: String = ""
    private var numb2Str: String = ""
    private var formulaStr: String = ""
    private var editingFirstNumber = true
    private var editingSecondNumber = false
    private var operatorPosition = -1
    private var currentOperator: Operator = Operator.UNDEFINED
    private lateinit var calculation: Calculation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onResume() {
        super.onResume()
        Log.d("check binding",binding.toString())
        handleClicks()
    }
    private fun handleClicks() {
        // handle clicks for digit buttons
        handleClicksForDigitButton(binding.numb1)
        handleClicksForDigitButton(binding.numb2)
        handleClicksForDigitButton(binding.numb3)
        handleClicksForDigitButton(binding.numb4)
        handleClicksForDigitButton(binding.numb5)
        handleClicksForDigitButton(binding.numb6)
        handleClicksForDigitButton(binding.numb7)
        handleClicksForDigitButton(binding.numb8)
        handleClicksForDigitButton(binding.numb9)
        handleClicksForDigitButton(binding.numb0)

        // handle clicks for operator buttons
        handleClicksForOperatorButton(binding.opPlus)
        handleClicksForOperatorButton(binding.opMinus)
        handleClicksForOperatorButton(binding.opMultiply)
        handleClicksForOperatorButton(binding.opDivide)
        handleClicksForOperatorButton(binding.opPower)

        // handle clicks for action buttons

        handleDeleteButtonClick()
        handleClearAllButtonClick()
        handleShowResultClick()
    }

    private fun handleShowResultClick() {
        this.binding.resultOutput.setOnClickListener {
            try {
                calculation =
                    Calculation(number1 = numb1Str.toDouble(), number2 = numb2Str.toDouble())
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "Wrong format number input, please take a look",
                    Toast.LENGTH_LONG
                ).show()
            }
            resultStr = when (currentOperator) {
                Operator.PLUS -> calculation.plus().toString()
                Operator.MINUS -> calculation.minus().toString()
                Operator.MULTIPLY -> calculation.multiply().toString()
                Operator.DIVIDE -> calculation.divide().toString()
                Operator.POWER -> calculation.power().toString()
                else -> resultStr
            }
            binding.resultOutput.text = resultStr
        }
    }

    private fun handleClearAllButtonClick() {
        this.binding.clearAll.setOnClickListener{
            numb1Str = ""
            numb2Str = ""
            resultStr = ""
            currentOperator = Operator.UNDEFINED
            editingFirstNumber = true
            editingSecondNumber = false
            updateFormula()
            binding.resultOutput.text = "0"
        }
    }

    private fun handleDeleteButtonClick() {
        Log.d("check delete", this.binding.characterDeleter.toString())
        this.binding.characterDeleter.setOnClickListener {
            when {
                formulaStr.length == operatorPosition -> {
                    editingFirstNumber = false
                    editingSecondNumber = false
                }
                formulaStr.length < operatorPosition -> {
                    editingFirstNumber = true
                    editingSecondNumber = false
                }
                formulaStr.length > operatorPosition -> {
                    editingFirstNumber = false
                    editingSecondNumber = true
                }
            }
            if (editingFirstNumber && numb1Str.isNotEmpty()) numb1Str.dropLast(1)
            else if (editingSecondNumber && numb2Str.isNotEmpty()) numb2Str.dropLast(1)
            else {
                currentOperator = Operator.UNDEFINED
                editingFirstNumber = true
            }
            updateFormula()
        }
    }

    private fun handleClicksForOperatorButton(operatorButton: Button) {
        val newOp: Operator = when(operatorButton.id){
            binding.opPlus.id -> Operator.PLUS
            binding.opMinus.id -> Operator.MINUS
            binding.opMultiply.id -> Operator.MULTIPLY
            binding.opDivide.id -> Operator.DIVIDE
            binding.opPower.id -> Operator.POWER
            else -> Operator.UNDEFINED
        }
        operatorButton.setOnClickListener{
            if (numb1Str != ""){
                editingFirstNumber = false
                editingSecondNumber = true
                currentOperator = newOp
                operatorPosition = numb1Str.length
                updateFormula()
            }
            Toast.makeText(this,operatorButton.text.toString()+" has been clicked",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNumberStringValueFromDigitButtonID(numberButtonID: Int): String =  when(numberButtonID){
        binding.numb1.id -> resources.getString(R.string._1)
        binding.numb2.id -> resources.getString(R.string._2)
        binding.numb3.id -> resources.getString(R.string._3)
        binding.numb4.id -> resources.getString(R.string._4)
        binding.numb5.id -> resources.getString(R.string._5)
        binding.numb6.id -> resources.getString(R.string._6)
        binding.numb7.id -> resources.getString(R.string._7)
        binding.numb8.id -> resources.getString(R.string._8)
        binding.numb9.id -> resources.getString(R.string._9)
        binding.numb0.id -> resources.getString(R.string._0)
        else -> {""}
    }
    private fun handleClicksForDigitButton(numberButton: Button) {
        numberButton.setOnClickListener{
            val numberDigit: String = getNumberStringValueFromDigitButtonID(numberButton.id)
            if (editingFirstNumber) numb1Str += numberDigit
            else numb2Str += numberDigit
            updateFormula()
        }
    }

    private fun updateFormula() {
        formulaStr =
            if(currentOperator == Operator.UNDEFINED)
                numb1Str
            else numb1Str + currentOperator + numb2Str
        binding.currentFormula.text = if(formulaStr=="") "0" else formulaStr
    }
}

