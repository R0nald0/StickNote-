package com.example.lembretes.presentation.ui.settings

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.lembretes.presentation.ui.theme.GlobalSizeFont
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
    var mode by remember {
        when (userPreference.isDarkMode) {
            1 -> mutableStateOf("Light")
            2 -> mutableStateOf("Dark")
            else -> mutableStateOf("Mesmo que o sistema")
        }
    }
    var sizeTitle by remember {
        when (userPreference.sizeTitleStickNote) {
            18 -> mutableStateOf("Pequeno")
            20 -> mutableStateOf("Médio")
            23 -> mutableStateOf("Grande")
            else -> mutableStateOf("Médio")
        }
    }
    var sizeDesctiption by remember {
        when (userPreference.sizeDescriptionStickNote) {
            14 -> mutableStateOf("Pequeno")
            17 -> mutableStateOf("Médio")
            20 -> mutableStateOf("Grande")
            else -> mutableStateOf("Pequeno")
        }
    }


    var openDialogThemeApp by remember {
        mutableStateOf(false)
    }
    var openDialogTextSize by remember {
        mutableStateOf(false)
    }
    var openDialogTextDescriptionSize by remember {
        mutableStateOf(false)
    }

    if (userPreference.loading) globalVIewModel.showLoader()
    else globalVIewModel.hideLoader()

    if (userPreference.errorMessage != null) {
        StickNoteSnackBarInfo(
            message = userPreference.errorMessage,
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

    if (openDialogTextSize) {
        StickNoteDialog(
            onDissmisRequest = { openDialogTextSize = false },
            content = {
                TextSizeTitleContent (
                    radiosOptions = listOf(
                        RadioButtonClass(
                            id = 18,
                            isSelected = false,
                            title = "Pequeno"
                        ),
                        RadioButtonClass(
                            id = 20,
                            isSelected = false,
                            title = "Médio"
                        ),
                        RadioButtonClass(
                            id = 23,
                            isSelected = false,
                            title = "Grande"
                        )
                    ),
                    titulo = "Título Lembrete",
                    onDissmiss = { openDialogTextSize = false },
                    idSelected = userPreference.sizeTitleStickNote ?: 20,
                    onSelected = {itemSelected ->
                        val (id,_,title) = itemSelected
                        sizeTitle =  title
                        GlobalSizeFont.titleSize = id
                        preferencesViewModel.updateSizeTitle(id)
                    }
                )
            }
        )
    }
    if (openDialogTextDescriptionSize) {
        StickNoteDialog(
            onDissmisRequest = { openDialogTextDescriptionSize = false },
            content = {
                StickNoteLog.info("SIZE TITLE ${userPreference.sizeDescriptionStickNote}")
                TextSizeTitleContent (
                     radiosOptions = listOf(
                         RadioButtonClass(
                             id = 14,
                             isSelected = true,
                             title = "Pequeno"
                         ),
                         RadioButtonClass(
                             id = 17,
                             isSelected = false,
                             title = "Médio"
                         ),
                         RadioButtonClass(
                             id = 20,
                             isSelected = false,
                             title = "Grande"
                         )
                     ),
                    titulo = "Descrição do lembrete",
                    onDissmiss = { openDialogTextDescriptionSize = false },
                    idSelected = userPreference.sizeDescriptionStickNote ?: 14,
                    onSelected = {itemSelected ->
                        val (id,_,title) = itemSelected
                        sizeDesctiption =  title
                        GlobalSizeFont.descriptionSize = id
                        preferencesViewModel.updateDescription(id)
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
                title = stringResource(R.string.tamanho_t_tulo_lembrete),
                textOptionSelected = sizeTitle,
                action = {openDialogTextSize = !openDialogTextSize}
            ),
            ItemMenu(
                title = stringResource(R.string.tamanho_descri_o_lembrete),
                textOptionSelected = sizeDesctiption,
                action = {openDialogTextDescriptionSize = !openDialogTextDescriptionSize}
            )
        )
    Box(
        modifier = modifier.padding(16.dp),
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

@Composable
fun TextSizeTitleContent(
    radiosOptions : List<RadioButtonClass>,
    titulo : String,
    modifier: Modifier = Modifier,
    idSelected: Int,
    onSelected: (RadioButtonClass) -> Unit,
    onDissmiss: () -> Unit
) {
    var selectedOption by remember {
        mutableIntStateOf(idSelected)
    }
    val radioGroup = remember {
        radiosOptions
    }
    Column(
        modifier = modifier
            .padding(16.dp)
            .clip(shape = RoundedCornerShape(30.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = titulo,
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
                StickNoteLog.info("selected :$selectedOption")
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