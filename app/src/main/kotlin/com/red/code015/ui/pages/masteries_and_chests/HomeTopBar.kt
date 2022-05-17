package com.red.code015.ui.pages.masteries_and_chests

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.HighlightOff
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.red.code015.data.model.Platform
import com.red.code015.domain.Profile
import com.red.code015.ui.common.MyIcon
import com.red.code015.ui.common.MyIconButton
import com.red.code015.ui.components.AsyncImage
import com.red.code015.ui.components.RedIconButton
import com.red.code015.ui.components.SummonerIcon
import com.red.code015.ui.theme.SomaticRounded
import com.red.code015.utils.Coil
import com.red.code015.utils.MAX_PROFILES

enum class BoxState {
    Collapsed,
    Expanded
}

@Composable
fun HomeTopAppBar(
    profiles: List<Profile>,
    selectedProfile: Profile?,
    onProfileClick: (Profile) -> Unit,
    onAddProfileClick: () -> Unit,
    platform: Platform,
    onPlatformClick: () -> Unit,
    exception: String?,
) {
    Column {
        ErrorBar(exception = exception)
        CenterAlignedTopAppBar(
            title = {
                Text(text = "ICHI.GO",
                    color = colorScheme.primary,
                    style = MaterialTheme.typography.displaySmall.copy(fontFamily = SomaticRounded),
                    fontWeight = FontWeight.Black
                )
            },
            navigationIcon = {
                if (selectedProfile != null && MAX_PROFILES != 1) {
                    var expanded by remember { mutableStateOf(false) }
                    val sizeSummonerIcon = 50.dp
                    RedIconButton(onClick = { expanded = true }, Modifier.size(sizeSummonerIcon)) {
                        SummonerIcon(selectedProfile.profileIconID, sizeSummonerIcon)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        offset = DpOffset(0.dp, (10).dp)
                    ) {

                        profiles.forEach {
                            ItemProfile(it, selectedProfile == it) {
                                expanded = false
                                onProfileClick(it)
                            }
                        }

                        if (profiles.size < MAX_PROFILES) {
                            if (profiles.isNotEmpty()) {
                                MenuDefaults.Divider(Modifier.padding(top = 8.dp))
                            }
                            DropdownMenuItem(
                                text = { Text("Add") },
                                onClick = {
                                    expanded = false
                                    onAddProfileClick()
                                },
                                leadingIcon = { Icon(Icons.Rounded.Add, contentDescription = null) }
                            )
                        }
                    }
                }
            },
            actions = {
                TextButton(onClick = onPlatformClick) {
                    Text(text = platform.id.name)
                }
            }
        )
    }
}

@Composable
fun ErrorBar(exception: String?) {
    val transition = updateTransition(if (exception == null) BoxState.Collapsed
    else BoxState.Expanded, label = "height")

    val height by transition.animateDp(label = "height") { state ->
        when (state) {
            BoxState.Collapsed -> 0.dp
            BoxState.Expanded -> 25.dp
        }
    }

    Text(
        text = exception ?: "",
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(colorScheme.error)
            .padding(4.dp),
        textAlign = TextAlign.Center,
        color = colorScheme.onError
    )

    val color = if (exception != null) colorScheme.error else colorScheme.surface
    val darkIcons = if (isSystemInDarkTheme()) exception != null else exception == null
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = darkIcons
        )
    }
}

@Composable
private fun ItemProfile(
    profile: Profile,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(modifier = Modifier
        .padding(4.dp, 0.dp)
        .defaultMinSize(minWidth = 150.dp)) {
        val content: @Composable RowScope.() -> Unit = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(50)),
                    model = Coil.urlProfileIcon(profile.profileIconID),
                    loading = {
                        Box(Modifier.size(30.dp)) {
                            CircularProgressIndicator(modifier = Modifier.align(
                                Alignment.Center))
                        }
                    }
                )
                Text(text = profile.name)
            }
        }

        if (isSelected) {
            Button(shape = RoundedCornerShape(25),
                onClick = onClick, modifier = Modifier.fillMaxWidth(), content = content,
                contentPadding = ButtonDefaults.TextButtonContentPadding)
        } else {
            TextButton(onClick = onClick,
                shape = RoundedCornerShape(25),
                modifier = Modifier.fillMaxWidth(),
                content = content)
        }
    }
}

@Composable
fun HomeHeader(
    innerPadding: PaddingValues,
    executeSearch: (String) -> Unit,
    content: LazyListScope.() -> Unit,
) {
    var query by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    val manager = LocalFocusManager.current
    Column {
        Box(Modifier
            .fillMaxWidth()
            .background(colorScheme.surface)
            .padding(16.dp)) {
            OutlinedTextField(
                value = query,
                trailingIcon = {
                    if (query.text.isNotEmpty())
                        MyIconButton(Icons.Rounded.HighlightOff) {
                            query = query.copy("")
                        }
                },
                leadingIcon = {
                    IconToggleButton(checked = query.text.isNotBlank(),
                        onCheckedChange = {
                            if (!it) {
                                manager.clearFocus(true)
                                executeSearch(query.text)
                            }
                        }) {
                        MyIcon(Icons.Rounded.Search,
                            tint = if (query.text.isNotBlank()) colorScheme.primary else colorScheme.onSurface)
                    }
                },
                onValueChange = { query = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(text = "Search Summoner") },
                shape = RoundedCornerShape(33),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                    keyboardType = KeyboardType.Ascii,
                    autoCorrect = false,
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (query.text.isNotBlank()) {
                            manager.clearFocus(true)
                            executeSearch(query.text)
                        }
                    }
                ),
                singleLine = true
            )
        }
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = content
        )
    }
}