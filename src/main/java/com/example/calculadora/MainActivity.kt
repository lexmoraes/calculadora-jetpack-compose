package com.example.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.objecthunter.exp4j.ExpressionBuilder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculadoraApp()
        }
    }
}

@Composable
fun CalculadoraApp() {
    var expression by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("0") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = expression,
            fontSize = 28.sp,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
        Text(
            text = result,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.height(20.dp))

        val buttons = listOf(
            listOf("C", "+/-", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("0", ".", "⌫", "=")
        )

        buttons.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { label ->
                    ButtonCalc(label) {
                        when (label) {
                            "C" -> {
                                expression = ""
                                result = "0"
                            }
                            "=" -> try {
                                result = evalExpression(expression)
                            } catch (e: Exception) {
                                result = "Erro"
                            }
                            "⌫" -> {
                                expression = if (expression.isNotEmpty()) expression.dropLast(1) else ""
                            }
                            "÷" -> expression += "/"
                            "×" -> expression += "*"
                            else -> expression += label
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ButtonCalc(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = if (label in listOf("+", "-", "×", "÷", "=")) Color.Blue else Color.LightGray),
        modifier= Modifier.size(80.dp).padding(4.dp)
    ) {
        Text(label, fontSize = 24.sp, color = Color.White)
    }
}

fun evalExpression(expression: String): String {
    return try {
        val result = ExpressionBuilder(expression).build().evaluate()
        result.toString()
    } catch (e: Exception) {
        "Erro"
    }
}
