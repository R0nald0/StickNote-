package com.example.lembretes.presentation.ui.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.lembretes.R
import com.example.lembretes.presentation.ui.theme.LembretesTheme

@Composable
fun IntroStickNoteBoxExample(modifier: Modifier = Modifier, onHide: () -> Unit) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(.5f)
            .background(color = Color(0xFF7C99AC))
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally


    ) {


      val composition  by rememberLottieComposition(
          spec = LottieCompositionSpec.RawRes(R.raw.card_v1)
      )

        LottieAnimation(
            alignment = Alignment.Center,
            composition =  composition,
            iterations = LottieConstants.IterateForever,
            isPlaying = true,
            restartOnPlay = true,
            reverseOnRepeat = true

        )
        Spacer(modifier = modifier.height(20.dp))
        Text(
            modifier = Modifier.padding(18.dp),
            text = "Você pode deslizar à direita para atualizar um lembrete ou para esquerda para excluí-lo.",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = colorScheme.onPrimary,
                fontWeight = FontWeight.W600
            )
        )
        Spacer(modifier = modifier.height(20.dp))
        Button(
            modifier = modifier.align(alignment = Alignment.End),
            onClick = onHide
        ) {
            Text("Ok",
                modifier = Modifier
                    .padding(16.dp, 1.dp)
                    ,
                style =MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W600,
                    color = colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@Preview
@Composable
private fun IntroStickNoteBoxExamplePrev() {
    LembretesTheme {
        IntroStickNoteBoxExample(
            onHide = {}
        )
    }
}