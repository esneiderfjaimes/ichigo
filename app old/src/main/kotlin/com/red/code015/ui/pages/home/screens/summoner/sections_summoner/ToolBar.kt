@file:OptIn(ExperimentalFoundationApi::class)

package com.red.code015.ui.pages.home.screens.summoner.sections_summoner

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.red.code015.R
import com.red.code015.data.model.SummonerUI
import com.red.code015.ui.common.margin
import com.red.code015.ui.components.AsyncImage
import com.red.code015.ui.components.ImageBackground
import com.red.code015.ui.pages.home.screens.summoner.paddingHorizontal
import com.red.code015.ui.theme.md_theme_dark_onSurface
import com.red.code015.utils.Coil

@Composable
fun LazyItemScope.ToolBar(summoner: SummonerUI, bgChampId: String?) {

    val painterResource = painterResource(id = R.mipmap.riot_desktop_background)
    var painter by remember {
        mutableStateOf(painterResource)
    }
    ImageBackground(
        Modifier.animateItemPlacement(),
        painter = painter,
        paddingHorizontal = paddingHorizontal,
        verticalArrangement = Arrangement.spacedBy(margin),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(margin),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sizeSummonerIcon = 80.dp
            Box(Modifier.height(sizeSummonerIcon)) {
                AsyncImage(Modifier
                    .size(sizeSummonerIcon)
                    .clip(RoundedCornerShape(33)),
                    Coil.urlProfileIcon(
                        summoner.profileIconId),
                    loading = {
                        Box(Modifier.size(sizeSummonerIcon)) {
                            CircularProgressIndicator(modifier = Modifier.align(
                                Alignment.Center))
                        }
                    }
                )
                if (summoner.level > 0) {
                    Text(text = summoner.level.toString(),
                        Modifier
                            .clip(CircleShape)
                            .background(colorScheme.surface.copy(
                                alpha = 0.9f))
                            .padding(8.dp, 2.dp)
                            .align(Alignment.BottomCenter),
                        style = typography.bodySmall)
                }
            }
            Column {
                Text(text = summoner.name,
                    fontWeight = FontWeight.Black,
                    color = md_theme_dark_onSurface,
                    style = typography.titleLarge.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ))

                if (summoner.account != null)
                    Text(text = "${summoner.account.gameName}#${summoner.account.tagLine}",
                        color = md_theme_dark_onSurface.copy(alpha = 1f),
                        style = typography.bodySmall)
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(margin)
        ) {
            Button(onClick = { /*TODO*/ },
                modifier = Modifier.weight(0.5f)) {
                Text(text = "Update")
            }
            FilledTonalButton(onClick = { /*TODO*/ },
                modifier = Modifier.weight(0.5f)) {
                Text(text = "In game")
            }
        }
    }

    bgChampId?.let {
        Log.i("TAG", "ToolBar: https://ddragon.leagueoflegends.com/cdn/img/champion/splash/$it.jpg")
        AsyncImage(
            model = "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${it}.jpg",
            success = {
                painter = this.painter
            }
        )
    }
}