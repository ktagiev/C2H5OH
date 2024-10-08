package ru.t022.c2h5ohkt

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.t022.c2h5ohkt.ui.theme.C2H5OHktTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    lateinit var pref: SharedPreferences
    lateinit var ed: SharedPreferences.Editor
    var v0: Int = 0   // объём исходного спирта
    var n0: Int = 0   // крепость исходного спирта
    var n1: Int = 0   // требуемая крепость спирта
    var x: Int = 0    // вычисленный объём воды
    var calc_v1: Int = 0   // вычисленный объём полученного раствора
    var v1: Int = 0   // требуемый объём полученного раствора
    lateinit var  scope: CoroutineScope
    lateinit var snackbarHostState: SnackbarHostState

    fun savePref() {
        ed.putInt("v0", v0)
        ed.putInt("n0", n0)
        ed.putInt("n1", n1)
        ed.putInt("v1", v1)
        ed.apply()
    }

    fun loadPref() {
        v0 = pref.getInt("v0", 1000)
        n0 = pref.getInt("n0", 70)
        n1 = pref.getInt("n1", 40)
        v1 = pref.getInt("v1", 1750)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        pref = this.getSharedPreferences("C2H5OH", Context.MODE_PRIVATE)
        ed = pref.edit()
        loadPref()
        setContent {
            snackbarHostState = remember { SnackbarHostState() }
            scope = rememberCoroutineScope()
            C2H5OHktTheme(darkTheme = true) {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
                    Greeting(
                        //modifier = Modifier.padding(innerPadding)
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 5.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun input_field(modifier: Modifier, text: String, message: MutableState<String>) {
        Box(modifier = modifier) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(3f), text = text)
                TextField(
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) ,
                    value = message.value,
                    onValueChange = { newText -> message.value = newText }
                )
            }
        }
    }

    @Composable
    fun output_field(text: String, message: MutableState<String>) {
        Box() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(modifier = Modifier.weight(3f), text = text)
                Text(
                    text = message.value,
                    modifier = Modifier.weight(1f),
                    color = Color.Green,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    @Composable
    fun Greeting(modifier: Modifier = Modifier) {
        val v0_message: MutableState<String> = remember { mutableStateOf(v0.toString()) }
        val n0_message: MutableState<String> = remember { mutableStateOf(n0.toString()) }
        val n1_message: MutableState<String> = remember { mutableStateOf(n1.toString()) }
        val x_message: MutableState<String> = remember { mutableStateOf("-") }
        val calc_v1_message: MutableState<String> = remember { mutableStateOf("-") }
        val v1_message: MutableState<String> = remember { mutableStateOf(v1.toString()) }

        Column(
            modifier = modifier
        ) {
            Text(
                color = Color(0xFF808000),
                text = "Разбавить спирт водой\n при температуре 20°С",
                modifier = modifier.align(Alignment.CenterHorizontally),
                fontSize = 28.sp,
                lineHeight = 30.sp
            )
            input_field(modifier, "Объём исходного спирта, мл", v0_message)
            input_field(modifier, "Крепость исходного спирта, °\n [35°..95°]", n0_message)
            input_field(modifier, "Требуемая крепость спирта, °\n [30°..90°]", n1_message)
            Button(modifier = modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    v0 = v0_message.value.toInt()
                    n0 = n0_message.value.toInt()
                    n1 = n1_message.value.toInt()
                    scope.launch {
                        snackbarHostState.showSnackbar("Расчёт")
                    }
                    // здесь расчёт прямой
                    x = Fertman.calcV1(v0, n0, n1)
                    calc_v1 = Fertman.V1

                    x_message.value = x.toString()
                    calc_v1_message.value = calc_v1.toString()

                    savePref()
                }) {
                Text("Рассчитать", fontSize = 25.sp)
            }
            output_field("Добавить воды\n по таблице Г.И. Фертмана", x_message)
//            Box(modifier = modifier) {
//                TextField(modifier = modifier
//                    .align(Alignment.Center)
//                    .width(100.dp)
//                    .height(50.dp),
//                    value = v1_message.value, onValueChange = { newText -> v1_message.value = newText})
            output_field("Объём полученного раствора\n с учётом сжатия", calc_v1_message)
//            }
//            input_field(modifier, "Требуемый объём\n раствора, мл", v1_message)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(modifier = Modifier.weight(2f), text = "Требуемый объём\n раствора, мл")
                TextField(
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    value = v1_message.value,
                    onValueChange = { newText -> v1_message.value = newText })
                IconButton(modifier = modifier
                    .weight(1f),
                    onClick = {
                        v1 = v1_message.value.toInt()
                        n0 = n0_message.value.toInt()
                        n1 = n1_message.value.toInt()

                        // здесь расчёт обратный
                        x = Fertman.calcV0(v1, n0, n1)
                        v0 = Fertman.V0
                        calc_v1 = Fertman.V1

                        x_message.value = x.toString()
                        calc_v1_message.value = calc_v1.toString()
                        v0_message.value = v0.toString()

                        savePref()
                    }) {
//                    Text("Рассчитать", fontSize = 25.sp)
                Icon(Icons.Default.ArrowForward, contentDescription = "Рассчитать",modifier = Modifier.size(50.dp),
                    tint = Color.Green)
                }
            }
        }
    }
}