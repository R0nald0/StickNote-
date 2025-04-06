package com.example.lembretes.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.presentation.ui.settings.models.ItemMenu
import com.example.lembretes.presentation.ui.settings.models.RadioButtonClass
import com.example.lembretes.presentation.ui.settings.widgets.StickNoteItemMenu
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteAppBar
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.PreferencesViewModel

@Composable
fun SettingScreen(
    preferencesViewModel : PreferencesViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onClosed: () -> Unit,
) {

    Scaffold(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        topBar = {
            StickNoteAppBar(
                title = "Configurações",
                onClosed = onClosed
            )
        }
    ) { paddingValues ->

        val userPrefe by preferencesViewModel.userPreference.collectAsStateWithLifecycle()


        var mode by remember {
             when(userPrefe.isDarkMode){
                 1 -> mutableStateOf("Light")
                 2 ->mutableStateOf("Dark")
                 else -> mutableStateOf("Mesmo que o sistema")
             }

        }
        var openDialog by remember {
            mutableStateOf(false)
        }
        if (userPrefe.loading){
            Box(
                modifier
                    .fillMaxSize()
                    .background(color = Color.Gray)
                    .alpha(0.7f),
                 contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        if (openDialog) {
            StickNoteDialog(

                onDissmisRequest ={openDialog = false}  ,
                content = {
                    DialogDarkMode(
                        idSelected = userPrefe.isDarkMode ?:3,
                        onSelected = {
                            radioSelected -> mode = radioSelected.title
                            openDialog = false
                            preferencesViewModel.updateDarkMode(radioSelected.id)
                        }
                    ) { }
                }
            )
        }

        val  context = LocalContext.current
        val menus = remember {
            mutableStateListOf(
                ItemMenu(
                    title = ContextCompat.getString(context,R.string.tema_do_app),
                   textOptionSelected =  mode,
                    action = {openDialog = !openDialog}
                ),
                ItemMenu(
                    title = "Zona",
                    textOptionSelected = "America/Sao Paulo",
                    action = {
                        //TODO criar acão
                    }
                ),
                ItemMenu(
                    title = "Tamanho titulo lembrete",
                    textOptionSelected = "Médio",
                    action = {
                        //TODO criar acão
                    }
                )
            )
        }
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier
                    .padding(paddingValues = paddingValues)
                    .fillMaxSize()
                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                menus.forEach { menu ->
                    StickNoteItemMenu(
                        title = menu.title,
                        mode = menu.textOptionSelected,
                        onClickMenu = menu.action
                    )
                }
            }
            Text(
                stringResource(R.string.vers_o_1_0_0),
                style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ))
        }

    }

}

@Composable
fun DialogDarkMode(
    idSelected : Int,
    onSelected: (RadioButtonClass) -> Unit,
    onDissmiss: () -> Unit
) {
    val radioGroup = remember {
        mutableStateListOf(
            RadioButtonClass(
                id = 1,
                isSelected = false,
                title = "Light"
            ),
            RadioButtonClass(
                id = 2,
                isSelected = false,
                title = "Dark"
            ),
            RadioButtonClass(
                id = 3,
                isSelected = false,
                title = "Mesmo que o sistema"
            )
        )
    }

    var selectedOption by remember {
        mutableIntStateOf(idSelected)
    }

    Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(shape = RoundedCornerShape(30.dp)),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
            Text(
                text =stringResource( R.string.tema_do_app),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            radioGroup.forEach { radioBottonClass ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedOption = radioBottonClass.id
                            onSelected(radioBottonClass)
                            onDissmiss()
                        },

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedColor = MaterialTheme.colorScheme.primary
                        ),
                        selected = radioBottonClass.id == selectedOption,
                        onClick = {
                            selectedOption = radioBottonClass.id
                            onSelected(radioBottonClass)
                            onDissmiss()
                        })
                     Text(
                         text = radioBottonClass.title,
                         color = MaterialTheme.colorScheme.onPrimaryContainer
                         )
                }
            }

        }



}


/*@Preview
@Composable
private fun DialogDarkModePrev() {
    LembretesTheme {
        DialogDarkMode(
            onSelected = {},
            onDissmiss = {}
        )
    }
}*/


@Preview
@Composable
private fun SettingScreenPrev() {
    LembretesTheme {
        SettingScreen(
            onClosed = {}
        )
    }
}