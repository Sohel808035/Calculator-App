package com.example.mycalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var previousCalculationTextView: TextView
    private lateinit var resultTextView: TextView

    // Calculator state variables
    private var isNewOperation = true
    private var currentNumber = ""
    private var pendingOperation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize TextViews
        previousCalculationTextView = findViewById(R.id.previousCalculationTextView)
        resultTextView = findViewById(R.id.resultTextView)

        // Initialize Buttons
        val button0: Button = findViewById(R.id.btn0)
        val button1: Button = findViewById(R.id.btn1)
        val button2: Button = findViewById(R.id.btn2)
        val button3: Button = findViewById(R.id.btn3)
        val button4: Button = findViewById(R.id.btn4)
        val button5: Button = findViewById(R.id.btn5)
        val button6: Button = findViewById(R.id.btn6)
        val button7: Button = findViewById(R.id.btn7)
        val button8: Button = findViewById(R.id.btn8)
        val button9: Button = findViewById(R.id.btn9)

        val buttonPlus: Button = findViewById(R.id.btnPlus)
        val buttonMinus: Button = findViewById(R.id.btnMinus)
        val buttonMultiply: Button = findViewById(R.id.btnMultiply)
        val buttonDivide: Button = findViewById(R.id.btnDivide)
        val buttonPercent: Button = findViewById(R.id.btnPercent)

        val buttonDot: Button = findViewById(R.id.btnDot)
        val buttonEqual: Button = findViewById(R.id.btnEqual)
        val buttonClear: Button = findViewById(R.id.btnClear)
        val buttonBackspace: Button = findViewById(R.id.btnBackspace)

        // --- All errors are fixed below ---

        // Number button click listeners
        button0.setOnClickListener { appendNumber("0") }
        button1.setOnClickListener { appendNumber("1") }
        button2.setOnClickListener { appendNumber("2") }
        button3.setOnClickListener { appendNumber("3") }
        button4.setOnClickListener { appendNumber("4") }
        button5.setOnClickListener { appendNumber("5") }
        button6.setOnClickListener { appendNumber("6") }
        button7.setOnClickListener { appendNumber("7") }
        button8.setOnClickListener { appendNumber("8") }
        button9.setOnClickListener { appendNumber("9") }
        buttonDot.setOnClickListener { appendNumber(".") }

        // Operator button click listeners
        buttonPlus.setOnClickListener { appendOperator("+") }
        buttonMinus.setOnClickListener { appendOperator("-") }
        buttonMultiply.setOnClickListener { appendOperator("*") }
        buttonDivide.setOnClickListener { appendOperator("/") }
        buttonPercent.setOnClickListener { appendOperator("%") } // Note: Percent logic can be complex

        // Action button click listeners
        buttonEqual.setOnClickListener { calculateResult() }
        buttonClear.setOnClickListener { clear() }
        buttonBackspace.setOnClickListener { backspace() }
    }

    // --- Calculator Logic Functions ---

    private fun appendNumber(number: String) {
        if (isNewOperation) {
            resultTextView.text = number
            isNewOperation = false
        }
        // Prevent adding multiple dots
        if (number == "." && resultTextView.text.contains(".")) {
            return
        }
        resultTextView.append(number)
    }

    private fun appendOperator(op: String) {
        if (resultTextView.text.isNotEmpty()) {
            val number = resultTextView.text.toString()
            if (currentNumber.isNotEmpty() && pendingOperation.isNotEmpty()) {
                calculateResult() // Calculate previous operation first
            }
            currentNumber = resultTextView.text.toString()
            pendingOperation = op
            previousCalculationTextView.text = "$currentNumber $pendingOperation"
            isNewOperation = true
        }
    }

    private fun calculateResult() {
        if (currentNumber.isNotEmpty() && pendingOperation.isNotEmpty() && resultTextView.text.isNotEmpty()) {
            val newNumber = resultTextView.text.toString()
            val num1 = currentNumber.toDoubleOrNull()
            val num2 = newNumber.toDoubleOrNull()

            if (num1 != null && num2 != null) {
                val result = when (pendingOperation) {
                    "+" -> num1 + num2
                    "-" -> num1 - num2
                    "*" -> num1 * num2
                    "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN // Handle division by zero
                    "%" -> num1 % num2
                    else -> 0.0
                }

                // Format result to avoid ".0" for whole numbers
                if (result.isNaN()){
                    resultTextView.text = "Error"
                } else if (result == result.roundToInt().toDouble()) {
                    resultTextView.text = result.toInt().toString()
                } else {
                    resultTextView.text = result.toString()
                }

                previousCalculationTextView.text = "$currentNumber $pendingOperation $newNumber ="
                currentNumber = ""
                pendingOperation = ""
                isNewOperation = true
            }
        }
    }

    private fun clear() {
        resultTextView.text = "0"
        previousCalculationTextView.text = ""
        currentNumber = ""
        pendingOperation = ""
        isNewOperation = true
    }

    private fun backspace() {
        val currentText = resultTextView.text.toString()
        if (currentText.isNotEmpty() && !isNewOperation) {
            resultTextView.text = currentText.dropLast(1)
            if (resultTextView.text.isEmpty()) {
                resultTextView.text = "0"
                isNewOperation = true
            }
        }
    }
}
