package com.example.lembretes.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteAppBar
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.PreferencesViewModel
import com.example.lembretes.presentation.viewmodel.UserPreference

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    onClosed:()->Unit,
    preferencesViewModel: PreferencesViewModel,
    userPreference: UserPreference
    ) {
    Scaffold (
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            StickNoteAppBar(
                title = "Configurações",
                onClosed = onClosed
            )
        }
    ){paddingValues->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)

        ) {
            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "DarkMode",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Switch(
                    modifier = Modifier.padding(8.dp),
                    checked = userPreference.isDakrMode,
                    onCheckedChange ={ value->
                       preferencesViewModel.updateDarkMode(value)
                    },
                    thumbContent = {
                        if (userPreference.isDakrMode){
                            Icon(
                                Icons.Filled.CheckCircle,
                                contentDescription = "Is Dark Mode true",
                                tint = Color.Green
                                )
                        }else{
                            Icon(
                                Icons.Rounded.Close,
                                contentDescription = "Is Dark Mode false")
                        }
                    }
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.height(18.dp))
        }
    }

}
data class radioBottonClass(
    val isSelected : Boolean,
    val title :String
)

@Composable
fun DialogDarkMode(
    modifier: Modifier = Modifier,
    onDissmiss:()->Unit
) {
    val radioGroup = remember {
        mutableStateListOf(
            radioBottonClass(
                isSelected = false,
                title = "Light"
            ),
            radioBottonClass(
                isSelected = false,
                title = "Dark"
            ),
            radioBottonClass(
                isSelected = false,
                title = "Mesmo que o sistema"
            )
        )
    }
     var selectedOption  by remember {
        mutableStateOf("")
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .background(Color.White, shape = RoundedCornerShape(size = 20.dp))
        ,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(30.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = " Dark mode",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            radioGroup.forEach { radioBottonClass ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = radioBottonClass.title
                        },

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = radioBottonClass.title == selectedOption,
                        onClick = {
                            selectedOption = radioBottonClass.title
                        })
                    Text(text = radioBottonClass.title)
                }
            }
            Spacer(modifier = modifier.height(16.dp))
            Row(
                modifier =Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { onDissmiss() }) {
                    Text(text = "Cancelar")
                }
                TextButton(onClick = {
                    onDissmiss()
                }) {
                    Text(text = "Salvar")
                }
            }

        }

        }

}


@Preview
@Composable
private fun DialogDarkModePrev() {
    LembretesTheme {
        DialogDarkMode(
            onDissmiss = {}
        )
    }
}

@PreviewLightDark()
@Preview
@Composable
private fun SettingScreenPrev() {
    LembretesTheme {
        SettingScreen(
            preferencesViewModel = (PreferencesViewModel::class) as PreferencesViewModel,
            userPreference = UserPreference(),
            onClosed = {}
        )
    }
}