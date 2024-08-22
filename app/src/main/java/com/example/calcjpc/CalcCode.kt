package com.example.calcjpc

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upeu.calcupeujpc.ui.theme.CalcUPeUJPCTheme
import com.example.calcjpc.ui.theme.Purple200
import com.example.calcjpc.ui.theme.textColor
import kotlin.math.pow
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalcUPeUJPCTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalcUPeU()
                }
            }
        }
    }
}

fun isNumeric(toCheck: String): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return toCheck.matches(regex)
}

@Composable
fun ButtonX(
    modifier: Modifier,
    valuex: String,
    onValueChange: (String) -> Unit,
    onIsNewOpChange: (Boolean) -> Unit,
    textState: String,
    isNewOp: Boolean,
    onOpChange: (String) -> Unit,
    onOldValueChange: (String) -> Unit,
    oldTextState: String,
    op: String
) {
    Column(modifier = modifier.wrapContentSize(Alignment.Center)) {
        Box(
            modifier = modifier
                .weight(1f)
                .background(Color(0xFF404345))
                .border(width = .5.dp, Color(0xFF2C2F32))
                .clickable(
                    enabled = true,
                    onClick = {
                        if (isNumeric(valuex)) {
                            var valor = textState
                            if (isNewOp) {
                                valor = ""
                                onValueChange.invoke(valor)
                            }
                            onIsNewOpChange.invoke(false)
                            valor += valuex
                            onValueChange.invoke(valor)
                        }
                        when (valuex) {
                            "+", "-", "*", "/", "%", "^" -> {
                                onOpChange.invoke(valuex)
                                onOldValueChange.invoke(textState)
                                onIsNewOpChange.invoke(true)
                            }
                            "AC" -> {
                                onValueChange.invoke("0")
                                onIsNewOpChange.invoke(true)
                            }
                            "." -> {
                                var dot = textState
                                if (isNewOp) {
                                    dot = ""
                                    onValueChange.invoke(dot)
                                }
                                onIsNewOpChange.invoke(false)
                                if (!dot.contains(".")) {
                                    dot += "."
                                    onValueChange.invoke(dot)
                                }
                            }
                            "=" -> {
                                if (oldTextState.isNotEmpty()) {
                                    var finalNumber = 0.0
                                    when (op) {
                                        "*" -> {
                                            finalNumber = oldTextState.toDouble() * textState.toDouble()
                                        }
                                        "/" -> {
                                            finalNumber = oldTextState.toDouble() / textState.toDouble()
                                        }
                                        "+" -> {
                                            finalNumber = oldTextState.toDouble() + textState.toDouble()
                                        }
                                        "-" -> {
                                            finalNumber = oldTextState.toDouble() - textState.toDouble()
                                        }
                                        "^" -> {
                                            finalNumber = oldTextState.toDouble().pow(textState.toDouble())
                                        }
                                    }
                                    onValueChange.invoke(finalNumber.toString())
                                    onIsNewOpChange.invoke(true)
                                }
                            }
                            "√" -> {
                                val number = textState.toDouble()
                                val result = sqrt(number)
                                onValueChange.invoke(result.toString())
                                onIsNewOpChange.invoke(true)
                            }
                            "1/x" -> {
                                val number = textState.toDouble()
                                if (number != 0.0) {
                                    val result = 1 / number
                                    onValueChange.invoke(result.toString())
                                    onIsNewOpChange.invoke(true)
                                }
                            }
                            "π" -> {
                                onValueChange.invoke(Math.PI.toString())
                                onIsNewOpChange.invoke(true)
                            }
                        }
                    }
                )
        ) {
            Text(
                text = valuex,
                style = TextStyle(
                    fontSize = 24.sp,
                    textAlign = TextAlign.End,
                    color = textColor,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        }
    }
}

@Composable
fun CalculatorTextField(
    textState: String,
    modifier: Modifier,
    onValueChange: (String) -> Unit,
) {
    Column(
        modifier = modifier
            .background(Purple200)
            .wrapContentSize(Alignment.BottomEnd)
            .fillMaxSize()
    ) {
        TextField(
            value = textState,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .wrapContentSize(Alignment.BottomEnd)
                .fillMaxSize(),
            textStyle = TextStyle(fontSize = 36.sp, textAlign = TextAlign.End, color = textColor),
            maxLines = 2,
            readOnly = true
        )
    }
}

@Composable
fun CalculatorFirstRow(
    textState: String,
    isNewOp: Boolean,
    onValueChange: (String) -> Unit,
    onIsNewOpChange: (Boolean) -> Unit,
    onOpChange: (String) -> Unit,
    onOldValueChange: (String) -> Unit,
    modifier: Modifier,
    op: String,
    oldTextState: String,
    data: List<String>
) {
    Row(modifier = modifier.fillMaxSize()) {
        data.forEach {
            ButtonX(
                modifier = modifier,
                valuex = it,
                onValueChange = onValueChange,
                onIsNewOpChange = onIsNewOpChange,
                textState = textState,
                isNewOp = isNewOp,
                onOpChange = onOpChange,
                onOldValueChange = onOldValueChange,
                op = op,
                oldTextState = oldTextState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalcUPeU() {
    CalcUPeUJPCTheme {
        Column {
            var op by remember { mutableStateOf("") }
            var isNewOp by remember { mutableStateOf(true) }
            var oldTextState: String by remember { mutableStateOf("") }
            var textState: String by remember { mutableStateOf("0") }

            CalculatorTextField(
                textState = textState,
                modifier = Modifier.height(100.dp),
                onValueChange = { textState = it }
            )

            Column(modifier = Modifier.fillMaxSize()) {
                val listA = listOf("AC", "π", "√", "/")
                val listB = listOf("7", "8", "9", "*")
                val listC = listOf("4", "5", "6", "-")
                val listD = listOf("1", "2", "3", "+")
                val listE = listOf("0", ".", "^", "=")
                val listF = listOf("1/x")

                val listaCompleta = listOf(listA, listB, listC, listD, listE, listF)
                listaCompleta.forEach {
                    CalculatorFirstRow(
                        isNewOp = isNewOp,
                        textState = textState,
                        onValueChange = { textState = it },
                        onIsNewOpChange = { isNewOp = it },
                        onOpChange = { op = it },
                        onOldValueChange = { oldTextState = it },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        op = op,
                        oldTextState = oldTextState,
                        data = it
                    )
                }
            }
        }
    }
}
