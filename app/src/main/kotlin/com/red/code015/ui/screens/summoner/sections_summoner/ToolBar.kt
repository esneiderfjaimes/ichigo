package com.red.code015.ui.screens.summoner.sections_summoner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.red.code015.R
import com.red.code015.domain.Summoner
import com.red.code015.ui.components.ImageBackground
import com.red.code015.ui.screens.summoner.margin
import com.red.code015.ui.screens.summoner.paddingHorizontal
import com.red.code015.utils.Coil

@Composable
fun ColumnScope.ToolBar(summoner: Summoner) {
    ImageBackground(
        painter = painterResource(id = R.mipmap.pyke_44),
        paddingHorizontal = paddingHorizontal,
        verticalArrangement = Arrangement.spacedBy(margin),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(margin),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val sizeSummonerIcon = 80.dp
            Box(Modifier.height(sizeSummonerIcon)) {
                SubcomposeAsyncImage(Coil.urlProfileIcon(
                    summoner.profileIconId),
                    contentDescription = "Summoner Icon",
                    Modifier
                        .size(sizeSummonerIcon)
                        .clip(RoundedCornerShape(33)),
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
                    style = typography.titleLarge)

                if (summoner.account != null)
                    Text(text = "${summoner.account!!.gameName}#${summoner.account!!.tagLine}",
                        color = colorScheme.onSurface.copy(alpha = 0.5f),
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
}