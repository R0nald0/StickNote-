package com.example.lembretes.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.decode.ImageSource
import com.example.lembretes.R
import com.example.lembretes.presentation.MainActivity
import com.example.lembretes.presentation.ui.ui.theme.LembretesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DescriptionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LembretesTheme {
                    Box (
                     modifier = Modifier.fillMaxSize()
                    ){
                        Image(modifier = Modifier.fillMaxSize(),
                            painter = painterResource(R.drawable.postit4), contentDescription = "",
                            contentScale = ContentScale.FillBounds
                            )
                        DescriptionPage(
                            name = "Android",
                            activity = this@DescriptionActivity,
                            modifier = Modifier
                        )
                    }
            }
        }
    }
}

@Composable
fun DescriptionPage(name: String,activity: Activity ,modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Spacer(modifier = modifier.height(50.dp).weight(0.5f))
        Text(
             modifier = Modifier.padding(20.dp)
                 .weight(0.4f),
             text = "Olá $name você tem um Lembrete agendado para dia",
             style = MaterialTheme.typography.titleMedium.copy(
                 fontSize = 28.sp,
                 color = MaterialTheme.colorScheme.primary,
                 letterSpacing = 2.sp
             ),
             textAlign = TextAlign.Center
            )
        Text(
            text = "23/05/2025 as 10:40",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            modifier = Modifier.padding(20.dp)
                .weight(0.4f),
            text = " Nome Lembrete,agendado",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            ),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = modifier
                .fillMaxWidth(0.7f)
                .weight(0.5f)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    val intent = Intent(activity.applicationContext,MainActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
                }
            ) {
                Text("Confirmar")
            }
        }
        Spacer(modifier = modifier.weight(0.1f))
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionPagePreview() {
    LembretesTheme {
        DescriptionPage("Android", activity = Activity())
    }
}