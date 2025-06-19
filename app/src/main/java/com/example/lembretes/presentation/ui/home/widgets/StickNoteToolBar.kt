package com.example.lembretes.presentation.ui.home.widgets

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.lembretes.R
import com.example.lembretes.domain.model.User
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StickNoteToolBar(
    modifier: Modifier = Modifier,
    user: User,
    numberOfStickNotes: Int = 0,
    onOpenProfile: () -> Unit,
    openSearch: () -> Unit,
) {
    val scroolBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Log.i("INFO_", "StickNoteToolBar:${scroolBehavior.state.collapsedFraction} ")

    var isClosed by rememberSaveable { mutableStateOf(false) }

    val heightAnimate by animateDpAsState(targetValue = if (isClosed) 77.dp else 170.dp ,)
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .background(
                color = colorScheme.primary,
                shape = RoundedCornerShape(bottomEnd = 30.dp, bottomStart = 30.dp)
            ).height(heightAnimate)
            .fillMaxWidth()
            .padding(16.dp)
            .clickable{
              isClosed = !isClosed
            }

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,

        ){

                    Text(
                        modifier =  modifier.weight(0.5f),
                        text = "Lembrete", style = MaterialTheme
                            .typography.titleLarge
                            .copy(color = colorScheme.onPrimaryContainer),
                    )



            IconButton(onClick = openSearch) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "User avatar",
                    tint = colorScheme.onPrimaryContainer
                    )
            }
            AsyncImage(
                modifier = Modifier
                    .size(width = 42.dp, height = 42.dp)
                    .clip(CircleShape)
                    .background(color = Color.Gray)
                    .border(
                        border = BorderStroke(
                            color = colorScheme.onPrimaryContainer, width = 2.dp
                        ), shape = CircleShape
                    )
                    .clickable { onOpenProfile() },
                contentScale = ContentScale.Crop,
                alpha = 0.7f,
                contentDescription = "imagem usuário",
                error = painterResource(R.drawable.ic_person_24),
                model = user.photoProfile.toUri()
            )
        }
        Spacer(modifier.height(15.dp))
        Text(
            text = "Olá ${user.name.capitalize(Locale.current)},${daySection()}!!",
            style = MaterialTheme.typography.titleLarge
                .copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimaryContainer
                )
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val agendadosText =
                if (numberOfStickNotes <= 1) "lembrete agendado" else "lembretes agendados"
            Text(
                text = "Você tem",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = colorScheme.onPrimaryContainer ),
                fontSize = 13.sp,
            )
            Text(
                text = " $numberOfStickNotes ",
                style = MaterialTheme.typography.bodyLarge.
                copy(
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onPrimaryContainer
                    )
            )
            Text(
                text = agendadosText,
                style = MaterialTheme.typography.bodyLarge.
                copy(
                    fontSize = 13.sp,
                    color = colorScheme.onPrimaryContainer
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun SticnkNoteToolBarPreview() {
    LembretesTheme {
        StickNoteToolBar(
            onOpenProfile = {  },
            user = User(1, "Test", ""),
            openSearch = {}
        )
    }
}

private fun daySection(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    return when (hour) {
        in 0..5 -> "Boa noite"
        in 6..11 -> "Bom Dia"
        in 12..17 -> "Boa Tarde"
        in 18..24 -> "Boa noite"
        else -> ""
    }
}

/*
@Composable
fun AnimatedSwipeColumn() {
    val minHeight = 100.dp
    val maxHeight = 400.dp
    val heightPxRange = with(LocalDensity.current) { minHeight.toPx()..maxHeight.toPx() }

    val animatedHeight = remember { Animatable(heightPxRange.endInclusive) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(with(LocalDensity.current) { animatedHeight.value.toDp() })
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    coroutineScope.launch {
                        val newHeight = (animatedHeight.value - dragAmount).coerceIn(heightPxRange)
                        animatedHeight.snapTo(newHeight)
                    }
                }
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Arraste para cima ou para baixo", fontSize = 18.sp)
            // Adicione mais conteúdo conforme necessário
        }
    }
}*/
