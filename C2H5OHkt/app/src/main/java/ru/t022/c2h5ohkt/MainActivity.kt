package ru.t022.c2h5ohkt

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material3.TextField
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    //var pref : SharedPreferences =  this.getSharedPreferences("C2H5OH", Context.MODE_PRIVATE)
    lateinit var pref : SharedPreferences
    var v0 : Int = 0   // объём исходного спирта
    var n0 : Int = 0   // крепость исходного спирта
    var n1 : Int = 0   // требуемая крепость спирта
    lateinit var ed : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        pref = this.getSharedPreferences("C2H5OH", Context.MODE_PRIVATE)
        ed = pref.edit()
        v0 = pref.getInt("v0",1000)
        n0 = pref.getInt("n0",70)
        n1 = pref.getInt("n1",40)
        setContent {
//        ed  =  pref.edit()
            C2H5OHktTheme(darkTheme = true){
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

@Composable
fun input_field(text: String,v: Int){
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = text)
            TextField(value = v.toString(),onValueChange = {})
        }
}

@Composable
fun output_field(text: String){
    Row() {
        Text(text = text)
        Text("0",color = Color.Green)
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier //, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Разбавить спирт водой при 20°С",
            modifier = modifier,
            fontSize = 28.sp
        )
        input_field("Объём исходного спирта, мл",v0)
        input_field("Крепость исходного спирта, °\n [35°..95°]",n0)
        input_field("Требуемая крепость спирта, °\n" +
                " [30°..90°]",n1)
        Button(onClick = {
            ed.putInt("v0",2)
            ed.putInt("n0",n0)
            ed.putInt("n1",n1)
            ed.commit()
        }) {
            Text("Рассчитать", fontSize = 25.sp)

        }
        output_field("Добавить воды\n по таблице Г.И. Фертмана")
        output_field("Объём полученного раствора\n с учётом сжатия")
    }
}
}