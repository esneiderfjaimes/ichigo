@file:OptIn(ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.home.screens.summoner.sections_summoner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.red.code015.R
import com.red.code015.data.model.LeagueUI2
import com.red.code015.data.model.summary
import com.red.code015.ui.pages.home.screens.summoner.margin
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal

val spacer = paddingHorizontal - margin

@Composable
fun Leagues(leagues: List<LeagueUI2>) {
    Box(modifier = Modifier.fillMaxWidth()) {
        val color = colorScheme.background
        Color.Red
        LazyRow(modifier = Modifier
            .padding(vertical = 0.dp)
            .drawWithCache {
                val gradient = Brush.horizontalGradient(
                    colors = listOf(Color.Transparent,
                        color.copy(0.75f),
                        color),
                    startX = size.width - (paddingHorizontal.toPx() * 1.75f),
                    endX = size.width
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.SrcOver)
                }
            },
            horizontalArrangement = Arrangement.spacedBy(margin)) {
            item { Spacer(Modifier.width(spacer)) }
            items(leagues) { LeagueItem(leagueUI = it) }
            item { Spacer(Modifier.width(spacer)) }
        }
    }
}

@Composable
fun LeagueItem(leagueUI: LeagueUI2) {
    val percentage =
        ((leagueUI.wins).toFloat() / (leagueUI.wins.toFloat() + leagueUI.losses.toFloat()))

    Card(modifier = Modifier
        .defaultMinSize(minWidth = 250.dp),
        contentColor = contentColorFor(colorScheme.background).copy(alpha = 0.75f),
        containerColor = colorScheme.background,
        border = BorderStroke(0.5.dp, colorScheme.outline.copy(alpha = 0.25f))) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)) {
            Box(modifier = Modifier
                .size(100.dp)
                .padding(0.dp)
                .border(
                    width = 6.dp,
                    color = leagueUI.color.copy(alpha = 0.2f),
                    shape = CircleShape
                )) {
                Image(painter = painterResource(leagueUI.idResRank),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp))
                CircularProgressIndicator(
                    progress = percentage,
                    modifier = Modifier.fillMaxSize(),
                    color = leagueUI.color,
                    strokeWidth = 6.dp
                )
            }
            Column {
                Box(modifier = Modifier
                    .clip(RoundedCornerShape(33))
                    .background(colorScheme.onPrimary)) {
                    Text(
                        text = stringResource(id = leagueUI.idResQueueType),
                        modifier = Modifier.padding(2.dp),
                        fontSize = 10.sp,
                        color = colorScheme.primary.copy(alpha = 0.5f)
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = leagueUI.summary(), color = leagueUI.color,
                        fontWeight = FontWeight.Black,
                        style = typography.titleLarge)
                    if (leagueUI.hotStreak) Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(33))
                            .background(Color(0xFFFF9417).copy(alpha = 0.25f))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(1.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(painter = painterResource(id = R.drawable.ic_racha),
                            contentDescription = null)
                        Text(text = "1", color = Color(0xFFFF9417)) // TODO
                    }
                }
                Text(text = "${leagueUI.points} LP",
                    style = typography.bodyMedium)
                Text(buildAnnotatedString {
                    append("${leagueUI.wins}W ${leagueUI.losses}L ")
                    withStyle(style = SpanStyle(color = if (percentage < 0.5f) {
                        colorScheme.error
                    } else {
                        colorScheme.primary
                    })) {
                        append("${(percentage * 100).toInt()}%")
                    }
                })
            }
        }
    }
}