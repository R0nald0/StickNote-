package com.example.lembretes.presentation.ui.widgets

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lembretes.R
import com.example.lembretes.domain.model.User
import com.example.lembretes.domain.model.UserDomain
import com.example.lembretes.presentation.ui.theme.LembretesTheme

@Composable
fun StickNoteDialogPerfil(
    user: User?,
    modifier: Modifier = Modifier,
    onDissmisRequest : ()->Unit,
    onSave :(String,String)->Unit,
    ) {

   var name by rememberSaveable {
       mutableStateOf(user?.name ?: "")
   }
    var isError by remember {
        mutableStateOf(false)
    }

   isError = name.length > 10

    Dialog(
        onDismissRequest = { onDissmisRequest() },
    ) {

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
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Customize seu Perfil",
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
                ImagePerfil()
                Spacer(modifier = Modifier.height(8.dp))
                StickNoteTextField(
                    modifier = modifier,
                    value = name,
                    label = "Nome",
                    isError = isError,
                    onChange = {value->
                       name =value
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
                                .fillMaxWidth()
                                .padding(0.dp),
                            horizontalArrangement =
                            if (isError) Arrangement.SpaceBetween
                            else Arrangement.End
                        ) {

                            if (isError)  Text(text = "Minimo de 3 caracteres")
                            Text(text = "Limit ${name.length}/${10}")
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                     horizontalArrangement = Arrangement.End
                ){
                    TextButton(onClick = onDissmisRequest) {
                        Text(text = "Cancelar")
                    }
                    TextButton(onClick = {
                        if (name.isNotBlank()){
                            onSave(name,"")
                            onDissmisRequest()
                        }
                    }) {
                        Text(text = "Salvar")
                    }
                }
            }
        }
    }
}

@Composable
private fun ImagePerfil() {
    Box (
        contentAlignment = Alignment.BottomEnd,
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape),

    ){
        Image(
            modifier = Modifier
                .size(width = 120.dp, height = 120.dp)
                .background(color = Color.Gray, shape = CircleShape),
            painter = painterResource(id = R.drawable.tree),
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )
        IconButton(onClick = { /*TODO*/ }) {
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
        ImagePerfil ()
    }
}
@Preview
@Composable
private fun StickNoteDialogPerfilPreview() {
    LembretesTheme {
        StickNoteDialogPerfil (
            user = UserDomain(1,"Teste",""),
            onDissmisRequest = {},
            onSave = {name,photo-> }
        )

    }
}