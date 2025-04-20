package com.example.lembretes.presentation.ui.profilepage

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.lembretes.R
import com.example.lembretes.presentation.ui.shared.widgets.ContentDialog
import com.example.lembretes.presentation.ui.shared.widgets.StickNoteDialog
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.presentation.viewmodel.UserViewModel

@Composable
fun ProfilePage(modifier: Modifier = Modifier,profileViewModel : UserViewModel = viewModel()) {
     val context = LocalContext.current
    val user by profileViewModel.user.collectAsStateWithLifecycle()
    var showPerfilDialog by remember {
        mutableStateOf(false)
    }


    Surface {
        val context = LocalContext.current
        var imagePefil by rememberSaveable { mutableStateOf(user.photoProfile) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = "Customize seu Perfil",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(16.dp))
            ImagePerfil(
                uriImage = imagePefil ,
                onAddPhoto = {
                     showPerfilDialog = !showPerfilDialog
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceAround
                ) {
               StickNoteIconText()
               StickNoteIconText()
               StickNoteIconText()
            }
        }
    }

    if (showPerfilDialog) {
        StickNoteDialog(
            content = {
                ContentDialog(
                    user = user,
                    onDismissRequest = { showPerfilDialog = !showPerfilDialog },
                    onSave = { name, photoPath ->
                        profileViewModel.crateUser(
                            name = name,
                            urlPerfilPhoto = photoPath
                        ) { message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                )
            },
            onDissmisRequest = { showPerfilDialog = !showPerfilDialog },
        )
    }
}

@Composable
fun StickNoteIconText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                color = Color.Gray.copy(
                    alpha = 0.2f),
                shape = RoundedCornerShape(25.dp)
            )
            .padding(8.dp)

        ,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = {},
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = "Icon Time")
        }
        Text(
            text =  "Salvador/Ba",
            style = MaterialTheme.typography.labelLarge
            )
        Text("35°c",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun StickNoteIconTextPreview() {
   LembretesTheme {
       StickNoteIconText()
   }

}

@Composable
private fun ImagePerfil(
    uriImage: String,
    onAddPhoto:()->Unit
) {
    Box (
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .border(
                border = BorderStroke(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = CircleShape
            )
            .padding(16.dp)
            .clip(CircleShape),
    ){

        AsyncImage(
            modifier = Modifier
                .size(width = 120.dp, height = 120.dp)
                .background(color = Color.Gray, shape = CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = "imagem do perfil",
            placeholder = painterResource(R.drawable.ic_person_24),
            model = uriImage
        )

        IconButton(onClick = onAddPhoto) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit Button",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        color = Color.Gray.copy(alpha = .5f),
                        shape = CircleShape)
            )
        }
    }
}

@Preview
@Composable
private fun ImagePerfilPreview() {
    LembretesTheme {
        ImagePerfil(uriImage = "") {}
    }
}



@Preview
@Composable
private fun ProfilePagePreview() {
    LembretesTheme {
        ProfilePage()
    }
}