package com.example.lembretes.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lembretes.R
import com.example.lembretes.core.notification.cancelNotification
import com.example.lembretes.domain.model.StickyNoteDomain
import com.example.lembretes.presentation.MainActivity
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.StickNoteViewmodel
import com.example.lembretes.presentation.viewmodel.UserViewModel
import com.example.lembretes.utils.dateFormatToString
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class DescriptionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            LembretesTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val extra = intent.getStringExtra("st")

                    if (extra == null) return@Box
                    val stickNote =
                        Gson().fromJson<StickyNoteDomain>(extra, StickyNoteDomain::class.java)

                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(R.drawable.postit4), contentDescription = "",
                        contentScale = ContentScale.FillBounds
                    )
                   DescriptionRoute(
                        activity = this@DescriptionActivity,
                         modifier = Modifier,
                         stickyNoteDomain = stickNote
                       )
                }
            }
        }
    }
}

@Composable
fun DescriptionRoute(
    activity: Activity,
    stickyNoteDomain: StickyNoteDomain,
    modifier: Modifier = Modifier
) {

    DescriptionPage(
        modifier = modifier,
        stickyNoteDomain = stickyNoteDomain
    ){
        val intent = Intent(activity.applicationContext, MainActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

}

@Composable
fun DescriptionPage(
    stickyNoteDomain: StickyNoteDomain,
    modifier: Modifier = Modifier,
    onFinish :()-> Unit
) {
    val textColor = Color(0xFF133665)
    val context = LocalContext.current
    val userViewModel = hiltViewModel<UserViewModel>()
    val stickNoteViewModel = hiltViewModel<StickNoteViewmodel>()

    val (_, name) = userViewModel.user.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        userViewModel.findFirstUser()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(
            modifier = modifier
                .height(80.dp)
                .weight(0.1f)
        )
        Text(
            modifier = Modifier
                .padding(20.dp),
            text = "Olá $name,você tem um Lembrete agendado para o dia",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 25.sp,
                color = textColor,
                letterSpacing = 2.sp
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = stickyNoteDomain.dateTime.dateFormatToString(),
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 28.sp,
                color = textColor
            )
        )
        Text(
            modifier = Modifier.padding(20.dp),
            text = stickyNoteDomain.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 28.sp,
                color = textColor,
                letterSpacing = 2.sp
            ),
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier.padding(20.dp),
            text = stickyNoteDomain.description,
            maxLines = 5,
            overflow = TextOverflow.Ellipsis,

            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                color = textColor,
                letterSpacing = 2.sp
            ),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = modifier
                .padding(5.dp)
                .fillMaxWidth(0.7f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                modifier = modifier.fillMaxWidth(),
                onClick = {
                    stickNoteViewModel.updateNotificatioStickNote(stickyNoteDomain) {
                        context.cancelNotification(stickyNoteDomain.noticafitionId.toInt())
                    }
                    onFinish()
                }
            ) {
                Text(stringResource(R.string.confirmar))
            }
        }
        Spacer(modifier = modifier.weight(0.1f))
    }
}

@Preview(showBackground = true)
@Composable
fun DescriptionPagePreview() {
    LembretesTheme {
        DescriptionPage(
            stickyNoteDomain = StickyNoteDomain(
                id = 1,
                name = "Teste",
                description = "descricao teste",
                dateTime = Date().time,
                noticafitionId = 22L,
                isRemember = true,
                tags = mutableListOf()
            ),
            onFinish = {}
        )
    }
}