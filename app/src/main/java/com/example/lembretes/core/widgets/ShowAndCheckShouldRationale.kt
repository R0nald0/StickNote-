package com.example.lembretes.core.widgets

import android.Manifest
import android.app.Activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.lembretes.R
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog


@Composable
fun ShowAndCheckShouldRationale(
    modifier: Modifier = Modifier,
    activity: Activity,
    permission: String,
    onCancel :()-> Unit,
    onAccept: () -> Unit
) {

    var showRationale by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var permi by remember { mutableStateOf(permission) }



    when (permi) {
        Manifest.permission.SCHEDULE_EXACT_ALARM -> {
            message =
                "Para garantir que você receba lembretes importantes,este aplicativo precisa da permissão de Alarme,Isso nos permite exibir\n" +
                        "alertas sobre suas tarefas e compromissos no momento certo.Conceda a\n" +
                        "permissão para manter seus lembretes sempre à vista!"
            showRationale = true
        }

        Manifest.permission.POST_NOTIFICATIONS -> {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                message = ContextCompat.getString(activity, R.string.permission_explication)
                showRationale = true
            }
        }

    }


    if (showRationale) {
        StickNoteDialog(
            modifier = modifier,
            onDissmisRequest = {},
            content = {
                Surface(
                    modifier = modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            MaterialTheme.colorScheme.background,
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .clip(shape = RoundedCornerShape(30.dp)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            "Permissão Necessaria",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier.height(16.dp))
                        Text(
                            message,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            TextButton(
                                onClick = {
                                    StickNoteLog.info("SHOW $showRationale")
                                    showRationale = false
                                    onCancel()
                                    StickNoteLog.info("SHOW $showRationale")
                                },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Red,
                                    containerColor = Color.Transparent
                                ),
                            ) {
                                Text(
                                    stringResource(R.string.negar),
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                            TextButton(
                                onClick = {
                                    showRationale = false

                                    onAccept()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    containerColor = Color.Transparent
                                ),
                            ) {
                                Text(
                                    stringResource(R.string.ok),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}
