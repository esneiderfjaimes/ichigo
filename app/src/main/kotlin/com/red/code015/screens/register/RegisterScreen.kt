package com.red.code015.screens.register

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.red.code015.utils.toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    nav: NavHostController,
    viewModel: RegisterViewModel = hiltViewModel(),
) {
    val context: Context = LocalContext.current
    viewModel.event.observe(LocalLifecycleOwner.current) {
        when (it) {
            is RegisterViewModel.Event.SummonerFound -> {
                viewModel.saveSummonerName(it.summonerSummary.name)
                nav.navigate("home")
            }
            is RegisterViewModel.Event.SummonerNotFound -> {
                context.toast(it.exception.message)
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Register") },
                navigationIcon = {
                    IconButton(onClick = { nav.navigate("home") }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "LAN")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                var summonerName by remember { mutableStateOf(TextFieldValue("")) }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Lorem Ipsum\n")
                        }
                        append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sit amet porta ex, id commodo magna.")
                    },
                    modifier = Modifier.padding(4.dp, 24.dp),
                    style = MaterialTheme.typography.headlineSmall
                )
                OutlinedTextField(
                    value = summonerName,
                    onValueChange = { summonerName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(text = "Summoner Name") },
                    shape = RoundedCornerShape(33)
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(33),
                    onClick = {
                        viewModel.summonerByName(summonerName.text)
                    }) {
                    Text(text = "Done")
                }
            }

        }
    )
}