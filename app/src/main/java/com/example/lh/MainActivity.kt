package com.example.lh

import android.os.Bundle
import android.provider.UserDictionary.Words
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.example.lh.ui.theme.LhTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App(viewModel = GameViewModel())
        }
    }
}
@Composable
fun App(viewModel: GameViewModel) {
    val s = viewModel.state.collectAsState().value
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.LightGray), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Lykkehjulet", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Magenta, fontFamily = FontFamily.Serif)
        Text(text = "Liv " + s.life)
        Text(text = "Point " + s.point)
        val point = when (s.wheel) {
            0 -> "Bankerot"
            else -> (s.wheel*100).toString() + "$"
        }
        Text(text = "Position på lykkehjul " + point, color = Color.Black, fontWeight = FontWeight.Bold)
        Button(onClick = { viewModel.turnWheel()}, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text(text = "Spin", color= Color.White)


        }

        Text(text = "Ordet du skal gætte har kategorien \"${s.category}\"", fontStyle = FontStyle.Italic)
        Word(word = s.foundWord)

        GuessBox(onGuess = {viewModel.guessACharacter(it)})

        if(!s.foundWord.contains("_")) {
            Text(text = "Du har vundet", color = Color.Yellow, fontWeight = FontWeight.Bold)
            Button(onClick = { viewModel.createNewGame() }) {
                Text(text = "Spil Igen")
            }
        }
        if(s.life <= 0) {
            Text(text = "Du har tabt", color = Color.Red, fontWeight = FontWeight.Bold)
            Button(onClick = { viewModel.createNewGame() }) {
                Text(text = "Spil Igen")
            }
        }
    }

}
@Composable
fun GuessBox(onGuess: (String) -> Unit) {
    var value by remember {
        mutableStateOf("")
    }
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        TextField(value = value, onValueChange = {
            if (it.length < 2)
                value = it.toUpperCase()
        }, label = {
            Text(text = "Indtast dit gæt")
        })

        Button(onClick = {
            if (value.length == 1)
                onGuess(value)
        },colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)) {
            Text("Gæt", color = Color.White, fontWeight = FontWeight.Bold)

        }

    }
}


@Composable
fun Word(word : String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        for(w in word) {
            Text(text = w.toString(), modifier = Modifier.padding(horizontal = 2.dp), fontSize = 15.sp)
        }
    }
}