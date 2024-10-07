package ru.t022.c2h5ohkt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.t022.c2h5ohkt.ui.theme.C2H5OHktTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            C2H5OHktTheme(darkTheme = true){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        t = "20",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
@Composable
fun input_field(text: String){
        Row() {
            Text(text = text)
            TextField(value = "0",onValueChange = {})
        }
}

@Composable
fun output_field(text: String){
    Row() {
        Text(text = text)
        Text("Значение")
    }
}

@Composable
fun Greeting(t: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = "Разбавить спирт водой при $t°С",
            modifier = modifier,
            fontSize = 28.sp
        )
        input_field("Объём исходного спирта, мл")
        input_field("Крепость исходного спирта, °\n [35°..95°]")
        input_field("Требуемая крепость спирта, °\n" +
                " [30°..90°]")
        Button(onClick = { /*TODO*/ }) {
            Text("Рассчитать", fontSize = 25.sp)

        }
        output_field("Добавить воды\n по таблице Г.И. Фертмана")
        output_field("Объём полученного раствора\n с учётом сжатия")
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    C2H5OHktTheme {
        Greeting("Android")
    }
}