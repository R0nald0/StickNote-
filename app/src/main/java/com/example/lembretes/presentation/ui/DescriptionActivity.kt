package com.example.lembretes.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.lembretes.utils.convertDateLongToString
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
                    Box (
                     modifier = Modifier.fillMaxSize()
                    ){
                       val extra  = intent.getStringExtra("st")

                        if (extra == null) return@Box
                        val stickNote = Gson().fromJson<StickyNoteDomain>(extra, StickyNoteDomain::class.java)

                        Image(modifier = Modifier.fillMaxSize(),
                            painter = painterResource(R.drawable.postit4), contentDescription = "",
                            contentScale = ContentScale.FillBounds
                            )
                        DescriptionPage(
                            activity = this@DescriptionActivity,
                            modifier = Modifier,
                            stickyNoteDomain =  stickNote
                        )
                    }
            }
        }
    }
}

@Composable
fun DescriptionPage(
   activity: Activity,
   stickyNoteDomain: StickyNoteDomain ,
   modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val userViewModel = hiltViewModel<UserViewModel>()
    val stickNoteViewModel  = hiltViewModel<StickNoteViewmodel>()

     val (_,name)  =  userViewModel.user.collectAsStateWithLifecycle().value
    SideEffect {
        userViewModel.findFirstUser()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(38.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = modifier
            .height(100.dp)
            .weight(0.5f))
        Text(
             modifier = Modifier
                 .padding(20.dp)
                 .weight(0.3f),
             text = "Olá ${name},você tem um Lembrete agendado para o dia",
             style = MaterialTheme.typography.titleMedium.copy(
                 fontSize = 25.sp,
                 color = MaterialTheme.colorScheme.primary,
                 letterSpacing = 2.sp
             ),
             textAlign = TextAlign.Center
            )
        Text(
            text =  Date().convertDateLongToString(stickyNoteDomain.dateTime)?:"00/00/0000",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            modifier = Modifier.padding(20.dp),
            text = stickyNoteDomain.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
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
                    stickNoteViewModel.updateNotificatioStickNote(stickyNoteDomain){
                        if (stickyNoteDomain.id == null ) {
                            Toast.makeText(context, "Erro ao cancelar a notificação", Toast.LENGTH_SHORT).show()
                            return@updateNotificatioStickNote
                        }
                        context.cancelNotification(stickyNoteDomain.noticafitionId.toInt())
                    }
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
        DescriptionPage( activity = Activity(), stickyNoteDomain = StickyNoteDomain(
            id = 1,
            name = "Teste",
            description = "descricao teste" ,
            dateTime =  Date().time,
            noticafitionId = 22L,
            isRemember =  true,
            tags = mutableListOf()
        ))
    }
}