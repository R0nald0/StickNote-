package com.example.lembretes.presentation.ui.home.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lembretes.domain.model.User
import com.example.lembretes.presentation.model.NavigationItemDataClass
import com.example.lembretes.presentation.ui.shared.widgets.ContentDialog
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialogPerfil
import com.example.lembretes.utils.dateForExtense
import kotlinx.coroutines.launch
import java.util.Date


@Composable
fun StickNoteDrawer(
    modifier: Modifier = Modifier,
    user: User,
    drawerState: DrawerState,
    onNavigateToSettingsScreen: () -> Unit,
    onClickMenu: () -> Unit,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()


    val menus = listOf(
        NavigationItemDataClass(
            icon = Icons.Filled.Settings,
            label = "Configurações",
            selected = false,
            onClick = {
                onNavigateToSettingsScreen
                scope.launch { drawerState.close() }
            },
            badge = ""
        ),
    )

    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            StickNoteDrawerContent(
                modifier = Modifier,
                optionsMenus = menus,
                onClickMenu = onClickMenu
            )
        },
        content = content
    )
}

@Composable
private fun StickNoteDrawerContent(
    modifier: Modifier = Modifier,
    today: String = Date().dateForExtense(),
    onClickMenu: () -> Unit,
    optionsMenus: List<NavigationItemDataClass>
) {

    ModalDrawerSheet(
        modifier = modifier.fillMaxWidth(fraction = 0.7f)
    )
    {
        Surface(
            modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.2f)
        ) {
            Row(
                modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = today,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
//                         Icon(
//                              painter = painterResource(id = R.drawable.sun),
//                             contentDescription = "sun",
//                             modifier = modifier.size(60.dp),
//                             tint = Color.Yellow
//                         )
            }
        }

        HorizontalDivider()
        Spacer(modifier = modifier.height(10.dp))
        optionsMenus.forEach { itemNav ->
            NavigationDrawerItem(
                icon = { Icon(itemNav.icon, contentDescription = "User icons") },
                label = { Text(text = itemNav.label) },
                selected = itemNav.selected,
                onClick = itemNav.onClick,
                badge = { Text(itemNav.badge) }
            )
        }

    }
}