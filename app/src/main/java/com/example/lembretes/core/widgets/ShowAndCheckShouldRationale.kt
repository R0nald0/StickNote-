package com.example.lembretes.core.widgets

import android.app.Activity
import android.content.pm.PackageManager
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.lembretes.R
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog

@Composable
fun ShowAndCheckShouldRationale(
    modifier: Modifier = Modifier,
    activity :Activity,
    permission :String,
    messagem :String,
    onCancel:()->Unit,
    onAccept:()->Unit
) {

    var isRationale = remember {
        ContextCompat.checkSelfPermission(activity.applicationContext,permission) != PackageManager.PERMISSION_GRANTED
                && !activity.shouldShowRequestPermissionRationale(permission)
    }

    if (isRationale){
        StickNoteDialog(
            modifier = modifier,
            onDissmisRequest ={},
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
                        Text("Permissão Necessaria",
                            style = MaterialTheme.typography.titleMedium
                            )
                        Spacer(modifier.height(16.dp))
                        Text(messagem,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            TextButton(
                                onClick = onCancel,
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = Color.Red,
                                    containerColor = Color.Transparent
                                ),
                            ) {
                                Text(stringResource(R.string.negar),
                                    style =MaterialTheme.typography.labelMedium
                                    )
                            }
                            TextButton(
                                onClick = onAccept,
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = MaterialTheme.colorScheme.primary,
                                    containerColor = Color.Transparent
                                ),
                            ) {
                                Text(stringResource(R.string.ok),
                                    style =MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}