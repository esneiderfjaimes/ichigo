@file:OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)

package com.red.code015.ui.pages.home.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.red.code015.R
import com.red.code015.ui.components.material_modifications.ModalBottomSheetState
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.State
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.State.SearchBy
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.State.SearchBy.RiotID
import com.red.code015.ui.pages.home.screens.register.RegisterViewModel.State.SearchBy.SummonerName
import com.red.code015.utils.PrefixTransformation

@Composable
fun RegisterForm(
    viewState: State,
    sheetState: ModalBottomSheetState,
    onSearchByChange: (SearchBy) -> Unit,
    onDoneBySummonerNameClick: (String) -> Unit,
    onDoneByRiotIDClick: (String, String) -> Unit,
) {
    val (first, second, third) = FocusRequester.createRefs()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    if (sheetState.isVisible) focusManager.clearFocus()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        var query by remember { mutableStateOf(TextFieldValue("")) }
        var queryTag by remember { mutableStateOf(TextFieldValue("")) }

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Lorem Ipsum\n")
                }
                append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean sit amet porta ex, id commodo magna.")
            },
            modifier = Modifier.padding(4.dp, 24.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        var expanded by remember { mutableStateOf(false) }
        TextButton(onClick = { expanded = true },
            modifier = Modifier
                .align(Alignment.End)) {
            Text(text = "${stringResource(R.string.search_by)} ${stringResource(viewState.searchBy.idRes)}")
            SearchByDropdown(expanded = expanded,
                onDismissRequest = { expanded = false },
                onItemClick = onSearchByChange)
        }

        Row {
            OutlinedTextField(
                value = query,
                enabled = !viewState.isLoading,
                onValueChange = { query = it },
                modifier = Modifier
                    .weight(1f)
                    .focusOrder(first) {
                        down = when (viewState.searchBy) {
                            SummonerName -> third
                            RiotID -> second
                        }
                    }
                    .focusRequester(focusRequester),
                placeholder = { Text(text = stringResource(viewState.searchBy.idRes)) },
                keyboardOptions = KeyboardOptions(imeAction = when (viewState.searchBy) {
                    SummonerName -> ImeAction.Search
                    RiotID -> ImeAction.Next
                }),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                    onSearch = { focusManager.clearFocus() }
                ),
                shape = RoundedCornerShape(33)
            )
            if (viewState.searchBy == RiotID)
                OutlinedTextField(
                    value = queryTag,
                    enabled = !viewState.isLoading,
                    onValueChange = {
                        if (it.text.length <= 5) {
                            queryTag = it
                        } else {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .width(80.dp)
                        .focusOrder(second) { down = third },
                    placeholder = { Text(text = stringResource(R.string.tagline)) },
                    shape = RoundedCornerShape(33),
                    visualTransformation = PrefixTransformation("#",
                        MaterialTheme.colorScheme.primary)
                )
        }

        if (viewState.isLoading) {
            CircularProgressIndicator(modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp))
        } else {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusOrder(third),
                shape = RoundedCornerShape(33),
                enabled = when (viewState.searchBy) {
                    SummonerName -> query.text.isNotBlank()
                    RiotID -> query.text.isNotBlank() && queryTag.text.isNotBlank()
                },
                onClick = {
                    when (viewState.searchBy) {
                        SummonerName -> onDoneBySummonerNameClick(query.text)
                        RiotID -> onDoneByRiotIDClick(query.text, queryTag.text)
                    }
                    focusManager.clearFocus()
                }) {
                Text(text = "Done")
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}