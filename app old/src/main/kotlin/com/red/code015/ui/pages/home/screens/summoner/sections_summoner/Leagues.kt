@file:OptIn(ExperimentalMaterial3Api::class)

package com.red.code015.ui.pages.home.screens.summoner.sections_summoner

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.red.code015.data.model.LeagueUI
import com.red.code015.ui.common.MyCard
import com.red.code015.ui.common.SpaceH
import com.red.code015.ui.common.margin4
import com.red.code015.ui.common.padHor
import com.red.code015.utils.gradientEnd

@Composable
fun Leagues(leagues: List<LeagueUI>) {
    Box(Modifier
        .fillMaxWidth()
        .gradientEnd(colorScheme.background)) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.Center
        ) {
            SpaceH(padHor)
            leagues.sorted().forEach { LeagueItem(it) }
            SpaceH(padHor)
        }
    }
}

@Composable
fun LeagueItem(leagueUI: LeagueUI) {
    MyCard {
        Row(Modifier
            .width(IntrinsicSize.Max)
            .padding(margin4),
            horizontalArrangement = Arrangement.spacedBy(margin4),
            verticalAlignment = Alignment.CenterVertically) {
            GraphAndProgress(leagueUI)
            LeagueDetail(leagueUI)
        }
    }
}

@Composable
fun GraphAndProgress(league: LeagueUI) {
    val percentageProgress: Float = (league.points).toFloat() / (100f)
    Box(Modifier
        .size(100.dp)
        .border(
            width = 6.dp,
            color = league.color.copy(alpha = 0.2f),
            shape = CircleShape
        )) {
        Image(painter = painterResource(league.idResRank),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp))
        CircularProgressIndicator(
            progress = percentageProgress,
            modifier = Modifier.fillMaxSize(),
            color = league.color,
            strokeWidth = 6.dp
        )
    }
}

@Composable
fun LeagueDetail(league: LeagueUI) {
    val percentage = league.wins.toFloat() / (league.wins.toFloat() + league.losses.toFloat())
    Column(Modifier.fillMaxWidth()) {
        Box(modifier = Modifier
            .clip(RoundedCornerShape(33))
            .background(colorScheme.onPrimary)) {
            Text(
                text = stringResource(id = league.idResQueueType),
                modifier = Modifier.padding(2.dp),
                fontSize = 10.sp,
                color = colorScheme.primary.copy(alpha = 0.75f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = league.summary(), color = league.color,
                fontWeight = FontWeight.Black,
                style = typography.titleLarge)
            if (league.hotStreak) Row(
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

        if (league.miniSeries == null) {
            Text(text = "${league.points} LP", style = typography.bodyMedium)
        } else Column(Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(25))
            .background(LocalContentColor.current.copy(alpha = 0.05f))
            .padding(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "${league.points} LP", style = typography.bodyMedium)
            Row(Modifier) {
                league.miniSeries.progress.toList().forEach {
                    Icon(
                        imageVector = when (it) {
                            'L' -> Icons.Rounded.Cancel
                            'N' -> Icons.Rounded.RadioButtonUnchecked
                            else -> Icons.Rounded.CheckCircle
                        },
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = when (it) {
                            'L' -> colorScheme.error
                            'N' -> LocalContentColor.current
                            else -> colorScheme.primary
                        })
                }
            }
        }

        Text(buildAnnotatedString {
            append("${league.wins}W ${league.losses}L ")
            withStyle(style = SpanStyle(
                color = if (percentage < 0.5f) colorScheme.error else colorScheme.primary,
                fontWeight = FontWeight.Black
            )) {
                append("${(percentage * 100).toInt()}%")
            }
        })
    }
}