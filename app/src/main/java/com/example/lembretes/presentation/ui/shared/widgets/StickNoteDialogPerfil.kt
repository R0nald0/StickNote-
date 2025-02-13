package com.example.lembretes.presentation.ui.shared.widgets

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.lembretes.R
import com.example.lembretes.domain.model.User
import com.example.lembretes.domain.model.UserDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme
import com.example.lembretes.utils.createJpgImageFromInputStream

@Composable
fun StickNoteDialogPerfil(
    modifier: Modifier = Modifier,
    content : @Composable() ((onDissminn: ()->Unit) -> Unit),
    onDissmisRequest : ()->Unit,
    ) {
    Dialog(
        onDismissRequest = { onDissmisRequest() },
    ) {
        content(onDissmisRequest)
    }
}


@Composable
fun ContentDialog(
    user: User?,
    modifier: Modifier = Modifier,
    onDismissRequest : ()->Unit,
    onSave :(String,String)->Unit,
) {
    val context = LocalContext.current
    var name by rememberSaveable {
        mutableStateOf(user?.name ?: "")
    }
    var isError by remember {
        mutableStateOf(false)
    }
    var imagePefil by rememberSaveable {
        mutableStateOf(user?.photoProfile ?: "")
    }
    val laucher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uriImage ->
         if (uriImage == null) {
             Toast.makeText(context,"Nehuma Imagem Selecionada",  Toast.LENGTH_SHORT).show()
             return@rememberLauncherForActivityResult
         }

        imagePefil = context.createJpgImageFromInputStream(uri = uriImage)
    }

    isError = name.length > 10

    Box(
        modifier = modifier
            .padding(8.dp)
            .background(
                MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(size = 20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .clip(shape = RoundedCornerShape(30.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Customize seu Perfil",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            ImagePerfil(
                uriImage = imagePefil ,
                onAddPhoto = {
                laucher.launch("image/*")
            }

            )
            Spacer(modifier = Modifier.height(8.dp))
            StickNoteTextField(
                modifier = modifier,
                value = name,
                label = "Nome",
                isError = isError,
                onChange = {value->
                    name = value
                },
                maxLines = 1,
                singleLine = true,
                icon = { Icon(
                    Icons.Default.Person ,
                    contentDescription ="Icon person" ) },
                trailingIcon = { Icon(
                    Icons.Default.Info,
                    contentDescription = "Info Icon",
                    tint = Color.Red
                ) },
                supportTexting = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement =
                        if (isError) Arrangement.SpaceBetween
                        else Arrangement.End
                    ) {
                         if (isError)  Text(text = "Mínimo de 10 caracteres ",
                             style = MaterialTheme.typography.labelSmall
                             )
                        Text(text = "${name.length}/${10}",
                            style = MaterialTheme.typography.labelSmall
                            )
                    }
                }
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancelar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
                TextButton(onClick = {
                    if (name.isNotBlank()){
                        onSave(name,imagePefil.toString())
                        onDismissRequest()
                    }
                }) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Salvar")
                }
            }
        }
    }
}
@Composable
private fun ImagePerfil(
    uriImage: String,
    onAddPhoto:()->Unit
) {
    Box (
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape),
    ){
        AsyncImage(
            modifier = Modifier
                .size(width = 120.dp, height = 120.dp)
                .background(color = Color.Gray, shape = CircleShape),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            placeholder = painterResource(R.drawable.ic_person_24),
            model = uriImage
        )

        IconButton(onClick = onAddPhoto) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add Button",
                tint = Color.White,
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
        ImagePerfil (uriImage = "",{})
    }
}
@Preview
@Composable
private fun StickNoteDialogPerfilPreview() {
    LembretesTheme {
        StickNoteDialogPerfil (
            onDissmisRequest = {},
            content = {
                ContentDialog(
                    user = UserDomain(1,"Teste",""),
                    onDismissRequest = { /*TODO*/ },
                    onSave = {t, s ->}
                )
            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StickNoteDialogPerfilDarkPreview() {
    LembretesTheme {
        StickNoteDialogPerfil (
            onDissmisRequest = {},
            content = {
                ContentDialog(
                    user = UserDomain(1,"Testessssssss",""),

                    onDismissRequest = { /*TODO*/ },
                    onSave = {t, s ->}

                )
            }

        )

    }
}