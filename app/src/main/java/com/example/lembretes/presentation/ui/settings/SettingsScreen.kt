package com.example.lembretes.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lembretes.R
import com.example.lembretes.core.log.StickNoteLog
import com.example.lembretes.core.notification.StickNoteSnackBarInfo
import com.example.lembretes.presentation.ui.settings.models.ItemMenu
import com.example.lembretes.presentation.ui.settings.models.RadioButtonClass
import com.example.lembretes.presentation.ui.settings.widgets.StickNoteItemMenu
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.GlobalVIewModel
import com.example.lembretes.presentation.viewmodel.PreferencesViewModel
import com.example.lembretes.presentation.viewmodel.UserPreference

@Composable
fun SettingScreen(
    globalVIewModel: GlobalVIewModel = viewModel(),
    preferencesViewModel: PreferencesViewModel = viewModel(),
    userPreference: UserPreference,
    modifier: Modifier = Modifier,
) {
    var zoneTime by remember { mutableStateOf(userPreference.timeZone) }
    var mode by remember {
        when (userPreference.isDarkMode) {
            1 -> mutableStateOf("Light")
            2 -> mutableStateOf("Dark")
            else -> mutableStateOf("Mesmo que o sistema")
        }
    }
    var sizeTitle by remember {
        when (userPreference.sizeTitleStickNote) {
            14 -> mutableStateOf("Pequeno")
            17 -> mutableStateOf("Mêdio")
            20 -> mutableStateOf("Grande")
            else -> mutableStateOf("Mêdio")
        }
    }


    var openDialogThemeApp by remember {
        mutableStateOf(false)
    }
    var openDialogTimeZone by remember {
        mutableStateOf(false)
    }
    var openDialogTextSize by remember {
        mutableStateOf(false)
    }

    if (userPreference.loading) globalVIewModel.showLoader()
    else globalVIewModel.hideLoader()

    if (userPreference.errorMessage != null) {
        StickNoteSnackBarInfo(
            message = userPreference.errorMessage ?: "Erro ao salvar preferência",
            onAction = {}
        )
    }

    if (openDialogThemeApp) {
        StickNoteDialog(
            onDissmisRequest = { openDialogThemeApp = false },
            content = {
                DialogDarkMode(
                    idSelected = userPreference.isDarkMode ?: 3,
                    onSelected = { radioSelected ->
                        mode = radioSelected.title
                        openDialogThemeApp = false
                        preferencesViewModel.updateDarkMode(radioSelected.id)
                    }
                ) { }
            }
        )
    }

    if (openDialogTimeZone) {
        StickNoteDialog(
            onDissmisRequest = { openDialogTimeZone = false },
            content = {
                TimeZoneContent(
                    onDissmiss = { openDialogTimeZone = false },
                    value = zoneTime.toString(),
                    onSelected = { timezone ->
                        openDialogTimeZone = false
                        preferencesViewModel.updateZoneTime(timezone)
                        zoneTime = timezone
                        StickNoteLog.info("SettingScreen:$timezone ")
                    }
                )
            }
        )
    }
    if (openDialogTextSize) {
        StickNoteDialog(
            onDissmisRequest = { openDialogTextSize = false },
            content = {
                TextSizeTitleContent (
                    onDissmiss = { openDialogTextSize = false },
                    idSelected = userPreference.sizeTitleStickNote ?: 2,
                    onSelected = {itemSelected ->
                        val (id,_,title) = itemSelected
                        sizeTitle =  title
                        preferencesViewModel.updateSizeTitle(id)
                    }
                )
            }
        )
    }

    val context = LocalContext.current
    val menus =
        listOf(
            ItemMenu(
                title = ContextCompat.getString(context, R.string.tema_do_app),
                textOptionSelected = mode,
                action = { openDialogThemeApp = !openDialogThemeApp }
            ),
            ItemMenu(
                title = "Zona",
                textOptionSelected = zoneTime
                    .toString(),
                action = { openDialogTimeZone = !openDialogTimeZone }
            ),
            ItemMenu(
                title = "Tamanho titulo lembrete",
                textOptionSelected = sizeTitle,
                action = {openDialogTextSize = !openDialogTextSize}
            )
        )


    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(text = stringResource(R.string.configura_es))
            Spacer(Modifier.height(15.dp))

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
            )
        )
    }

}

@Composable
fun DialogDarkMode(
    idSelected: Int,
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
            text = stringResource(R.string.tema_do_app),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeZoneContent(
    modifier: Modifier = Modifier,
    value: String,
    onSelected: (String) -> Unit,
    onDissmiss: () -> Unit
) {
    val timeZones = mapOf(
        "America/Sao_Paulo" to "America/Sao_Paulo GMT-03:00",
        "America/Manaus" to "America/Manaus GMT-04:00",
        "America/Rio_Branco" to "America/Rio_Branco GMT-05:00",
        "America/Noronha" to "America/NoronhaGMT-02:00",
    )
    var selectedItem by remember { mutableStateOf<String>(value) }

    Column(
        modifier
            .fillMaxHeight(.5f)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "Escolha a Zona de tempo no App "
        )
        LazyColumn(
            modifier = Modifier.weight(7f)
        ) {
            items(timeZones.toList(), key = { it.first }) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedItem = item.first
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedColor = MaterialTheme.colorScheme.primary
                        ),
                        selected = selectedItem == item.first,
                        onClick = {
                            selectedItem = item.first
                        })
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = item.second,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                HorizontalDivider()
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(2f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onDissmiss
            ) {
                Text(
                    text = "Cancelar",
                    color = MaterialTheme.colorScheme.error,
                )

            }
            TextButton(onClick = {
                if (selectedItem.isNotBlank()) {
                    onSelected(selectedItem)
                    onDissmiss()
                }
            }) {
                Text(
                    text = "Salvar",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

    }
}

@Composable
fun TextSizeTitleContent(
    modifier: Modifier = Modifier,
    idSelected: Int,
    onSelected: (RadioButtonClass) -> Unit,
    onDissmiss: () -> Unit
) {
    var selectedOption by remember {
        mutableIntStateOf(idSelected)
    }
    val radioGroup = remember {
        mutableStateListOf(
            RadioButtonClass(
                id = 14,
                isSelected = false,
                title = "Pequeno"
            ),
            RadioButtonClass(
                id = 17,
                isSelected = false,
                title = "Mêdio"
            ),
            RadioButtonClass(
                id = 20,
                isSelected = false,
                title = "Grande"
            )
        )
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(30.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Título Lembrete",
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
@Preview
@Composable
private fun SettingScreenPrev() {
    LembretesTheme {
        SettingScreen(
            userPreference = UserPreference()
        )
    }
}