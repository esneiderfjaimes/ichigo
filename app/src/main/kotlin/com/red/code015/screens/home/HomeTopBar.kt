package com.red.code015.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun HomeAppBar(scrollBehavior: TopAppBarScrollBehavior) {
    SmallTopAppBar(
        title = {
            Text(text = "ICHI.GO",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black
            )
        },
        actions = {
            TextButton(onClick = { /*TODO*/ }) {
                Text(text = "LAN")
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun HomeHeader(
    innerPadding: PaddingValues,
    content: LazyListScope.() -> Unit,
) {
    var summonerName by remember { mutableStateOf(TextFieldValue("")) }
    Column {
        Box(Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)) {
            OutlinedTextField(
                value = summonerName,
                leadingIcon = {
                    Icon(imageVector = Icons.Rounded.Search,
                        contentDescription = "Search Icon")
                },
                onValueChange = { summonerName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search Summoner") },
                shape = RoundedCornerShape(33)
            )
        }
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}