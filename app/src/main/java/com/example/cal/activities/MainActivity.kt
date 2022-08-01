package com.example.cal.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cal.R
import com.example.cal.databinding.ActivityMainBinding
import com.example.cal.models.Calculation
import com.example.cal.models.Operator

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private var resultStr: String = "" // string value of the calculated result
    private var numb1Str: String = ""
    private var numb2Str: String = ""
    private var formulaStr: String = "" // represent the math, made up from $numb1Str$currentOperator$numb2Str
    private var editingFirstNumber = true
    private var editingSecondNumber = false

    // invalid position other than -1, because it will be compared with the length of a string
    private var operatorPosition = -5 // necessary for delete-1-character button
    private var currentOperator: Operator = Operator.UNDEFINED
    private lateinit var digitButtonClickListener: View.OnClickListener
    private lateinit var operatorButtonClickListener: View.OnClickListener
    private var calculation: Calculation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Declare OnClick Listener for Digit Buttons
        digitButtonClickListener = View.OnClickListener {
            digitButton ->
            if(resultStr.isNotBlank()) {
                onStartNewCalculation()
                resultStr = ""
            }
            val numberDigit: String = getNumberStringValueFromDigitButtonID(digitButton!!.id)
            if (editingFirstNumber) numb1Str =
                if (numb1Str != "" && numb1Str.first().toString() == "0") numberDigit
                else numb1Str + numberDigit
            else numb2Str =
                if (numb2Str != "" && numb2Str.first().toString() == "0") numberDigit
                else numb2Str + numberDigit
            updateFormula()
        }
        // Declare OnClick Listener for Operator Buttons
        operatorButtonClickListener = View.OnClickListener {
            operatorButton ->
            val newOp: Operator = getOpStringFromOpButton(operatorButton as Button)
            if (numb1Str != ""){
                if(resultStr.isNotBlank()) {
                    onStartNewCalculation()
                    numb1Str = resultStr
                    resultStr = ""
                }
                editingFirstNumber = false
                editingSecondNumber = true
                currentOperator = newOp
                operatorPosition = numb1Str.length
                updateFormula()
            }
        }

        // set onClickListeners
        handleClicks()
    }
    private fun handleClicks() {
        // handle clicks for digit buttons
        binding!!.numb1.setOnClickListener(digitButtonClickListener)
        binding!!.numb2.setOnClickListener(digitButtonClickListener)
        binding!!.numb3.setOnClickListener(digitButtonClickListener)
        binding!!.numb4.setOnClickListener(digitButtonClickListener)
        binding!!.numb5.setOnClickListener(digitButtonClickListener)
        binding!!.numb6.setOnClickListener(digitButtonClickListener)
        binding!!.numb7.setOnClickListener(digitButtonClickListener)
        binding!!.numb8.setOnClickListener(digitButtonClickListener)
        binding!!.numb9.setOnClickListener(digitButtonClickListener)
        binding!!.numb0.setOnClickListener(digitButtonClickListener)

        // handle clicks for operator buttons
        binding!!.opPlus.setOnClickListener(operatorButtonClickListener)
        binding!!.opMinus.setOnClickListener(operatorButtonClickListener)
        binding!!.opMultiply.setOnClickListener(operatorButtonClickListener)
        binding!!.opDivide.setOnClickListener(operatorButtonClickListener)
        binding!!.opPower.setOnClickListener(operatorButtonClickListener)

        // handle clicks for action buttons
        handleDeleteButtonClick()
        binding!!.clearAll.setOnClickListener{clearAll()}
        binding!!.showResult.setOnClickListener { showResult() }
    }


    private fun showResult() {
        calculation =
            if (numb2Str.isBlank()) Calculation(numb1Str.ifBlank { "0" }.toDouble())
            else Calculation(number1 = numb1Str.toDouble(), number2 = numb2Str.toDouble())
        try {
            resultStr = doMath()
        } catch (e: Exception){
            binding?.errorTextOutput?.text = e.message
        }
        binding?.resultOutput?.text = resultStr.ifBlank {"0"}
    }

    private fun doMath() = when (currentOperator) {
        Operator.PLUS -> calculation?.plus().toString()
        Operator.MINUS -> calculation?.minus().toString()
        Operator.MULTIPLY -> calculation?.multiply().toString()
        Operator.DIVIDE -> calculation?.divide().toString()
        Operator.POWER -> calculation?.power().toString()
        else -> numb1Str.ifBlank { "0" }
    }


    private fun clearAll() {
        onStartNewCalculation()
        updateFormula()
        binding!!.resultOutput.text = "0"
    }

    private fun onStartNewCalculation() {
        numb1Str = ""
        numb2Str = ""
        currentOperator = Operator.UNDEFINED
        editingFirstNumber = true
        editingSecondNumber = false
    }

    private fun handleDeleteButtonClick() {
        binding?.characterDeleter?.setOnClickListener {
            when {
                formulaStr.length-1 == operatorPosition -> {
                    editingFirstNumber = false
                    editingSecondNumber = false
                }
                formulaStr.length-1 < operatorPosition -> {
                    editingFirstNumber = true
                    editingSecondNumber = false
                }
                formulaStr.length-1 > operatorPosition && operatorPosition > 0 -> {
                    editingFirstNumber = false
                    editingSecondNumber = true
                }
            }
            if (editingFirstNumber && numb1Str!="") numb1Str = numb1Str.dropLast(1)
            else if (editingSecondNumber) {
                if( numb2Str!= "") numb2Str = numb2Str.dropLast(1)
                else if( operatorPosition == -5 ) numb1Str = numb1Str.dropLast(1)
            }
            else {
                currentOperator = Operator.UNDEFINED
                editingFirstNumber = true
                operatorPosition = -5
            }
            updateFormula()
        }
    }

    private fun getOpStringFromOpButton(operatorButton: Button): Operator
        = when (operatorButton.id) {
            binding!!.opPlus.id -> Operator.PLUS
            binding!!.opMinus.id -> Operator.MINUS
            binding!!.opMultiply.id -> Operator.MULTIPLY
            binding!!.opDivide.id -> Operator.DIVIDE
            binding!!.opPower.id -> Operator.POWER
            else -> Operator.UNDEFINED
    }

    private fun getNumberStringValueFromDigitButtonID(numberButtonID: Int): String
        =  when(numberButtonID){
            binding!!.numb1.id -> resources.getString(R.string._1)
            binding!!.numb2.id -> resources.getString(R.string._2)
            binding!!.numb3.id -> resources.getString(R.string._3)
            binding!!.numb4.id -> resources.getString(R.string._4)
            binding!!.numb5.id -> resources.getString(R.string._5)
            binding!!.numb6.id -> resources.getString(R.string._6)
            binding!!.numb7.id -> resources.getString(R.string._7)
            binding!!.numb8.id -> resources.getString(R.string._8)
            binding!!.numb9.id -> resources.getString(R.string._9)
            binding!!.numb0.id -> resources.getString(R.string._0)
            else -> {""}
        }

    private fun updateFormula() {
        formulaStr =
            if(currentOperator == Operator.UNDEFINED) numb1Str
            else "$numb1Str${currentOperator.getVal()}$numb2Str"
        binding!!.formulaOutput.text = formulaStr.ifBlank { "0.0" }
    }
}

